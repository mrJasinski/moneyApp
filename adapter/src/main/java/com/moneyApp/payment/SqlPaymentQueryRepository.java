package com.moneyApp.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
interface SqlPaymentQueryRepository extends PaymentQueryRepository, JpaRepository<PaymentSnapshot, Long>
{
    @Override
    @Query(value = "FROM PaymentSnapshot p " +
                   "JOIN PaymentPositionSnapshot pp " +
                   "WHERE p.user.id = :userId AND pp.paymentDate <= :endDate  AND pp.isPaid = FALSE")
    List<PaymentSnapshot> findUnpaidTillDateAndUserId(LocalDate endDate, long userId);
}
