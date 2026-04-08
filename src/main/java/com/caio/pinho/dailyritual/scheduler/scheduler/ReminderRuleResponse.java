package com.caio.pinho.dailyritual.scheduler.scheduler;

import java.time.LocalDateTime;

public record ReminderRuleResponse(Long id, Long planId, String title, Integer minutesBefore, LocalDateTime nextSessionAt) {
	static ReminderRuleResponse from(ReminderRule rule) {
		return new ReminderRuleResponse(rule.getId(), rule.getPlanId(), rule.getTitle(), rule.getMinutesBefore(), rule.getNextSessionAt());
	}
}
