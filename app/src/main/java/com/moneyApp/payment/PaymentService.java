package com.moneyApp.payment;

import com.moneyApp.utils.Utils;
import com.moneyApp.payment.dto.PaymentDTO;
import com.moneyApp.schedule.service.ScheduleService;
import com.moneyApp.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentService
{
    private final PaymentRepository paymentRepo;
    private final PaymentQueryRepository paymentQueryRepo;
    private final UserService userService;
    private final ScheduleService scheduleService;

    PaymentService(
            final PaymentRepository paymentRepo
            , final PaymentQueryRepository paymentQueryRepo
            , final UserService userService
            , final ScheduleService scheduleService)
    {
        this.paymentRepo = paymentRepo;
        this.paymentQueryRepo = paymentQueryRepo;
        this.userService = userService;
        this.scheduleService = scheduleService;
    }

//    List<Payment> getActualMonthPaymentsByUserId(Long userId)
//    {
//        var startDate = LocalDate.of(Utils.ACTUAL_YEAR, Utils.ACTUAL_MONTH, 1);
//        var endDate = LocalDate.of(Utils.ACTUAL_YEAR, Utils.ACTUAL_MONTH, Utils.ACTUAL_MONTH_LENGTH);
//
//        return this.paymentQueryRepo.findPaymentsByDatesAndUserId(startDate, endDate, userId);
//    }

//    public List<PaymentDTO> getActualMonthPaymentsByUserIdAsDto(Long userId)
//    {
//        return convertPaymentsToSimpleDto(getActualMonthPaymentsByUserId(userId));
//    }

//    List<Payment> getPaymentsFromNowTillDateByUserId(LocalDate date, Long userId)
//    {
//        return this.paymentQueryRepo.findPaymentsByDatesAndUserId(LocalDate.now(), date, userId);
//    }
//    List<PaymentDTO> getPaymentsFromNowTillDateByUserIdAsDto(LocalDate date, Long userId)
//    {
//        return convertPaymentsToSimpleDto(getPaymentsFromNowTillDateByUserId(date, userId));
//    }
//
//    List<Payment> getPaymentsFromNowTillDateWithPreviousUnpaidByUserId(LocalDate date, Long userId)
//    {
//        return Stream.concat(getPaymentsFromNowTillDateByUserId(date, userId).stream(),
//                        getUnpaidPaymentsTillDateByUserId(LocalDate.now(), userId).stream())
//                        .collect(Collectors.toList());
//    }
//
//    List<Payment> getUnpaidPaymentsTillDateByUserId(LocalDate date, long userId)
//    {
//        return this.paymentQueryRepo.findNotPaidTillDateByUserId(date, userId);
//    }

//    public List<PaymentDTO> getPaymentsFromNowTillDateWithPreviousUnpaidByUserIdAsDto(LocalDate date, Long userId)
//    {
//        return convertPaymentsToSimpleDto(getPaymentsFromNowTillDateWithPreviousUnpaidByUserId(date, userId));
//    }

    Double getUnpaidPaymentsSumByList(List<PaymentDTO> payments)
    {
        payments.removeIf(p -> (p.getIsPaid()).equals(PaymentStatus.PAID));

        return payments.stream().mapToDouble(PaymentDTO::getAmount).sum();
    }

//    List<PaymentDTO> convertPaymentsToSimpleDto(List<Payment> payments)
//    {
//        var result = new ArrayList<PaymentDTO>();
//
//        for (Payment p : payments)
//            for (Payment.Position d : p.getPositions())
//                result.add(new PaymentDTO(d.getPaymentDate(), p.getDescription(), p.getAmount(), d.isPaid()));
//
//        return result;
//    }
//
//    public Payment createPaymentByUserEmail(PaymentDTO toSave, String userEmail)
//    {
//        var payment = this.paymentRepo.save(new Payment(toSave.getStartDate(), toSave.getFrequencyType(), toSave.getFrequency(),
//                toSave.getDescription(), toSave.getAmount(), this.userService.getUserByEmail(userEmail)));
//
////        check if frequencies other than 1 has proper multiplier
////        check for one time frequency is omitted because it's not taken into account
//
//        if (!(payment.getFrequencyType()).equals(PaymentFrequency.ONCE) && payment.getFrequency() <= 1)
//            throw new IllegalArgumentException("Wrong payment frequency!");
//
//        generatePaymentDates(payment);
//
//        return payment;
//    }
//
//    public void setPaymentDateAsPaid(PaymentDate paymentDate)
//    {
//        this.paymentDateRepo.setPaymentAsPaidById(paymentDate.getId());
//
//        this.scheduleService.deleteJob(String.valueOf(paymentDate.getHash()), ScheduleUtils.GROUP_PAYMENT_NOTIFICATIONS);
//    }
//
//    void generatePaymentDates(Payment payment)
//    {
//        var result = payment.getDates();
//
//        switch (payment.getFrequencyType())
//        {
//            case ONCE -> result.add(createPaymentDateByDate(payment.getStartDate(), payment));
//            case WEEKLY ->
//            {
//                for (int i = 0; i < payment.getFrequency(); i++)
//                    result.add(createPaymentDateByDate(payment.getStartDate().plusWeeks(i), payment));
//            }
//            case MONTHLY ->
//            {
//                for (int i = 0; i < payment.getFrequency(); i++)
//                    result.add(createPaymentDateByDate(payment.getStartDate().plusMonths(i), payment));
//            }
//            case QUARTERLY ->
//            {
//                for (int i = 0; i < payment.getFrequency(); i++)
//                    result.add(createPaymentDateByDate(payment.getStartDate().plusMonths(3L * i), payment));
//            }
//            case YEARLY ->
//            {
//                for (int i = 0; i < payment.getFrequency(); i++)
//                    result.add(createPaymentDateByDate(payment.getStartDate().plusYears(i), payment));
//            }
//        }
//
////        reminder is sent at 6 pm day before payment date (for unpaid payments)
////        for now it's hardcoded but in future time and date should be set by user
//        try
//        {
//            scheduleReminders(result);
//        } catch (SchedulerException e)
//        {
////            TODO obsługa błędu
//            throw new RuntimeException(e);
//        }
//    }
//
//    void scheduleReminders(Set<PaymentDate> dates) throws SchedulerException
//    {
//        for (PaymentDate d : dates)
//        {
//            var jobDetail = JobBuilder.newJob().ofType(SendPaymentReminder.class)
//                    .storeDurably()
//                    .withIdentity(String.valueOf(d.getHash()), ScheduleUtils.GROUP_PAYMENT_NOTIFICATIONS)
//                    .withDescription("Send email notification for payment")
//                    .build();
//
//            jobDetail.getJobDataMap().put("paymentDateId", d.getId());
//
//            var cronDateTime = getReminderCronDateTime(d);
//
//            var trigger = newTrigger().forJob(jobDetail)
//            .withIdentity(String.valueOf(d.getHash()), ScheduleUtils.GROUP_PAYMENT_NOTIFICATIONS)
//            .withDescription("Trigger description")
//            .withSchedule(CronScheduleBuilder.cronSchedule(cronDateTime))
//            .build();
//
//            this.scheduleService.scheduleJob(jobDetail, trigger);
//        }
//    }
//
//    String getReminderCronDateTime(PaymentDate paymentDate)
//    {
//        var dateTime = computeReminderDate(paymentDate);
//
//        var year = dateTime.getYear();
//        var month = dateTime.getMonthValue();
//        var day = dateTime.getDayOfMonth();
//        var hour = dateTime.getHour();
//        var minute = dateTime.getMinute();
//
//        return String.format("0 %d %d %d %d ? %d", minute, hour, day, month, year);
//    }
//
//    LocalDateTime computeReminderDate(PaymentDate date)
//    {
//        return LocalDateTime.of(date.getPaymentDate().minusDays(1), LocalTime.of(18, 0));
//    }
//
//    PaymentDate createPaymentDateByDate(LocalDate date, Payment payment)
//    {
//        return this.paymentDateRepo.save(new PaymentDate(date, payment));
//    }
//
////    public List<PaymentDTO> getPaymentsByUserIdAsDto(Long userId)
////    {
////        return getPaymentsByUserId(userId)
////                .stream()
////                .map(Payment::toDto)
////                .collect(Collectors.toList());
////    }
//
//    public List<Payment> getPaymentsByUserId(Long userId)
//    {
//        return this.paymentQueryRepo.findByUserId(userId);
//    }
//
//    public PaymentDate getPaymentDateById(Long paymentDateId)
//    {
//        return this.paymentDateRepo.findById(paymentDateId)
//                .orElseThrow(() -> new IllegalArgumentException("Payment date with given id not found!"));
//    }
//
//    public PaymentDate getPaymentDateByHash(Integer hash)
//    {
//        return this.paymentDateRepo.findByHash(hash)
//                .orElseThrow(() -> new IllegalArgumentException("Payment date for given hash not found!"));
//    }
}