package ws.rabbitmq.demo.test.messageConfirms;

import ws.rabbitmq.demo.test.TConsumer;
import ws.rabbitmq.demo.test.TProducer;

import java.util.Arrays;

/**
 * @author WindShadow
 * @version 2021-07-29.
 */

public class MessageConfirmsMain {

    public static final String QUEUE_NAME = "MessageConfirmsMain";

    public static void singleMC() {

        TProducer producer = new SingleMessageConfirmsProducer("生产者MessageConfirmsMain",QUEUE_NAME);
        TConsumer consumer = new TConsumer("消费者MessageConfirmsMain",QUEUE_NAME);
        Arrays.asList("AA","BB","CC","DD","EE").forEach(producer::sendMessage);
        consumer.consume();
    }
    public static void multiMC() {

        TProducer producer = new MultiMessageConfirmsProducer("生产者MessageConfirmsMain",QUEUE_NAME);
        TConsumer consumer = new TConsumer("消费者MessageConfirmsMain",QUEUE_NAME);
        Arrays.asList("AA","BB","CC","DD","EE").forEach(producer::sendMessage);
        consumer.consume();
    }

    public static void asynMC() {

        TProducer producer = new AsynMessageConfirmsProducer("生产者MessageConfirmsMain",QUEUE_NAME);
        TConsumer consumer = new TConsumer("消费者MessageConfirmsMain",QUEUE_NAME);
        Arrays.asList("AA","BB","CC","DD","EE").forEach(producer::sendMessage);
        consumer.consume();
    }

    public static void main(String[] args) {

//        singleMC();
//        multiMC();
        asynMC();
    }
}
