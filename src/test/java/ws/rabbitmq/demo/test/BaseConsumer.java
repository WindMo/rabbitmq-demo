package ws.rabbitmq.demo.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

import static ws.rabbitmq.demo.test.RabbitConstant.CONNECTION_FACTORY;

/**
 * @author WindShadow
 * @version 2021-07-28.
 */

@Slf4j
public abstract class BaseConsumer {

    protected final String consumerName;
    protected final String queueName;
    protected Connection connection;
    protected Channel channel;

    public BaseConsumer(String consumerName, String queueName) {

        this.consumerName = consumerName;
        this.queueName = queueName;
        this.init();
    }

    @SneakyThrows
    private void init() {

        // 打开连接，创建信道
        connection = CONNECTION_FACTORY.newConnection(consumerName);
        channel = connection.createChannel();
    }

    public abstract void consume();

    // 消费回调接口
    protected DeliverCallback getDeliverCallback() {

        return (s, delivery) -> {

//            try {
//                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 300));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            log.info("{} - 接受到消息 - [{}]: {}", consumerName, queueName, new String(delivery.getBody(), StandardCharsets.UTF_8));
        };
    }

    @SneakyThrows
    private void destroy() {

        channel.close();
        connection.close();
    }

    @Override
    protected void finalize() throws Throwable {

        this.destroy();
        super.finalize();
    }
}
