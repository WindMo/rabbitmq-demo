package ws.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ws.rabbitmq.demo.test.RabbitConstant;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author WindShadow
 * @version 2021-07-28.
 */

public abstract class BaseProducer {

    public final String producerName;

    public Connection connection;
    public Channel channel;
    // 该信道中当前发布的消息数量
    protected final AtomicLong count = new AtomicLong(0);

    public BaseProducer(String producerName) {

        this.producerName = producerName;
        this.init();
    }

    @SneakyThrows
    private void init() {

        // 打开连接，创建信道
        connection = RabbitConstant.CONNECTION_FACTORY.newConnection(producerName);
        channel = connection.createChannel();
    }

    protected abstract void doSendMessage(String msg);

    protected abstract void doSendMessage(String msg,String routingKey);

    public void sendMessage(String msg) {

        doSendMessage(msg);
        this.count.addAndGet(1);
    }

    public void sendMessage(String msg,String routingKey) {

        doSendMessage(msg,routingKey);
        this.count.addAndGet(1);
    }

    @SneakyThrows
    protected void destroy() {

        channel.close();
        connection.close();
    }

    @Override
    protected void finalize() throws Throwable {

        this.destroy();
        super.finalize();
    }
}
