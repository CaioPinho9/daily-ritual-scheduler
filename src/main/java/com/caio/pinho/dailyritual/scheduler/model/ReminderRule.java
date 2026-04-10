package com.caio.pinho.dailyritual.scheduler.model;

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
@Table(name = "tb_reminder_rules")
public class ReminderRule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "co_seq_reminder_rule")
	private Long id;

	@Column(name = "co_user", nullable = false)
	private Long userId;

	@Column(name = "co_plan", nullable = false)
	private Long planId;

	@Column(name = "no_title", nullable = false)
	private String title;

	@Column(name = "nu_minutes_before", nullable = false)
	private Integer minutesBefore;

	@Column(name = "dt_next_session_at", nullable = false)
	private LocalDateTime nextSessionAt;
}
