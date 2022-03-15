package ws.rabbitmq.demo.test.exchange.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ws.rabbitmq.demo.BaseProducer;

import java.nio.charset.StandardCharsets;

/**
 * 扇出交换机生产者
 * @author WindShadow
 * @version 2021-07-31.
 */
@Slf4j
public class FanoutExchangeProducer extends BaseProducer {

    public final String producerName;
    public final String[] queueNames;
    /** 交换机名称 */
    protected String exchangeName;
    protected String bindRoutingKey;

    public FanoutExchangeProducer(String producerName, String exchangeName, String bindRoutingKey, String... queueNames) {
        super(producerName);

        this.producerName = producerName;
        this.queueNames = queueNames;
        this.exchangeName = exchangeName;
        this.bindRoutingKey = bindRoutingKey;
        this.init();
    }

    @SneakyThrows
    private void init() {

        // 声明扇出交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);

        // 通过信道声明队列，绑定到指定交换机，队列创建遵循幂等性
        for (String queueName : queueNames) {

            /*
             * 生成一个队列
             * 1.队列名称
             * 2.队列持久化（里面的消息默认存储在内存中），重启服务，队列存在，消息不存在
             * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费（排他性）
             * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
             * 5.其他参数
             */
            channel.queueDeclare(queueName,false,false,true,null);// 测试阶段自动删除
            // 绑定队列到交换机
            channel.queueBind(queueName, exchangeName, bindRoutingKey);
        }
    }

    @SneakyThrows
    @Override
    protected void doSendMessage(String msg) {

        // 发送消息：交换机名称，routingKey，配置，消息字节
        channel.basicPublish(exchangeName,bindRoutingKey,null,msg.getBytes(StandardCharsets.UTF_8));// 配置：null消息不持久化，储在内存中
//        channel.basicPublish("",queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes(StandardCharsets.UTF_8)); // 配置： MessageProperties.PERSISTENT_TEXT_PLAIN，消息持久化，储在硬盘中
        log.info("{} - 消息发送 {} => e={} [{}]",producerName,msg,exchangeName,bindRoutingKey);
    }

    @Override
    protected void doSendMessage(String msg, String routingKey) {
        throw new UnsupportedOperationException();
    }
}
