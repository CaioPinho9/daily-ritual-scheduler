package com.caio.pinho.dailyritual.scheduler.dto;

import java.time.LocalDateTime;

import com.caio.pinho.dailyritual.scheduler.model.ReminderRule;

public record ReminderRuleResponse(Long id, Long planId, String title, Integer minutesBefore, LocalDateTime nextSessionAt) {
	public static ReminderRuleResponse from(ReminderRule rule) {
		return new ReminderRuleResponse(rule.getId(), rule.getPlanId(), rule.getTitle(), rule.getMinutesBefore(), rule.getNextSessionAt());
	}
}
