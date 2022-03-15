package ws.rabbitmq.demo.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WindShadow
 * @version 2021-07-28.
 */

@Slf4j
public class TConsumer extends BaseConsumer{

    public TConsumer(String consumerName, String queueName) {
        super(consumerName,queueName);
    }

    @SneakyThrows
    public void consume() {

        // 通过信道声明一个消费者
        // 队列名称，是否自动应答（ack）：自动应答，收到消息立刻应答；手动应答：调用channel.basicAck()进行手动应答
        boolean autoAck = true;
        channel.basicConsume(queueName, autoAck,this.getDeliverCallback(), s -> log.info("{} - 消费消息失败",consumerName));
        log.info("{} - 开始接受消息",consumerName);// 接受消费是异步的
    }
}
