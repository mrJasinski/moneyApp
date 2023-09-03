package com.moneyApp.user.event.listener;

import com.moneyApp.mail.service.MailService;
import com.moneyApp.user.event.OnRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>
{
    @Autowired
    private MailService mailService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event)
    {
        this.sendRegistrationEmail(event);
    }

    private void sendRegistrationEmail(OnRegistrationCompleteEvent event)
    {
        var recipient = event.getUser().getEmail();
        var subject = "Rejestracja konta";
        var text = String.format("""
                Dzień dobry %s!
                
                Rejestracja Twojego konta w systemie MoneyApp przebiegła pomyślnie. Życzymy miłego korzystania z serwisu.
                
                Zespół MoneyApp
                """, event.getUser().getName());

        this.mailService.sendSimpleMessage(recipient, subject, text);
    }
}
