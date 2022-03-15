package ws.rabbitmq.demo.test.exchange.fanout;

import java.util.Scanner;

/**
 * @author WindShadow
 * @version 2021-08-04.
 */

public class FanoutMain {

    public static final String EXCHANGE_NAME = "FanoutMain";
    public static final String QUEUE_NAME1 = "FanoutQ1";
    public static final String QUEUE_NAME2 = "FanoutQ2";
    public static final String ROUTING_KEY = "logs";

    public static void main(String[] args) {

        FanoutExchangeProducer producer = new FanoutExchangeProducer("扇出生产者",EXCHANGE_NAME,ROUTING_KEY,QUEUE_NAME1,QUEUE_NAME2);
        FanoutExchangeConsumer consumer1 = new FanoutExchangeConsumer("扇出消费者1",QUEUE_NAME1);
        FanoutExchangeConsumer consumer2 = new FanoutExchangeConsumer("扇出消费者2",QUEUE_NAME2);
        consumer1.consume();
        consumer2.consume();


        try (Scanner sc = new Scanner(System.in)) {

            while (true) {

                String msg = sc.next();
                producer.sendMessage(msg);
            }
        }
    }
}
