package ws.rabbitmq.demo.test;

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.GetResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author WindShadow
 * @version 2021-07-29.
 */
@Slf4j
public class TConsumerAck extends TConsumer{

    public TConsumerAck(String consumerName, String queueName) {
        super(consumerName, queueName);
    }

    @SneakyThrows
    public void consume() {

        // 通过信道声明一个消费者
        // 队列名称，是否自动应答（ack）：自动应答，收到消息立刻应答；手动应答：调用channel.basicAck()进行手动应答
        boolean autoAck = false;// 手动应答
        channel.basicConsume(queueName, autoAck,this.getDeliverCallback(), s -> log.info("{} - 消费消息失败",consumerName));
        log.info("{} - 开始接受消息",consumerName);// 接受消费是异步的

//        GetResponse getResponse = channel.basicGet(queueName, true);
//        getResponse.getBody();// todo
    }

    @Override
    protected DeliverCallback getDeliverCallback() {

        return (s, delivery) -> {

            log.info("{} - 接受到消息 - [{}]: {}", consumerName, queueName, new String(delivery.getBody(), StandardCharsets.UTF_8));
            // 手动应答
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            log.info("{} - 手动应答 - [{}]: {}",consumerName,queueName, new String(delivery.getBody(), StandardCharsets.UTF_8));
        };
    }
}
