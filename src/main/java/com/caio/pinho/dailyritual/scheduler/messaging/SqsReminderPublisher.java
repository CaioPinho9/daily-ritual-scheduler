package com.caio.pinho.dailyritual.scheduler.messaging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.caio.pinho.dailyritual.scheduler.model.ReminderJob;
import com.caio.pinho.dailyritual.shared.config.AppSqsProperties;
import com.caio.pinho.dailyritual.shared.messaging.MessagePublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@ConditionalOnProperty(prefix = "app.messaging", name = "provider", havingValue = "sqs", matchIfMissing = true)
public class SqsReminderPublisher implements MessagePublisher<ReminderJob> {

	private final SqsClient sqsClient;
	private final String queueUrl;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public SqsReminderPublisher(
			SqsClient sqsClient,
			AppSqsProperties sqsProperties) {
		this.sqsClient = sqsClient;
		this.queueUrl = sqsProperties.reminderQueueUrl();
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
		sqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(body).build());
	}

	private record ReminderDueMessage(Long jobId, Long userId, Long planId, String title, String dueAt) {
	}
}
