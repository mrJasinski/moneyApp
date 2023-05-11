package com.moneyApp.mail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService
{
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMessage(String mailTo, String subject, String text)
    {
        var message = new SimpleMailMessage();
        message.setFrom("moneyAppMain@gmail.com");
        message.setTo(mailTo);
        message.setSubject(subject);
        message.setText(text);

        this.mailSender.send(message);
    }
}
