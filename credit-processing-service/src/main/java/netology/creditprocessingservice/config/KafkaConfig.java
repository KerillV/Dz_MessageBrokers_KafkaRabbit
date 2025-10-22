package netology.creditprocessingservice.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import netology.creditapplicationservice.event.CreditApplicationEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    // для чтения событий

    @Bean
    public ConsumerFactory<String, CreditApplicationEvent> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // подключение к кафке
        // чтобы кафка понимала, что она должна отдавать сообщения именно с этого офсета
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "credit-processing-service"); // набор конзюмеров, которые читают один поток событий
        // если автоофсет сбрасывается, то мы хотим прочитать все сообщения с нуля
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        // без указанных ниже 2 строк скорее всего будет ошибка
//        // * означает что можем использовать любые пакеты (особенность работы в java)
//        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        // указываем ссылку на наш класс
//        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "netology.creditapplicationservice.event.CreditApplicationEvent");

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CreditApplicationEvent> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, CreditApplicationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
