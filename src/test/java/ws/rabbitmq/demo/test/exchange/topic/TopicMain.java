package ws.rabbitmq.demo.test.exchange.topic;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * 主题交换机
 * @author WindShadow
 * @version 2021-08-04.
 */

public class TopicMain {

    public static final String EXCHANGE_NAME = "TopicMain";
    public static final String QUEUE_NAME1 = "TopicQ1";
    public static final String QUEUE_NAME2 = "TopicQ2";
    public static final String QUEUE_NAME3 = "TopicQ3";
    public static final String QUEUE_NAME4 = "TopicQ4";
    // routingKey * 代表一个单词，# 代表多个，与通配符不同
    public static final String[] ROUTING_KEYS1 = {"log.warn"};
    public static final String[] ROUTING_KEYS2 = {"log.info","log.error"};
    public static final String[] ROUTING_KEYS3 = {"log.#"};
    public static final String[] ROUTING_KEYS4 = {"log.*"};

    @SneakyThrows
    public static void main(String[] args) {

        TopicExchangeProducer producer = new TopicExchangeProducer("主题生产者",EXCHANGE_NAME,
                new String[][]{ROUTING_KEYS1,ROUTING_KEYS2,ROUTING_KEYS3,ROUTING_KEYS4},
                new String[]{QUEUE_NAME1,QUEUE_NAME2,QUEUE_NAME3,QUEUE_NAME4});
        TopicExchangeConsumer consumer1 = new TopicExchangeConsumer("主题消费者1",QUEUE_NAME1);
        TopicExchangeConsumer consumer2 = new TopicExchangeConsumer("主题消费者2",QUEUE_NAME2);
        TopicExchangeConsumer consumer3 = new TopicExchangeConsumer("主题消费者3",QUEUE_NAME3);
        TopicExchangeConsumer consumer4 = new TopicExchangeConsumer("主题消费者4",QUEUE_NAME4);
        consumer1.consume();
        consumer2.consume();
        consumer3.consume();
        consumer4.consume();

        TimeUnit.SECONDS.sleep(1);

        producer.sendMessage("AA","log.track");// 消费者：3 4
        producer.sendMessage("BB","log.info");// 消费者：2 3 4
        producer.sendMessage("CC","log.error");// 消费者：2 3 4
        producer.sendMessage("DD","log.warn");// 消费者：1 3 4
        producer.sendMessage("EE","log.other.ex");// 消费者： 3
    }
}
