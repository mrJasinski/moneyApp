package com.moneyApp.payment.service;

import com.moneyApp.Utils;
import com.moneyApp.payment.Payment;
import com.moneyApp.payment.PaymentDate;
import com.moneyApp.payment.PaymentFrequency;
import com.moneyApp.payment.PaymentStatus;
import com.moneyApp.payment.dto.PaymentDTO;
import com.moneyApp.payment.repository.PaymentDateRepository;
import com.moneyApp.payment.repository.PaymentRepository;
import com.moneyApp.user.service.UserService;
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
    private final PaymentDateRepository paymentDateRepo;
    private final UserService userService;

    public PaymentService(PaymentRepository paymentRepo, PaymentDateRepository paymentDateRepo, UserService userService)
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
//        sprawdzenie krotności dla jednorazowej się pomija ponieważ nie jest brana ona pod uwagę
        if (!(payment.getFrequencyType()).equals(PaymentFrequency.ONCE) && payment.getFrequency() <= 1)
            throw new IllegalArgumentException("Wrong payment frequency!");
        else
            generatePaymentDates(payment);

        return payment;
    }

    public void setPaymentAsPaid()
    {
//        TODO określenie warunku na odnalezienie płatności
//        this.paymentDateRepo.setPaymentAsPaid();
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
}
