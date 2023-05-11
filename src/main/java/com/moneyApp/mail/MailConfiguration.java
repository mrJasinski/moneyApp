package com.moneyApp.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfiguration
{
    @Bean
    JavaMailSender getJavaMailSender()
    {
        var mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("moneyAppMain@gmail.com");
        mailSender.setPassword("dnohahumurabufxg");

        var properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public SimpleMailMessage templateSimpleMessage()
    {
        var message = new SimpleMailMessage();
        message.setText(
                "This is the test email template!");
        return message;
    }
}
