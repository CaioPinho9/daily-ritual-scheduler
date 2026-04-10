package com.caio.pinho.dailyritual.scheduler.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caio.pinho.dailyritual.scheduler.dto.ReminderRuleRequest;
import com.caio.pinho.dailyritual.scheduler.dto.ReminderRuleResponse;
import com.caio.pinho.dailyritual.scheduler.service.ReminderSchedulerService;
import com.caio.pinho.dailyritual.shared.security.AuthenticatedUser;

@RestController
@RequestMapping("/reminder-rules")
public class ReminderRuleController {

	private final ReminderSchedulerService reminderSchedulerService;

	public ReminderRuleController(ReminderSchedulerService reminderSchedulerService) {
		this.reminderSchedulerService = reminderSchedulerService;
	}

	@PostMapping
	public ReminderRuleResponse create(
			@AuthenticationPrincipal AuthenticatedUser user,
			@Valid @RequestBody ReminderRuleRequest request) {
		return reminderSchedulerService.createRule(user.userId(), request);
	}
}
