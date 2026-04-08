package com.caio.pinho.dailyritual.scheduler.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ReminderSchedulerServiceTest {

	@Autowired
	private ReminderSchedulerService service;

	@Autowired
	private CapturingReminderPublisher publisher;

	@Autowired
	private ReminderRuleRepository ruleRepository;

	@Autowired
	private ReminderJobRepository jobRepository;

	@BeforeEach
	void setUp() {
		jobRepository.deleteAll();
		ruleRepository.deleteAll();
		publisher.jobs.clear();
	}

	@Test
	void shouldCreateAndEmitDueJobs() {
		service.createRule(5L, new ReminderRuleRequest(9L, "Review", 30, LocalDateTime.parse("2026-04-08T12:20:00")));
		service.tick();
		assertEquals(1, publisher.jobs.size());
	}

	@Test
	void shouldNotEmitDuplicateJobsForSameRuleAndDueAt() {
		service.createRule(5L, new ReminderRuleRequest(9L, "Review", 30, LocalDateTime.parse("2026-04-08T12:20:00")));
		service.tick();
		service.tick();
		assertEquals(1, publisher.jobs.size());
	}

	@TestConfiguration
	static class TestConfig {
		@Bean
		@Primary
		Clock testClock() {
			return Clock.fixed(Instant.parse("2026-04-08T12:00:00Z"), ZoneOffset.UTC);
		}

		@Bean
		@Primary
		CapturingReminderPublisher reminderPublisher() {
			return new CapturingReminderPublisher();
		}
	}

	static class CapturingReminderPublisher implements ReminderPublisher {
		private final List<ReminderJob> jobs = new ArrayList<>();

		@Override
		public void publish(ReminderJob job) {
			jobs.add(job);
		}
	}
}
