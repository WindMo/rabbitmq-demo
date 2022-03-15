package ws.rabbitmq.demo.test.workqueue;

import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.rabbitmq.demo.test.TConsumer;
import ws.rabbitmq.demo.test.TConsumerAck;
import ws.rabbitmq.demo.test.TConsumerMesaageRequeue;
import ws.rabbitmq.demo.test.TProducer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 消息队列的生产与消费
 * 工作队列模式
 * @author WindShadow
 * @version 2021-07-27.
 */
@Slf4j
public class WorkQueueMain {


    public static final String QUEUE_NAME = "WorkQueueMain";

    public static void autoAck() {

        TProducer producer = new TProducer("生产者WorkQueueMain",QUEUE_NAME);
        TConsumer consumer1 = new TConsumer("消费者WorkQueueMain - 1",QUEUE_NAME);
        TConsumer consumer2 = new TConsumer("消费者WorkQueueMain - 2",QUEUE_NAME);
        consumer1.consume();
        consumer2.consume();
        // 两个消费者（两个工作线程），默认轮询方式消费消息
        Arrays.asList("AA","BB","CC","DD","EE").forEach(producer::sendMessage);

    }

    public static void manualAck() {

        TProducer producer = new TProducer("生产者WorkQueueMain", QUEUE_NAME);
        TConsumer consumer1 = new TConsumerAck("消费者WorkQueueMain - 1", QUEUE_NAME);
        TConsumer consumer2 = new TConsumerAck("消费者WorkQueueMain - 2", QUEUE_NAME);
        consumer2.consume();
        consumer1.consume();
        // 两个消费者（两个工作线程），默认轮询方式消费消息
        Arrays.asList("AA","BB","CC","DD","EE").forEach(producer::sendMessage);

    }

    public static void messageRequeue() {

        TProducer producer = new TProducer("生产者WorkQueueMain", QUEUE_NAME);
        TConsumer consumer1 = new TConsumerMesaageRequeue("消费者WorkQueueMain - 1", QUEUE_NAME);
        TConsumer consumer2 = new TConsumerAck("消费者WorkQueueMain - 2", QUEUE_NAME);

        // 两个消费者（两个工作线程），默认轮询方式消费消息
        Arrays.asList("AA","BB","CC","DD","EE").forEach(producer::sendMessage);
        // consumer1模拟了异常下的消息无法处理，处理，consumer1处理不了（无法应答）的消息最终会重新入队，由consumer2消费
        consumer1.consume();
        consumer2.consume();

    }

    public static void main(String[] args) {

//        autoAck();
//        manualAck();
        messageRequeue();
    }
}
