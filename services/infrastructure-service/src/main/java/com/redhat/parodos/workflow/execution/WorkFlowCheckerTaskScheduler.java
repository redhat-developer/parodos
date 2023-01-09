package com.redhat.parodos.workflow.execution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Configuration
@Slf4j
public class WorkFlowCheckerTaskScheduler {
    private final TaskScheduler taskScheduler;
    Map<String, ScheduledFuture<?>> tasksMap = new HashMap<>();

    public WorkFlowCheckerTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void schedule(String name, Runnable task, String cronExpression) {
        if (!tasksMap.containsKey(name)) {
            log.info("Scheduling workflow checker task name: {} and cron expression: {}", name, cronExpression);
            ScheduledFuture<?> scheduledTask = taskScheduler.schedule(task, new CronTrigger(cronExpression,
                    TimeZone.getTimeZone(TimeZone.getDefault().getID())));
            tasksMap.put(name, scheduledTask);
        }
        else {
            log.info("Task name: {} is already scheduled!", tasksMap.get(name));
        }
    }
}
