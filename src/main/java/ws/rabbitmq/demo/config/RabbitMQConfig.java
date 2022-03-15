package ws.rabbitmq.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WindShadow
 * @version 2021-08-05.
 */

@Configuration
public class RabbitMQConfig {


    // 直接交换机
    @Bean("logExchange")
    public DirectExchange directExchange0() {

        return new DirectExchange("logExchange",true,false);
    }
//
//    // 扇出交换机
//    @Bean("fanoutExchange")
//    public FanoutExchange fanoutExchange0() {
//
//        return new FanoutExchange("FanoutE",true,false);
//    }
//
//    // 主题交换机
//    @Bean("topicExchange")
//    public TopicExchange topicExchange0() {
//
//        return new TopicExchange("TopicE",true,false);
//    }

    // 声明队列
    @Bean("infoLogQueue")
    public Queue queue0() {

        return QueueBuilder.durable("infoLogQueue").build();

//        return QueueBuilder.durable("direct_q1").deadLetterExchange("xxx").deadLetterRoutingKey("rk").build();// 指定死信交换机和RoutingKey
    }
    // 绑定队列到交换机
    @Bean("logExchangeToLogQueue")
    public Binding binding0(@Qualifier("logExchange") DirectExchange exchange, @Qualifier("infoLogQueue") Queue queue) {

        return BindingBuilder.bind(queue).to(exchange).with("info");
    }
}
