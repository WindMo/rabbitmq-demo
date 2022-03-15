package ws.rabbitmq.demo.test.exchange.topic;

import ws.rabbitmq.demo.test.TConsumer;

/**
 * @author WindShadow
 * @version 2021-08-04.
 */

public class TopicExchangeConsumer extends TConsumer {

    public TopicExchangeConsumer(String consumerName, String queueName) {
        super(consumerName, queueName);
    }
}
