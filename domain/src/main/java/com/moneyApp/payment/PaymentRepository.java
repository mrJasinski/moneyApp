package com.moneyApp.payment;

interface PaymentRepository
{
    PaymentSnapshot save(PaymentSnapshot entity);
    PaymentPositionSnapshot save(PaymentPositionSnapshot entity);
//
//    List<Payment.Position> findByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
//
//    Optional<Payment.Position> findPaymentDateById(Long paymentDateId);
//    Optional<Payment.Position> findByHash(Integer hash);
//
//    void setPaymentAsPaidById(Long paymentDateId);
}
