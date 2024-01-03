package com.moneyApp.payment;

import com.moneyApp.payment.dto.PaymentDTO;
import com.moneyApp.payment.dto.PaymentPositionDTO;
import com.moneyApp.schedule.ScheduleUtils;
import com.moneyApp.schedule.job.SendPaymentReminder;
import com.moneyApp.schedule.service.ScheduleService;
import com.moneyApp.vo.UserSource;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class PaymentService
{
    private final PaymentRepository paymentRepo;
    private final PaymentQueryRepository paymentQueryRepo;
    private final ScheduleService scheduleService;

    PaymentService(
            final PaymentRepository paymentRepo
            , final PaymentQueryRepository paymentQueryRepo
            , final ScheduleService scheduleService)
    {
        this.paymentRepo = paymentRepo;
        this.paymentQueryRepo = paymentQueryRepo;
        this.scheduleService = scheduleService;
    }

    public List<PaymentDTO> getUnpaidPaymentsTillDateByUserIdAsDto(final LocalDate endDate, final long userId)
    {

        return this.paymentQueryRepo.findUnpaidTillDateAndUserId(endDate, userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    PaymentDTO createPaymentByUserIdAsDto(PaymentDTO toSave, Long userId)
    {
        var payment = this.paymentRepo.save(new PaymentSnapshot(
                null
                , toSave.getStartDate()
                , toSave.getFrequencyType()
                , toSave.getFrequency()
                , toSave.getDescription()
                , toSave.getAmount()
                , new UserSource(userId)
                , new HashSet<>()));

        payment.addPositions(generatePaymentPositionsAndScheduleReminders(payment));

        payment.getPositions().forEach(this.paymentRepo::save);

        return toDto(payment);
    }

    PaymentDTO toDto(PaymentSnapshot payment)
    {
        return new PaymentDTO(
                payment.getStartDate()
                , payment.getFrequencyType()
                , payment.getFrequency()
                , payment.getDescription()
                , payment.getAmount()
                , payment.getPositions()
                    .stream()
                    .map(p -> new PaymentPositionDTO(
                            p.getPaymentDate()
                            , p.isPaid()
                            , p.getHash()))
                    .collect(Collectors.toList())
        );
    }

    HashSet<PaymentPositionSnapshot> generatePaymentPositionsAndScheduleReminders(PaymentSnapshot payment)
    {
//        check if frequencies other than 1 has proper multiplier
//        check for one time frequency is omitted because it's not taken into account
        if (!(payment.getFrequencyType()).equals(PaymentFrequency.ONCE) && payment.getFrequency() <= 1)
            throw new IllegalArgumentException("Wrong payment frequency!");

        var result = generatePaymentPositions(payment);

//        reminder is sent at 6 pm day before payment date (for still unpaid payments)
//        for now it's hardcoded but in future time and date should be set by user
        try
        {
            scheduleReminders(result);
        }
        catch (SchedulerException e)
        {
//            TODO obsługa błędu
            throw new RuntimeException(e);
        }
//
        return result;
    }

    HashSet<PaymentPositionSnapshot> generatePaymentPositions(PaymentSnapshot payment)
    {
        var result = new HashSet<PaymentPositionSnapshot>();

        var startDate = payment.getStartDate();
        LocalDate date = null;

            for (int i = 0; i < payment.getFrequency(); i++)
            {
                switch (payment.getFrequencyType())
                {
                    case ONCE -> date = startDate;
                    case WEEKLY -> date = startDate.plusWeeks(i);
                    case MONTHLY -> date = startDate.plusMonths(i);
                    case QUARTERLY -> date = startDate.plusMonths(3L * i);
                    case YEARLY -> date = startDate.plusYears(i);
                }

                result.add(new PaymentPositionSnapshot(null, date, payment));
            }

        return result;
    }

    List<PaymentDTO> getPaymentsByUserIdAsDto(final long userId)
    {
        return this.paymentQueryRepo.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    void scheduleReminders(Set<PaymentPositionSnapshot> dates) throws SchedulerException
    {
        for (PaymentPositionSnapshot d : dates)
        {
            var jobDetail = JobBuilder.newJob().ofType(SendPaymentReminder.class)
                    .storeDurably()
                    .withIdentity(String.valueOf(d.getHash()), ScheduleUtils.GROUP_PAYMENT_NOTIFICATIONS)
                    .withDescription("Send email notification for payment")
                    .build();

            jobDetail.getJobDataMap().put("paymentDateId", d.getId());

            var cronDateTime = getReminderCronDateTime(d);

            var trigger = newTrigger().forJob(jobDetail)
            .withIdentity(String.valueOf(d.getHash()), ScheduleUtils.GROUP_PAYMENT_NOTIFICATIONS)
            .withDescription("Trigger description")
            .withSchedule(CronScheduleBuilder.cronSchedule(cronDateTime))
            .build();

            this.scheduleService.scheduleJob(jobDetail, trigger);
        }
    }

    String getReminderCronDateTime(PaymentPositionSnapshot paymentDate)
    {
        var dateTime = computeReminderDate(paymentDate);

        var year = dateTime.getYear();
        var month = dateTime.getMonthValue();
        var day = dateTime.getDayOfMonth();
        var hour = dateTime.getHour();
        var minute = dateTime.getMinute();

        return String.format("0 %d %d %d %d ? %d", minute, hour, day, month, year);
    }

    LocalDateTime computeReminderDate(PaymentPositionSnapshot date)
    {
        return LocalDateTime.of(date.getPaymentDate().minusDays(1), LocalTime.of(18, 0));
    }

    void setPaymentPositionAsPaidByHash(final Integer hash)
    {
        this.paymentRepo.setPaymentPositionAsPaidByHash(hash);
    }
}