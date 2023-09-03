package com.moneyApp.payment.service;

import com.moneyApp.Utils;
import com.moneyApp.payment.Payment;
import com.moneyApp.payment.PaymentDate;
import com.moneyApp.payment.PaymentFrequency;
import com.moneyApp.payment.PaymentStatus;
import com.moneyApp.payment.dto.PaymentDTO;
import com.moneyApp.payment.repository.PaymentDateRepository;
import com.moneyApp.payment.repository.PaymentRepository;
import com.moneyApp.schedule.ScheduleUtils;
import com.moneyApp.schedule.job.SendPaymentReminder;
import com.moneyApp.schedule.service.ScheduleService;
import com.moneyApp.user.service.UserService;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class PaymentService
{
    private final PaymentRepository paymentRepo;
    private final PaymentDateRepository paymentDateRepo;
    private final UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    PaymentService(PaymentRepository paymentRepo, PaymentDateRepository paymentDateRepo, UserService userService)
    {
        this.paymentRepo = paymentRepo;
        this.paymentDateRepo = paymentDateRepo;
        this.userService = userService;
    }

    List<Payment> getActualMonthPaymentsByUserId(Long userId)
    {
        var startDate = LocalDate.of(Utils.ACTUAL_YEAR, Utils.ACTUAL_MONTH, 1);
        var endDate = LocalDate.of(Utils.ACTUAL_YEAR, Utils.ACTUAL_MONTH, Utils.ACTUAL_MONTH_LENGTH);

        return this.paymentRepo.findPaymentsByDatesAndUserId(startDate, endDate, userId);
    }

    public List<PaymentDTO> getActualMonthPaymentsByUserIdAsDto(Long userId)
    {
        return convertPaymentsToSimpleDto(getActualMonthPaymentsByUserId(userId));
    }

    List<Payment> getPaymentsFromNowTillDateByUserId(LocalDate date, Long userId)
    {
        return this.paymentRepo.findPaymentsByDatesAndUserId(LocalDate.now(), date, userId);
    }
    List<PaymentDTO> getPaymentsFromNowTillDateByUserIdAsDto(LocalDate date, Long userId)
    {
        return convertPaymentsToSimpleDto(getPaymentsFromNowTillDateByUserId(date, userId));
    }

    List<Payment> getPaymentsFromNowTillDateWithPreviousUnpaidByUserId(LocalDate date, Long userId)
    {
        return Stream.concat(getPaymentsFromNowTillDateByUserId(date, userId).stream(),
                        getUnpaidPaymentsTillDateByUserId(LocalDate.now(), userId).stream())
                        .collect(Collectors.toList());
    }

    List<Payment> getUnpaidPaymentsTillDateByUserId(LocalDate date, long userId)
    {
        return this.paymentRepo.findNotPaidTillDateByUserId(date, userId);
    }

    public List<PaymentDTO> getPaymentsFromNowTillDateWithPreviousUnpaidByUserIdAsDto(LocalDate date, Long userId)
    {
        return convertPaymentsToSimpleDto(getPaymentsFromNowTillDateWithPreviousUnpaidByUserId(date, userId));
    }

    Double getUnpaidPaymentsSumByList(List<PaymentDTO> payments)
    {
        payments.removeIf(p -> (p.getIsPaid()).equals(PaymentStatus.PAID));

        return payments.stream().mapToDouble(PaymentDTO::getAmount).sum();
    }

    List<PaymentDTO> convertPaymentsToSimpleDto(List<Payment> payments)
    {
        var result = new ArrayList<PaymentDTO>();

        for (Payment p : payments)
            for (PaymentDate d : p.getDates())
                result.add(new PaymentDTO(d.getPaymentDate(), p.getDescription(), p.getAmount(), d.isPaid()));

        return result;
    }

    public Payment createPaymentByUserEmail(PaymentDTO toSave, String userEmail)
    {
        var payment = this.paymentRepo.save(new Payment(toSave.getStartDate(), toSave.getFrequencyType(), toSave.getFrequency(),
                toSave.getDescription(), toSave.getAmount(), this.userService.getUserByEmail(userEmail)));

//        sprawdzenie czy częstotliwości inne niż jednorazowa mają stosowną krotność
//        sprawdzenie krotności dla jednorazowej się pomija ponieważ nie jest brana ona [krotność] pod uwagę
        if (!(payment.getFrequencyType()).equals(PaymentFrequency.ONCE) && payment.getFrequency() <= 1)
            throw new IllegalArgumentException("Wrong payment frequency!");

        generatePaymentDates(payment);

        return payment;
    }

    public void setPaymentDateAsPaid(PaymentDate paymentDate)
    {
        this.paymentDateRepo.setPaymentAsPaidById(paymentDate.getId());

        this.scheduleService.deleteJob(String.valueOf(paymentDate.getHash()), ScheduleUtils.GROUP_PAYMENT_NOTIFICATIONS);
    }

    void generatePaymentDates(Payment payment)
    {
        var result = payment.getDates();

        switch (payment.getFrequencyType())
        {
            case ONCE -> result.add(createPaymentDateByDate(payment.getStartDate(), payment));
            case WEEKLY ->
            {
                for (int i = 0; i < payment.getFrequency(); i++)
                    result.add(createPaymentDateByDate(payment.getStartDate().plusWeeks(i), payment));
            }
            case MONTHLY ->
            {
                for (int i = 0; i < payment.getFrequency(); i++)
                    result.add(createPaymentDateByDate(payment.getStartDate().plusMonths(i), payment));
            }
            case QUARTERLY ->
            {
                for (int i = 0; i < payment.getFrequency(); i++)
                    result.add(createPaymentDateByDate(payment.getStartDate().plusMonths(3L * i), payment));
            }
            case YEARLY ->
            {
                for (int i = 0; i < payment.getFrequency(); i++)
                    result.add(createPaymentDateByDate(payment.getStartDate().plusYears(i), payment));
            }
        }

//        założeniem jest że przypomnienie jest wysyłane dzień przed o 18ej
//        póki co zakodwane na szytwno a następnie o godzinie i ew ilości dni przed podanej przez uzytkownika
        try
        {
            scheduleReminders(result);
        } catch (SchedulerException e)
        {
//            TODO obsługa błędu
            throw new RuntimeException(e);
        }
    }

    void scheduleReminders(Set<PaymentDate> dates) throws SchedulerException
    {
        for (PaymentDate d : dates)
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

    String getReminderCronDateTime(PaymentDate paymentDate)
    {
        var dateTime = computeReminderDate(paymentDate);

        var year = dateTime.getYear();
        var month = dateTime.getMonthValue();
        var day = dateTime.getDayOfMonth();
        var hour = dateTime.getHour();
        var minute = dateTime.getMinute();

        return String.format("0 %d %d %d %d ? %d", minute, hour, day, month, year);
    }

    LocalDateTime computeReminderDate(PaymentDate date)
    {
//        TODO zmiana z 18:00 testowo
        return LocalDateTime.of(date.getPaymentDate().minusDays(1), LocalTime.of(10, 50));
    }

    PaymentDate createPaymentDateByDate(LocalDate date, Payment payment)
    {
        return this.paymentDateRepo.save(new PaymentDate(date, payment));
    }

    public List<PaymentDTO> getPaymentsByUserIdAsDto(Long userId)
    {
        return getPaymentsByUserId(userId)
                .stream()
                .map(Payment::toDto)
                .collect(Collectors.toList());
    }

    public List<Payment> getPaymentsByUserId(Long userId)
    {
        return this.paymentRepo.findByUserId(userId);
    }

    public PaymentDate getPaymentDateById(Long paymentDateId)
    {
        return this.paymentDateRepo.findById(paymentDateId)
                .orElseThrow(() -> new IllegalArgumentException("No payment date with given id!"));
    }

    public PaymentDate getPaymentDateByHash(Integer hash)
    {
        return this.paymentDateRepo.findByHash(hash)
                .orElseThrow(() -> new IllegalArgumentException("No payment date found for given hash!"));
    }
}
