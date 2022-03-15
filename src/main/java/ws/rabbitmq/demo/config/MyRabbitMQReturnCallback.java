package ws.rabbitmq.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Rabbit回退消息通知回调接口实现
 * @author WindShadow
 * @version 2021-08-06.
 */

@Slf4j
@Component
public class MyRabbitMQReturnCallback implements RabbitTemplate.ReturnCallback {

    /**
     * 该回调负责消息被回退时的处理
     * @param message 消息
     * @param replyCode 应答码
     * @param replyText 回退原因
     * @param exchange 进行消息回退的交换机
     * @param routingKey 目标路由key
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.warn("被回退的消息: {} ,应答码:：{}, 原因：{}，交换机：{}，路由值：{}",
                msg, replyCode, replyText, exchange, routingKey);
    }
}
