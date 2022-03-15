package ws.rabbitmq.demo.test;

import com.rabbitmq.client.ConnectionFactory;

/**
 * @author WindShadow
 * @version 2021-07-27.
 */

public class RabbitConstant {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "root123456";
    private static final String HOST = "192.168.100.100";
    private static final int PORT = 5672;
    private static final String VIRTUAL_HOST = "/";

    /** RabbitMQ内置的缺省的交换机 */
    public static final String DEFAULT_EXCHANGE = "";
    public static final ConnectionFactory CONNECTION_FACTORY = new ConnectionFactory();

    static {

        CONNECTION_FACTORY.setUsername(USERNAME);
        CONNECTION_FACTORY.setPassword(PASSWORD);
        CONNECTION_FACTORY.setHost(HOST);
        CONNECTION_FACTORY.setPort(PORT);
        CONNECTION_FACTORY.setVirtualHost(VIRTUAL_HOST);

    }
}
