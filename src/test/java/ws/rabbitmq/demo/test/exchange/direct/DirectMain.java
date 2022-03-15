package ws.rabbitmq.demo.test.exchange.direct;

import ws.rabbitmq.demo.test.exchange.fanout.FanoutExchangeConsumer;
import ws.rabbitmq.demo.test.exchange.fanout.FanoutExchangeProducer;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 直接交换机
 * @author WindShadow
 * @version 2021-08-04.
 */

public class DirectMain {

    public static final String EXCHANGE_NAME = "DirectMain";
    public static final String QUEUE_NAME1 = "DirectQ1";
    public static final String QUEUE_NAME2 = "DirectQ2";
    public static final String ROUTING_KEY1 = "d_q1";
    public static final String ROUTING_KEY2 = "d_q2";

    public static void main(String[] args) {

        DirectExchangeProducer producer = new DirectExchangeProducer("直接生产者",EXCHANGE_NAME,
                new String[]{ROUTING_KEY1,ROUTING_KEY2},
                new String[]{QUEUE_NAME1,QUEUE_NAME2});
        DirectExchangeConsumer consumer1 = new DirectExchangeConsumer("直接消费者1",QUEUE_NAME1);
        DirectExchangeConsumer consumer2 = new DirectExchangeConsumer("直接消费者2",QUEUE_NAME2);
        consumer1.consume();
        consumer2.consume();


        Arrays.asList("AA","BB","CC","DD","EE").forEach(str -> {

            if (Math.random() > 0.5) {

                producer.sendMessage(str,ROUTING_KEY1);
            }else {
                producer.sendMessage(str,ROUTING_KEY2);
            }

        });
    }
}
