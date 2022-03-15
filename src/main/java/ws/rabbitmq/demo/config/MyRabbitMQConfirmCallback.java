package ws.rabbitmq.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Rabbit发布通知回调接口实现
 * @author WindShadow
 * @version 2021-08-06.
 */

@Slf4j
@Component
public class MyRabbitMQConfirmCallback implements RabbitTemplate.ConfirmCallback {

    /**
     * 该回调只管发送者发送到交换机时的消息是否失败来进行回调，至于消息是否发送到队列，此处不可知，也不会触发回调
     * @param correlationData 消息数据
     * @param ack 是否已经应答
     * @param cause 发布失败原因（对于nack），当可用时（ack），为null
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        if (correlationData == null) {

            log.warn("发布通知回调接口【发布失败】，发布信息时设置的数据为空");
        }else {
            String msgId = correlationData.getId();
            if (ack) {
                log.info("发布通知回调接口【发布成功】：id: {}",msgId);
            }else {

                log.warn("发布通知回调接口【发布失败】：id: {} cause: {}",msgId,cause);
            }
        }
    }
}
