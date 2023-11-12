package com.moneyApp.schedule;

import com.moneyApp.schedule.service.ScheduleService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class ScheduleContextListener implements ServletContextListener
{
    private static final Logger logger = LogManager.getLogger(ScheduleContextListener.class);

    private ScheduleService schedulingService(ServletContextEvent sce) {
        var springContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());

        return springContext.getBean(ScheduleService.class);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        try
        {
            this.schedulingService(sce).startScheduler();
        }
        catch (SchedulerException e)
        {
            logger.error("Error while Scheduler is being started", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        try
        {
            this.schedulingService(sce).shutdownScheduler();
        }
        catch (SchedulerException e)
        {
            logger.error("Error while Scheduler is being shutdown", e);
        }
    }
}
