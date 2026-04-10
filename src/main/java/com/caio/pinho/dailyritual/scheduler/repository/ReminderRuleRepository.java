package com.caio.pinho.dailyritual.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caio.pinho.dailyritual.scheduler.model.ReminderRule;

public interface ReminderRuleRepository extends JpaRepository<ReminderRule, Long> {
}
