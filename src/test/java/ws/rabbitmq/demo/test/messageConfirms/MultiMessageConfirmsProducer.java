package ws.rabbitmq.demo.test.messageConfirms;

import com.rabbitmq.client.MessageProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ws.rabbitmq.demo.test.RabbitConstant;
import ws.rabbitmq.demo.test.TProducer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多条消息批量发布确认
 * 发布多个时同步等待，效率稍高，消息发布失败时不知道哪条失败了
 * @author WindShadow
 * @version 2021-07-29.
 */

@Slf4j
public class MultiMessageConfirmsProducer extends TProducer {
    public MultiMessageConfirmsProducer(String producerName, String queueName) {
        super(producerName, queueName);
        this.init();
    }

    // 发布消息达到该数量则进行批量发布确认
    private static final int MAX_SIZE = 100;
    private final AtomicInteger size = new AtomicInteger(0);

    @SneakyThrows
    private void init() {

        // 通过信道声明队列（默认连接到缺省的交换机）
        /*
         * 生成一个队列
         * 1.队列名称
         * 2.队列持久化（里面的消息默认存储在内存中），重启服务，队列存在，消息不存在
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费（排他性）
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        // 声明一个持久化的队列
        boolean durable = true;
        channel.queueDeclare(queueName,durable,false,false,null);
    }

    @SneakyThrows
    public void doSendMessage(String msg) {


        // 发送消息：交换机名称，队列名称，配置，消息字节
        // 配置：null消息不持久化，储在内存中
//        channel.basicPublish("",queueName,null,msg.getBytes(StandardCharsets.UTF_8));
        // 选择发布确认
        channel.confirmSelect();
//         配置： MessageProperties.PERSISTENT_TEXT_PLAIN，消息持久化，储在硬盘中
        channel.basicPublish(RabbitConstant.DEFAULT_EXCHANGE,queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes(StandardCharsets.UTF_8));
        if (size.addAndGet(1) == MAX_SIZE) {

            // 等待消息发送（发布）确认，此处是批量的
            boolean forConfirms = channel.waitForConfirms();
            if (forConfirms) {
                log.info("{} - 消息批量发送 {}条 => [{}]",producerName,size.get(),queueName);
            }else {
                log.info("{} - 消息批量发送 {}条 => [{}] >> 失败",producerName,size.get(),queueName);
            }
        }

    }
}
