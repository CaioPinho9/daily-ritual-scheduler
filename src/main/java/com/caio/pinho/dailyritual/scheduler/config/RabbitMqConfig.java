package com.caio.pinho.dailyritual.scheduler.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.caio.pinho.dailyritual.shared.config.AppMessagingProperties;

@Configuration
@ConditionalOnProperty(prefix = "app.messaging", name = "provider", havingValue = "rabbitmq")
public class RabbitMqConfig {

	@Bean
	Queue reminderQueue(AppMessagingProperties messagingProperties) {
		return QueueBuilder.durable(messagingProperties.reminderQueue()).build();
	}
}
