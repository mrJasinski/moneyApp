package com.moneyApp.quartz.stack;

import com.moneyApp.quartz.bae.AutowiringSpringBeanJobFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class ScheduleConfig
{
    @Autowired
    private ApplicationContext applicationContext;

//    @Bean
//    public Scheduler scheduler() throws SchedulerException, IOException
//    {
//        var factory = new StdSchedulerFactory();
////        factory.initialize(new ClassPathResource("properties/quartz.properties").getInputStream());
//
//        var scheduler = factory.getScheduler();
//        scheduler.setJobFactory(springBeanJobFactory());
//
//        return scheduler;
//    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        var jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(this.applicationContext);

        return jobFactory;
    }

//    @Primary
//    @Bean
//    @QuartzDataSource
////    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource quartzDS() {
//        return DataSourceBuilder.create().build();
//    }

    @Bean
    public DataSource getDataSource()
    {
        var dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/moneyTest");
        dataSourceBuilder.username("root");
//        dataSourceBuilder.password("");

        return dataSourceBuilder.build();
    }
}
