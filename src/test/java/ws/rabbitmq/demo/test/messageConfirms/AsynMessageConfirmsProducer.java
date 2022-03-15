package ws.rabbitmq.demo.test.messageConfirms;

import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ws.rabbitmq.demo.test.RabbitConstant;
import ws.rabbitmq.demo.test.TProducer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 异步消息发布确认
 * 异步确认，效率高，发布失败时可知道哪条失败了
 * @author WindShadow
 * @version 2021-07-29.
 */

@Slf4j
public class AsynMessageConfirmsProducer extends TProducer {

    /**
     * 声明一个跳表map来作为发送过的消息的缓存
     * 当消息发送时进行一次添加记录，当消息被发布确认时，删除该缓存中的消息，剩下的就是未被确认的消息
     */
    private final ConcurrentSkipListMap<Long,Object> messageMap = new ConcurrentSkipListMap<>();

    public AsynMessageConfirmsProducer(String producerName, String queueName) {
        super(producerName, queueName);
        this.init();
        // 添加消息异步确认通知回调（成功回调，失败回调）
        this.channel.addConfirmListener(this.getSuccessfulConfirmCallback(),this.getFailConfirmCallback());
    }

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
    @Override
    protected void doSendMessage(String msg) {

       /*
           配置：null消息不持久化，储在内存中
           channel.basicPublish("",queueName,null,msg.getBytes(StandardCharsets.UTF_8));

        */
        // 发送消息：交换机名称，队列名称，配置，消息字节
        // 选择发布确认
        channel.confirmSelect();
        // 配置： MessageProperties.PERSISTENT_TEXT_PLAIN，消息持久化，储在硬盘中
        channel.basicPublish(RabbitConstant.DEFAULT_EXCHANGE,queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes(StandardCharsets.UTF_8));

    }

    @Override
    public void sendMessage(String msg) {

        super.sendMessage(msg);
        // 发布之后，进行本地的记录
        this.messageMap.put(this.count.get(),msg);
    }

    /**
     * 成功回调
     * @return
     */
    protected ConfirmCallback getSuccessfulConfirmCallback() {
        // 消息编号，是否是批量的
        return (deliveryTag, multiple) -> {

            Object message = messageMap.get(deliveryTag);
            if (multiple) {
                // 批量用的少此处不展开
                messageMap.clear();
            }else {
                // 删除已经发布确认的消息
                messageMap.remove(deliveryTag);
                log.info("{} - 确认的消息 - {}: {}",producerName,deliveryTag,message);
            }

        };
    }

    /**
     * 失败回调
     * @return
     */
    protected ConfirmCallback getFailConfirmCallback() {

        return (deliveryTag, multiple) -> {
            if (multiple) {
                // 批量用的少此处不展开
                log.info("{} - 失败的消息 - {}",producerName, this.messageMap);
            }else {

                // 得到发布失败的消息
                Object message = messageMap.get(deliveryTag);
                log.info("{} - 失败的消息 - {}",producerName,message);
                // 可以进行下一步处理...
            }
        };
    }
}
