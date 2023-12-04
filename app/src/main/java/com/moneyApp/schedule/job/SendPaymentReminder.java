package com.moneyApp.schedule.job;

import com.moneyApp.mail.service.MailService;
import com.moneyApp.payment.PaymentService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class SendPaymentReminder implements Job
{
    private final MailService mailService;
    private final PaymentService paymentService;

    SendPaymentReminder(final MailService mailService, final PaymentService paymentService)
    {
        this.mailService = mailService;
        this.paymentService = paymentService;
    }

//    TODO tymczasowo aby implemetacja interfejsu nie rzucała błędu

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
    }

//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
//    {
//        var jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
//
//        var paymentDateId = (Long)jobDataMap.get("paymentDateId");
//        var paymentDate = this.paymentService.getPaymentDateById(paymentDateId);
//        var payment = paymentDate.getPayment();
//
//        var userMail = payment.getUser().getEmail();
//        var subject = "Przypomnienie o płatności";
//        var text = String.format("""
//                Dzień dobry %s!
//
//                przypominamy o oczekującej płatności "%s" na kwotę: %s.
//                Termin upływa %s.
//                """, payment.getUser().getName(), payment.getDescription(), payment.getAmount(), paymentDate.getPaymentDate());
//
//        this.mailService.sendSimpleMessage(userMail, subject, text);
//    }
}
