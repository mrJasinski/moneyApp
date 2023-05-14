package com.moneyApp.quartz.bae;

import com.moneyApp.payment.PaymentDate;

import com.moneyApp.mail.service.MailService;
import com.moneyApp.payment.Payment;
import com.moneyApp.payment.service.PaymentService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendPaymentReminder implements Job
{
    @Autowired
    private MailService mailService;

    @Autowired
    private PaymentService paymentService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        var jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        var paymentDateId = (Long)jobDataMap.get("paymentDateId");
        var paymentDate = this.paymentService.getPaymentDateById(paymentDateId);
        var payment = paymentDate.getPayment();

        var userMail = payment.getUser().getEmail();
        var subject = "Przypomnienie o płatności";
        var text = String.format("""
                Dzień dobry %s!
                
                przypominamy o oczekującej płatności "%s" na kwotę: %s.
                Termin upływa %s.
                """, payment.getUser().getName(), payment.getDescription(), payment.getAmount(), paymentDate.getPaymentDate());

        this.mailService.sendSimpleMessage(userMail, subject, text);
    }
}