package com.moneyApp.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
interface SqlPaymentRepository extends PaymentRepository, JpaRepository<PaymentSnapshot, Long>
{
//    @Override
//    @Query(value = "FROM PaymentPositionSnapshot d WHERE d.paymentDate BETWEEN :startDate AND :endDate AND d.payment.user.id = :userId")
//    List<Payment.Position> findByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
//
//    @Modifying
//    @Transactional
//    @Override
//    @Query(value = "UPDATE PaymentPositionSnapshot d SET d.isPaid = true WHERE d.id = :paymentDateId")
//    void setPaymentAsPaidById(Long paymentDateId);
}
