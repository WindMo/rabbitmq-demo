package ws.rabbitmq.demo.test.deadQueue;

import com.rabbitmq.client.BuiltinExchangeType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ws.rabbitmq.demo.BaseProducer;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 死信交换机生产者
 * 如果普通队列配置了死信交换机，队列中的消息发生以下情况，则会将信息发送到死信交换机
 * 1、消息过期ttl时间到达（可用作延时队列）
 * 2、消息被客户端拒绝
 * 3、队列消息已满
 * @author WindShadow
 * @version 2021-07-31.
 */
@Slf4j
public class DeathLetterQueueProducer extends BaseProducer {

    public final String producerName;
    public final String[] queueNames;
    /** 交换机名称 */
    protected String exchangeName;
    protected String[] bindRoutingKeys;
    /** 正常队列消息过期时间 */
    protected int[] ttlArrays;
    // 死信交换机名称
    protected String deadLetterExchangeName;
    // 死信队列名称
    protected String deadLetterQueueName;

    public DeathLetterQueueProducer(String producerName, String exchangeName, String[] bindRoutingKeys, String[] queueNames, int[] ttlArrays) {
        super(producerName);

        this.producerName = producerName;
        this.queueNames = queueNames;
        this.exchangeName = exchangeName;
        this.bindRoutingKeys = bindRoutingKeys;
        this.ttlArrays = ttlArrays;
        // 将就在此命名
        this.deadLetterExchangeName = exchangeName + "&" + "DE";
        // 将就在此命名
        this.deadLetterQueueName = deadLetterExchangeName + "_" + "Queue";
        this.init();
    }

    public String getDeadLetterExchangeName() {
        return deadLetterExchangeName;
    }

    public String getDeadLetterQueueName() {
        return deadLetterQueueName;
    }

    @SneakyThrows
    private void init() {

        // 声明直接交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        // 声明死信交换机（直接交换机类型）
        channel.exchangeDeclare(deadLetterExchangeName,BuiltinExchangeType.DIRECT);

        // 声明死信交换机与死信队列的路由key
        final String  deadLetterQueueRoutingKey = "targetRK";
        // 声明一个路由到死信交换机的队列（死信队列）
        channel.queueDeclare(deadLetterQueueName,false,false,false,null);// 测试阶段自动删除
        // 死信队列绑定到死信交换机
        channel.queueBind(deadLetterQueueName, deadLetterExchangeName,deadLetterQueueRoutingKey);


        // 通过信道声明普通的队列（默认连接到缺省的交换机），队列创建遵循等幂性
        for (int i=0; i < queueNames.length; i++) {

            String queueName = queueNames[i];
            String bindRoutingKey =  bindRoutingKeys[i];

            // 设置声明队列需要的参数
            Map<String,Object> args = new HashMap<>();
            // 正常队列设置死信交换机
            args.put("x-dead-letter-exchange",deadLetterExchangeName);
            // 设置死信routingKey
            args.put("x-dead-letter-routing-key",deadLetterQueueRoutingKey);
            // 正常队列设置消息过期时间ttl
            args.put("x-message-ttl",ttlArrays[i]);

            /*
             * 生成一个队列
             * 1.队列名称
             * 2.队列持久化（里面的消息默认存储在内存中），重启服务，队列存在，消息不存在
             * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费（排他性）
             * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
             * 5.其他参数
             */
            channel.queueDeclare(queueName,false,false,true,args);// 测试阶段自动删除
            // 正常队列绑定到正常交换机
            channel.queueBind(queueName, exchangeName,bindRoutingKey);
        }


    }

    @SneakyThrows
    @Override
    protected void doSendMessage(String msg) {

      throw new UnsupportedOperationException();
    }

    @SneakyThrows
    @Override
    protected void doSendMessage(String msg, String routingKey) {

        assert Arrays.asList(this.bindRoutingKeys).contains(routingKey);
        // 发送消息：交换机名称，routingKey，配置，消息字节
        channel.basicPublish(exchangeName,routingKey,null,msg.getBytes(StandardCharsets.UTF_8));// 配置：null消息不持久化，储在内存中
//        channel.basicPublish("",queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes(StandardCharsets.UTF_8)); // 配置： MessageProperties.PERSISTENT_TEXT_PLAIN，消息持久化，储在硬盘中
        log.info("{} - {} 消息发送 {} => e={} [{}]",producerName,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),msg,exchangeName,routingKey);
    }
}
