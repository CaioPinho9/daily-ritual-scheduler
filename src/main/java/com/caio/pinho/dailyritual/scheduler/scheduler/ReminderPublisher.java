package com.caio.pinho.dailyritual.scheduler.scheduler;

public interface ReminderPublisher {
	void publish(ReminderJob job);
}
