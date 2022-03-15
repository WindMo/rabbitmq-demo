package ws.rabbitmq.demo.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @author WindShadow
 * @version 2021-08-06.
 */
@Service
public class LogsServiceRabbitMQImpl implements LogsService {

    // Rabbit操作模板
    @Autowired
    private RabbitTemplate template;

    @Override
    public void sendInfoLog(String context) {

        this.doSendLog(context,"info");
    }

    @Override
    public void sendWarnLog(String context) {

        this.doSendLog(context,"warn");
    }

    @Override
    public void sendErrorLog(String context) {

        this.doSendLog(context,"error");
    }

    protected void doSendLog(String context, String routingKey) {

        Message message = new Message(context.getBytes(StandardCharsets.UTF_8),null);
        template.send("logExchange",routingKey,message);
    }
}
