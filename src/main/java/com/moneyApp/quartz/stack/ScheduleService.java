package com.moneyApp.quartz.stack;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

public interface ScheduleService
{
    void startScheduler() throws SchedulerException;

    void standbyScheduler() throws SchedulerException;

    void shutdownScheduler() throws SchedulerException;

    void scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException;

    void deleteJob(String jobName, String jobGroup);
}
