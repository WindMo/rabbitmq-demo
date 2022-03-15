package ws.rabbitmq.demo.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ws.rabbitmq.demo.BaseProducer;

import java.nio.charset.StandardCharsets;


/**
 * @author WindShadow
 * @version 2021-07-28.
 */

@Slf4j
public class TProducer extends BaseProducer {

    public final String queueName;
    public TProducer(String producerName, String queueName) {
        super(producerName);
        this.queueName = queueName;
        this.init();
    }

    @SneakyThrows
    private void init() {

        // 通过信道声明队列（默认连接到缺省的交换机），队列创建遵循幂等性
        /*
         * 生成一个队列
         * 1.队列名称
         * 2.队列持久化（里面的消息默认存储在内存中），重启服务，队列存在，消息不存在
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费（排他性）
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        channel.queueDeclare(queueName,false,false,false,null);
    }

    @SneakyThrows
    protected void doSendMessage(String msg) {


        // 发送消息：交换机名称，队列名称，配置，消息字节
        // 配置：null消息不持久化，储在内存中
        channel.basicPublish(RabbitConstant.DEFAULT_EXCHANGE,queueName,null,msg.getBytes(StandardCharsets.UTF_8));
        // 配置： MessageProperties.PERSISTENT_TEXT_PLAIN，消息持久化，储在硬盘中
//        channel.basicPublish("",queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes(StandardCharsets.UTF_8));
        log.info("{} - 消息发送 {} => [{}]",producerName,msg,queueName);
    }

    @Override
    protected void doSendMessage(String msg, String routingKey) {
        throw new UnsupportedOperationException();
    }

}
