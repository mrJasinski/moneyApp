package com.moneyApp.payment.repository;

import com.moneyApp.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SqlPaymentRepository extends PaymentRepository, JpaRepository<Payment, Long>
{
    @Override
    @Query(value = "FROM Payment p INNER JOIN PaymentDate d WHERE EXTRACT(MONTH FROM d.paymentDate) = :month AND " +
            "EXTRACT(YEAR FROM d.paymentDate) = :year AND p.user.id = :userId")
    List<Payment> findActualMonthPaymentsByUserId(Integer month, Integer year, Long userId);

//    @Override
//    @Query(value = "SELECT * FROM payments JOIN payment_dates AS d on payments.p_id = d.payment_id WHERE d.payment_date BETWEEN :startDate AND :endDate AND payments.user_id = :userId", nativeQuery = true)
//    List<Payment> findPaymentsByDatesAndUserId(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") Long userId);

//    @Override
//    @Query(value = "SELECT DISTINCT p FROM Payment p LEFT JOIN FETCH PaymentDate d WHERE d.paymentDate BETWEEN :startDate AND :endDate AND p.user.id = :userId")
//    List<Payment> findPaymentsByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);

    @Override
    @Query(value = "SELECT d.payment FROM PaymentDate d WHERE d.paymentDate <= :date AND d.isPaid = FALSE AND d.payment.user.id = :userId")
    List<Payment> findNotPaidTillDateByUserId(LocalDate date, Long userId);

    @Override
    @Query(value = "SELECT d.payment FROM PaymentDate d WHERE d.paymentDate BETWEEN :startDate AND :endDate AND d.payment.user.id = :userId")
    List<Payment> findPaymentsByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
}
