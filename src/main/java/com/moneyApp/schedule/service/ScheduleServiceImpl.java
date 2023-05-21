package com.moneyApp.schedule.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService
{
    @Autowired
    private Scheduler scheduler;

    @Override
    public void startScheduler() throws SchedulerException
    {
        if (!this.scheduler.isStarted())
            this.scheduler.start();

    }

    @Override
    public void standbyScheduler() throws SchedulerException
    {
        if (!this.scheduler.isInStandbyMode())
            this.scheduler.standby();
    }

    @Override
    public void shutdownScheduler() throws SchedulerException
    {
        if (!this.scheduler.isShutdown())
            this.scheduler.shutdown();
    }

    @Override
    public void scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException
    {
        this.scheduler.scheduleJob(jobDetail, trigger);
    }

//    TODO test

    @Override
    public void deleteJob(String jobName, String jobGroup)
    {
        try
        {
            this.scheduler.deleteJob(new JobKey(jobName, jobGroup));
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
