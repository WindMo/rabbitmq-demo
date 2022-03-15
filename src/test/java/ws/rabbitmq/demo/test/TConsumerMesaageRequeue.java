package ws.rabbitmq.demo.test;

import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 消息重新入队测试
 * @author WindShadow
 * @version 2021-07-29.
 */
@Slf4j
public class TConsumerMesaageRequeue extends TConsumerAck{
    public TConsumerMesaageRequeue(String consumerName, String queueName) {
        super(consumerName, queueName);
    }

    private volatile int n = 0;
    @Override
    protected DeliverCallback getDeliverCallback() {

        return (s, delivery) -> {

            this.n++;
            log.info("{} - 接受到消息 - [{}]: {}", consumerName, queueName, new String(delivery.getBody(), StandardCharsets.UTF_8));
            if (n >= 1) {
                // 收到第1个消息后，模拟异常
                log.info("{} - 异常退出，无法进行消息应答",consumerName);
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,true);// 不确认消息，且消息重新入队
            }
            // 手动应答
            // 消息标记，是否批量应答（一般为否false）
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            log.info("{} - 手动应答 - [{}]: {}",consumerName,queueName, new String(delivery.getBody(), StandardCharsets.UTF_8));
        };
    }
}
