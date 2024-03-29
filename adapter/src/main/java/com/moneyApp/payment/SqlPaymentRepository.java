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

    @Modifying
    @Transactional
    @Override
    @Query(value = "UPDATE PaymentPositionSnapshot p " +
                   "SET p.isPaid = TRUE " +
                   "WHERE p.hash = :hash")
    void setPaymentPositionAsPaidByHash(Integer hash);
}
