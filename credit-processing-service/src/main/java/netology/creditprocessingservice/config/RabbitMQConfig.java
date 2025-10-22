package netology.creditprocessingservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue creditDecisionsQueue() {
    return new Queue("credit-decisions",true, false, false);
    // настройки:
    // credit-decisions название очереди
    // durable - true данные должны сохраняться
    // exclusive - false не будет эксклюзивной блокировки
    // autoDelete - false не должна автоматически удаляться
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jasonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
