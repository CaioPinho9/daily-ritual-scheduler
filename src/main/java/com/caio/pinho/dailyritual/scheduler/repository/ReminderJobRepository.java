package com.caio.pinho.dailyritual.scheduler.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caio.pinho.dailyritual.scheduler.model.ReminderJob;

public interface ReminderJobRepository extends JpaRepository<ReminderJob, Long> {
	List<ReminderJob> findAllByEmittedFalseAndDueAtLessThanEqual(LocalDateTime dueAt);
	boolean existsByReminderRuleIdAndDueAt(Long reminderRuleId, LocalDateTime dueAt);
}
