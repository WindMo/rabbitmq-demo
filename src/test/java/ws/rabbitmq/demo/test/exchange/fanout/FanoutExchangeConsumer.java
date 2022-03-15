package ws.rabbitmq.demo.test.exchange.fanout;

import ws.rabbitmq.demo.test.TConsumer;

/**
 * @author WindShadow
 * @version 2021-08-04.
 */

public class FanoutExchangeConsumer extends TConsumer {

    public FanoutExchangeConsumer(String consumerName, String queueName) {
        super(consumerName, queueName);
    }
}
