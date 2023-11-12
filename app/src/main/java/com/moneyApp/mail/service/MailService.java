package com.moneyApp.mail.service;

import com.moneyApp.Utils;
import com.moneyApp.user.User;
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
        message.setFrom(Utils.APP_MAIL);
        message.setTo(mailTo);
        message.setSubject(subject);
        message.setText(text);

        this.mailSender.send(message);
    }

    public void sendAfterRegistrationMail(User user)
    {
        var mail = user.getEmail();
        var subject = "Dziękujemy za rejestrację w serwisie!";
        var text = String.format("""
                Dzień dobry %s!
                
                Dziękujemy za rejestrację w serwisie MoneyApp! Życzymy przyjemnego korzystania!
                
                Pozdrawiamy
                Zespół MoneyApp
                """, user.getName());

        sendSimpleMessage(mail, subject, text);
    }
}
