package com.example.bookstore.common.service;

import org.springframework.scheduling.config.ScheduledTaskRegistrar;

public interface SchedulerService {

	void reloadSchedule(); 
	void setScheduledTaskRegistrar(ScheduledTaskRegistrar registrar);
}
