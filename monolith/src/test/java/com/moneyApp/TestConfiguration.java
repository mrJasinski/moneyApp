package com.moneyApp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
class TestConfiguration
{
    @Bean
    @Primary
    @Profile("!integration")
    DataSource testDataSource()
    {
        var result = new DriverManagerDataSource("jdbc:mysql://localhost:3306/moneyTest", "root", "");
        result.setDriverClassName("com.mysql.cj.jdbc.Driver");

        return result;
    }
}
