package ws.rabbitmq.demo.test.deadQueue;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.rabbitmq.demo.test.TConsumerAck;
import ws.rabbitmq.demo.test.exchange.topic.TopicExchangeConsumer;
import ws.rabbitmq.demo.test.exchange.topic.TopicExchangeProducer;

import java.util.concurrent.TimeUnit;

/**
 * 死信队列
 * @author WindShadow
 * @version 2021-08-05.
 */

public class DeathLetterQueueMain {

    public static final String EXCHANGE_NAME = "NormalMain";
    public static final String QUEUE_NAME1 = "NormalQ1";
    public static final String QUEUE_NAME2 = "NormalQ2";

    public static final String ROUTING_KEY1 = "n_q1";
    public static final String ROUTING_KEY2 = "n_q2";

    @SneakyThrows
    public static void main(String[] args) {

        DeathLetterQueueProducer producer = new DeathLetterQueueProducer("死信生产者",EXCHANGE_NAME,
                new String[]{ROUTING_KEY1,ROUTING_KEY2},
                new String[]{QUEUE_NAME1,QUEUE_NAME2},
                new int[]{3 * 1000,5 * 1000});

        // 获取死信队列的名称
        String deadLetterQueueName = producer.getDeadLetterQueueName();
        // 使用消费者消费死信队列
        DeathLetterQueueConsumer consumer1 = new DeathLetterQueueConsumer("死信消费者",deadLetterQueueName);
        consumer1.consume();

        TimeUnit.SECONDS.sleep(1);

        producer.sendMessage("AA",ROUTING_KEY1);// 3s后死信消费者收到消息
        producer.sendMessage("BB",ROUTING_KEY2);// 5s后死信消费者收到消息
        producer.sendMessage("CC",ROUTING_KEY1);
        producer.sendMessage("DD",ROUTING_KEY2);
    }
}
