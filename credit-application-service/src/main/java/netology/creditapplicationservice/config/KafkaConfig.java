package netology.creditapplicationservice.config;

import netology.creditapplicationservice.event.CreditApplicationEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    // producerFactory дженерик, указываем тип ключа String и тип значения CreditApplicationEvent (наше событие)
    public ProducerFactory<String, CreditApplicationEvent> producerFactory() {
        // стандартные настройки
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092"); // адрес подключения к серверу -> лучше читать из application properties
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // сериализация ключа
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // сериализация значения
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    // для отправки используется класс KafkaTemplate
    public KafkaTemplate<String, CreditApplicationEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
