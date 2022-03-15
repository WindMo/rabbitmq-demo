package ws.rabbitmq.demo.test.exchange.direct;

import ws.rabbitmq.demo.test.TConsumer;

/**
 * @author WindShadow
 * @version 2021-08-04.
 */

public class DirectExchangeConsumer extends TConsumer {

    public DirectExchangeConsumer(String consumerName, String queueName) {
        super(consumerName, queueName);
    }
}
