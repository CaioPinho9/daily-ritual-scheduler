package com.caio.pinho.dailyritual.scheduler.scheduler;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_reminder_jobs")
public class ReminderJob {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "co_seq_reminder_job")
	private Long id;

	@Column(name = "co_reminder_rule", nullable = false)
	private Long reminderRuleId;

	@Column(name = "co_user", nullable = false)
	private Long userId;

	@Column(name = "co_plan", nullable = false)
	private Long planId;

	@Column(name = "no_title", nullable = false)
	private String title;

	@Column(name = "dt_due_at", nullable = false)
	private LocalDateTime dueAt;

	@Column(name = "st_emitted", nullable = false)
	private Boolean emitted;
}
