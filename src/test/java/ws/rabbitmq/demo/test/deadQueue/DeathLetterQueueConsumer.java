package ws.rabbitmq.demo.test.deadQueue;

import com.rabbitmq.client.DeliverCallback;

import lombok.extern.slf4j.Slf4j;
import ws.rabbitmq.demo.test.TConsumer;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 死信队列消费者
 * @author WindShadow
 * @version 2021-08-04.
 */
@Slf4j
public class DeathLetterQueueConsumer extends TConsumer {

    public DeathLetterQueueConsumer(String consumerName, String queueName) {
        super(consumerName, queueName);
    }

    @Override
    protected DeliverCallback getDeliverCallback() {

        return (s, delivery) -> log.info("{} - {} 接受到消息 - [{}]: {}", consumerName, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), queueName, new String(delivery.getBody(), StandardCharsets.UTF_8));
    }
}
