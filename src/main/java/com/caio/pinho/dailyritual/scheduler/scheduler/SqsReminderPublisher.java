package com.caio.pinho.dailyritual.scheduler.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class SqsReminderPublisher implements ReminderPublisher {

	private final SqsClient sqsClient;
	private final String queueUrl;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public SqsReminderPublisher(
			SqsClient sqsClient,
			@Value("${app.sqs.reminder-queue-url:http://localhost:4566/000000000000/reminder-due}") String queueUrl) {
		this.sqsClient = sqsClient;
		this.queueUrl = queueUrl;
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
