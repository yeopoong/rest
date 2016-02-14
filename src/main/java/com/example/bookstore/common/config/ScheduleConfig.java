
package com.example.bookstore.common.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.example.bookstore.common.service.SchedulerService;

@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {

	@Resource
	SchedulerService schedulerService;

	@Override
	public void configureTasks(ScheduledTaskRegistrar registrar) {
		schedulerService.setScheduledTaskRegistrar(registrar);
		schedulerService.reloadSchedule();
		registrar.setScheduler(taskExecutor());
	}
	
	private Executor taskExecutor() {
		return Executors.newScheduledThreadPool(10);
	}
}
