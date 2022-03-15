package ws.rabbitmq.demo.component;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author WindShadow
 * @version 2021-08-06.
 */

@Component
public class MessageConsumer {

    // 调用模板
    @Autowired
    public RabbitTemplate template;

    @RabbitListener(queues = "logQueue")// 指定消费的队列
    public void readLogs(Message msg) {


    }
}
