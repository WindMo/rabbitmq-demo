package ws.rabbitmq.demo.test.simple;

import lombok.extern.slf4j.Slf4j;
import ws.rabbitmq.demo.test.TConsumer;
import ws.rabbitmq.demo.test.TProducer;

import java.util.Scanner;

/**
 * 消息队列的生产与消费
 * 简单模式
 * @author WindShadow
 * @version 2021-07-27.
 */
@Slf4j
public class SimpleMain {

    public static final String QUEUE_NAME = "SimpleMain";
    public static void main(String[] args) {

        TProducer producer = new TProducer("生产者SimpleMain",QUEUE_NAME);
        TConsumer consumer = new TConsumer("消费者SimpleMain",QUEUE_NAME);
        consumer.consume();
        try (Scanner sc = new Scanner(System.in)) {

            while (true) {

                String msg = sc.next();
                producer.sendMessage(msg);
            }
        }
    }
}
