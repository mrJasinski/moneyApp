package com.moneyApp.payment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository
{
    Payment save(Payment entity);
//
//    List<Payment.Position> findByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
//
//    Optional<Payment.Position> findPaymentDateById(Long paymentDateId);
//    Optional<Payment.Position> findByHash(Integer hash);
//
//    void setPaymentAsPaidById(Long paymentDateId);
}
