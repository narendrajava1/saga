package com.naren.order.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.naren.model.order.CustomerOrder;
import com.naren.model.order.VetaEvent;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class KafkaProducerConfig {

	@Value("${spring.kafka.producer.bootstrap-servers: localhost:9093}")
	private String bootstrapServers;

	@Bean
	public ProducerFactory producerFactory() {
		log.info("Kafka broker address {} ", bootstrapServers);
		Map<String, Object> configProperties = new HashMap<>();
		configProperties.put(ProducerConfig.ACKS_CONFIG, "all");
		configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

//		configProperties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, KafkaGeographicPartitioner.class);
//		configProperties.put("partition.booking.for", "BIKE");

		return new DefaultKafkaProducerFactory<>(configProperties);
	}

	@Bean
	public KafkaTemplate<String, VetaEvent<CustomerOrder>> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
