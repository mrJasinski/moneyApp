package com.moneyApp.mail.service;

import com.moneyApp.user.dto.UserDTO;
import com.moneyApp.utils.Utils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService
{
    private final JavaMailSender mailSender;

    MailService(final JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }

    public void sendSimpleMessage(String mailTo, String subject, String text)
    {
        var message = new SimpleMailMessage();
        message.setFrom(Utils.APP_MAIL);
        message.setTo(mailTo);
        message.setSubject(subject);
        message.setText(text);

        this.mailSender.send(message);
    }

    public void sendAfterRegistrationMail(UserDTO user)
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