package com.caio.pinho.dailyritual.scheduler.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.caio.pinho.dailyritual.scheduler.model.ReminderJob;
import com.caio.pinho.dailyritual.shared.config.AppMessagingProperties;
import com.caio.pinho.dailyritual.shared.messaging.MessagePublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@ConditionalOnProperty(prefix = "app.messaging", name = "provider", havingValue = "rabbitmq")
public class RabbitMqReminderPublisher implements MessagePublisher<ReminderJob> {

	private final RabbitTemplate rabbitTemplate;
	private final String queueName;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public RabbitMqReminderPublisher(
			RabbitTemplate rabbitTemplate,
			AppMessagingProperties messagingProperties) {
		this.rabbitTemplate = rabbitTemplate;
		this.queueName = messagingProperties.reminderQueue();
	}

	@Override
	public void publish(ReminderJob job) {
		String body;
		try {
			body = objectMapper.writeValueAsString(new ReminderDueMessage(
					job.getId(),
					job.getUserId(),
					job.getPlanId(),
					job.getTitle(),
					job.getDueAt().toString()));
		}
		catch (JsonProcessingException exception) {
			throw new IllegalStateException("Unable to serialize reminder message", exception);
		}
		rabbitTemplate.convertAndSend(queueName, body);
	}

	private record ReminderDueMessage(Long jobId, Long userId, Long planId, String title, String dueAt) {
	}
}
