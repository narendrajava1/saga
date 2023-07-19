package com.naren.payment.config;

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
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.naren.model.order.CustomerOrder;
import com.naren.model.order.VetaEvent;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConsumerConfig {

	@Value("${spring.kafka.consumer.bootstrap-servers:localhost:9092}")
	private String bootstrapServers;

	@Value("${payments.group-id}")
	private String paymentsGroupId;

	@Value("${orders.group-id}")
	private String ordersGroupId;

	@Autowired
	private KafkaProperties kafkaProperties;

	@Bean(name = "ordersConsumerFactory")
	@Primary
	public ConsumerFactory<String, VetaEvent<CustomerOrder>> ordersConsumerFactory() {
		Map<String, Object> consumerConfigMap = new HashMap<>();
		consumerConfigMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		consumerConfigMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerConfigMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerConfigMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumerConfigMap.put(ConsumerConfig.GROUP_ID_CONFIG, ordersGroupId);
		consumerConfigMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		consumerConfigMap.putAll(this.kafkaProperties.buildConsumerProperties());
		return new DefaultKafkaConsumerFactory<>(consumerConfigMap, new StringDeserializer(),
                new JsonDeserializer<>(VetaEvent.class));

	}

	@Bean(name = "paymentsConsumerFactory")
	public ConsumerFactory<Object, Object> paymentsConsumerFactory() {
		Map<String, Object> consumerConfigMap = new HashMap<>();
		consumerConfigMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		consumerConfigMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerConfigMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerConfigMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumerConfigMap.put(ConsumerConfig.GROUP_ID_CONFIG, paymentsGroupId);
		consumerConfigMap.putAll(this.kafkaProperties.buildConsumerProperties());
		return new DefaultKafkaConsumerFactory<>(consumerConfigMap);

	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, VetaEvent<CustomerOrder>> orderskafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer containerFactoryConfigurer,
			ObjectProvider<ConsumerFactory<String, VetaEvent<CustomerOrder>>> consumerFactoryObjectProvider) {
		ConcurrentKafkaListenerContainerFactory<String, VetaEvent<CustomerOrder>> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
//		containerFactoryConfigurer.configure(concurrentKafkaListenerContainerFactory, ordersConsumerFactory());
		concurrentKafkaListenerContainerFactory.setConcurrency(3);
		concurrentKafkaListenerContainerFactory.setConsumerFactory(ordersConsumerFactory());
//		concurrentKafkaListenerContainerFactory.getContainerProperties().setAckMode(AckMode.MANUAL);
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
		concurrentKafkaListenerContainerFactory.setErrorHandler((exception, data) -> {
	         /* here you can do you custom handling, I am just logging it same as default Error handler does
	        If you just want to log. you need not configure the error handler here. The default handler does it for you.
	         Generally, you will persist the failed records to DB for tracking the failed records.  */
	         log.error("Error in process with Exception {} and the record is {}", exception, data);
	        });
		return concurrentKafkaListenerContainerFactory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<Object, Object> paymentsKafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer containerFactoryConfigurer,
			ObjectProvider<ConsumerFactory<Object, Object>> consumerFactoryObjectProvider) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactoryConfigurer.configure(concurrentKafkaListenerContainerFactory, paymentsConsumerFactory());
		concurrentKafkaListenerContainerFactory.setConcurrency(3);
		concurrentKafkaListenerContainerFactory.setConsumerFactory(paymentsConsumerFactory());
		concurrentKafkaListenerContainerFactory.getContainerProperties().setAckMode(AckMode.MANUAL);
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
