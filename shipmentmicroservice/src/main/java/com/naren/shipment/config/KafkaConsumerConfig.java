package com.naren.shipment.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

	@Value("${spring.kafka.consumer.bootstrap-servers:localhost:9093}")
	private String bootstrapServers;

	@Autowired
	private KafkaProperties kafkaProperties;

	@Bean
	public ConsumerFactory<Object, Object> consumerFactory() {
		Map<String, Object> consumerConfigMap = new HashMap<>();
		consumerConfigMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		consumerConfigMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerConfigMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerConfigMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumerConfigMap.put(ConsumerConfig.GROUP_ID_CONFIG, "booking");
		consumerConfigMap.putAll(this.kafkaProperties.buildConsumerProperties());
		return new DefaultKafkaConsumerFactory<>(consumerConfigMap);

	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer containerFactoryConfigurer,
			ObjectProvider<ConsumerFactory<Object, Object>> consumerFactoryObjectProvider) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactoryConfigurer.configure(concurrentKafkaListenerContainerFactory, consumerFactory());
		concurrentKafkaListenerContainerFactory.setConcurrency(3);
		concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
		concurrentKafkaListenerContainerFactory.setCommonErrorHandler(new CommonErrorHandler() {
			@Override
			public boolean handleOne(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer,
					MessageListenerContainer container) {
				return CommonErrorHandler.super.handleOne(thrownException, record, consumer, container);
			}

			@Override
			public void handleOtherException(Exception thrownException, Consumer<?, ?> consumer,
					MessageListenerContainer container, boolean batchListener) {
				CommonErrorHandler.super.handleOtherException(thrownException, consumer, container, batchListener);
			}
		});
		return concurrentKafkaListenerContainerFactory;
	}

}
