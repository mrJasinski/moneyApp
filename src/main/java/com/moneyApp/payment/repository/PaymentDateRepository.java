package com.moneyApp.payment.repository;

import com.moneyApp.payment.PaymentDate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentDateRepository
{
    PaymentDate save(PaymentDate entity);

    List<PaymentDate> findByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);

    Optional<PaymentDate> findById(Long paymentDateId);

    void setPaymentAsPaidById(Long paymentDateId);
}
