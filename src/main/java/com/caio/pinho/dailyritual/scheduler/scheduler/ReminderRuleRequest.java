package com.caio.pinho.dailyritual.scheduler.scheduler;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReminderRuleRequest(
		@NotNull Long planId,
		@NotBlank String title,
		@NotNull @Min(1) Integer minutesBefore,
		@NotNull LocalDateTime nextSessionAt
) {
}
