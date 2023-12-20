package com.moneyApp.payment;

import com.moneyApp.payment.dto.PaymentDTO;

import java.time.LocalDate;
import java.util.List;

interface PaymentQueryRepository
{
    List<PaymentSnapshot> findUnpaidTillDateAndUserId(LocalDate endDate, long userId);

    List<PaymentSnapshot> findByUserId(long userId);
//    List<Payment> findActualMonthPaymentsByUserId(Integer month, Integer year, Long userId);
//    List<Payment> findByUserId(Long userId);
//    List<Payment> findPaymentsByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
    //    List<Payment> findPaymentsByDatesAndUserId(String startDate, String endDate, Long userId);
//    List<Payment> findNotPaidTillDateByUserId(LocalDate date, Long userId);
}
