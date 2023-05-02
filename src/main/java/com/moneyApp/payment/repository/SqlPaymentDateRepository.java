package com.moneyApp.payment.repository;

import com.moneyApp.payment.PaymentDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SqlPaymentDateRepository extends PaymentDateRepository, JpaRepository<PaymentDate, Long>
{
    @Override
    @Query("FROM PaymentDate d WHERE d.paymentDate BETWEEN :startDate AND :endDate AND d.payment.user.id = :userId")
    List<PaymentDate> findByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
}
