package com.caio.pinho.dailyritual.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.caio.pinho.dailyritual.shared.api.CommonApiExceptionHandler;
import com.caio.pinho.dailyritual.shared.api.HealthController;
import com.caio.pinho.dailyritual.shared.security.SharedSecurityConfiguration;

@SpringBootApplication
@EnableScheduling
@Import({ SharedSecurityConfiguration.class, HealthController.class, CommonApiExceptionHandler.class })
public class SchedulerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerServiceApplication.class, args);
	}
}
