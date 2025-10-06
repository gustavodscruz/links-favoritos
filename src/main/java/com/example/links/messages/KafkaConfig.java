package com.example.links.messages;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.links.dto.LinkPreviewRequest;

@Configuration
public class KafkaConfig {

    public static final String LINK_PREVIEW_TOPIC = "link-preview-requests";
    public static final String LINK_PREVIEW_GROUP = "links-preview-group";

    @Bean
    public ProducerFactory<String, LinkPreviewRequest> producerFactory() {
        Map<String, Object> props = new HashMap<String, Object>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<String, LinkPreviewRequest>(props);
    }

    @Bean
    public ConsumerFactory<String, LinkPreviewRequest> consumerFactory() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<String, LinkPreviewRequest>(props);
    }

    @Bean
    public KafkaTemplate<String, LinkPreviewRequest> kafkaTemplate() {
        return new KafkaTemplate<String, LinkPreviewRequest>(producerFactory());
    }



}
