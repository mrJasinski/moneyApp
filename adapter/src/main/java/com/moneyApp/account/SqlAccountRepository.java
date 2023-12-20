package com.moneyApp.account;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface SqlAccountRepository extends AccountRepository, JpaRepository<AccountSnapshot, Long>
{
    @Transactional
    @Modifying
    @Override
    @Query(value = "UPDATE AccountSnapshot a " +
                   "SET a.actualBalance = a.actualBalance + :amount " +
                   "WHERE a.id = :accountId")
    void updateActualBalanceById(Double amount, Long accountId);
}
