package com.caio.pinho.dailyritual.scheduler.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caio.pinho.dailyritual.scheduler.dto.ReminderRuleRequest;
import com.caio.pinho.dailyritual.scheduler.dto.ReminderRuleResponse;
import com.caio.pinho.dailyritual.scheduler.model.ReminderJob;
import com.caio.pinho.dailyritual.scheduler.model.ReminderRule;
import com.caio.pinho.dailyritual.scheduler.repository.ReminderJobRepository;
import com.caio.pinho.dailyritual.scheduler.repository.ReminderRuleRepository;
import com.caio.pinho.dailyritual.shared.messaging.MessagePublisher;

@Service
public class ReminderSchedulerService {

	private final ReminderRuleRepository ruleRepository;
	private final ReminderJobRepository jobRepository;
	private final MessagePublisher<ReminderJob> reminderPublisher;
	private final Clock clock;

	public ReminderSchedulerService(
			ReminderRuleRepository ruleRepository,
			ReminderJobRepository jobRepository,
			MessagePublisher<ReminderJob> reminderPublisher,
			Clock clock) {
		this.ruleRepository = ruleRepository;
		this.jobRepository = jobRepository;
		this.reminderPublisher = reminderPublisher;
		this.clock = clock;
	}

	@Transactional
	public ReminderRuleResponse createRule(Long userId, ReminderRuleRequest request) {
		ReminderRule rule = new ReminderRule();
		rule.setUserId(userId);
		rule.setPlanId(request.planId());
		rule.setTitle(request.title());
		rule.setMinutesBefore(request.minutesBefore());
		rule.setNextSessionAt(request.nextSessionAt());
		return ReminderRuleResponse.from(ruleRepository.save(rule));
	}

	@Scheduled(fixedDelay = 60000)
	@Transactional
	public void tick() {
		LocalDateTime now = LocalDateTime.now(clock);
		for (ReminderRule rule : ruleRepository.findAll()) {
			LocalDateTime dueAt = rule.getNextSessionAt().minusMinutes(rule.getMinutesBefore());
			if (!jobRepository.existsByReminderRuleIdAndDueAt(rule.getId(), dueAt)) {
				ReminderJob job = new ReminderJob();
				job.setReminderRuleId(rule.getId());
				job.setUserId(rule.getUserId());
				job.setPlanId(rule.getPlanId());
				job.setTitle(rule.getTitle());
				job.setDueAt(dueAt);
				job.setEmitted(false);
				jobRepository.save(job);
			}
		}

		List<ReminderJob> dueJobs = jobRepository.findAllByEmittedFalseAndDueAtLessThanEqual(now);
		for (ReminderJob job : dueJobs) {
			reminderPublisher.publish(job);
			job.setEmitted(true);
		}
	}
}
