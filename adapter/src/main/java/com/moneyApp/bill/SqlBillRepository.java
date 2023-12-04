package com.moneyApp.bill;

import com.moneyApp.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
interface SqlBillRepository extends BillRepository, JpaRepository<BillSnapshot, Long>
{
//    TODO
//
//    @Transactional
//    @Modifying
//    @Override
//    @Query(value = "UPDATE Position p SET p.budgetPosition = :position WHERE p.id = :billPositionId AND p.user.id = :userId")
//    void updatePositionIdInDb(Long billPositionId, Long budgetPositionId, Long userId);
}
