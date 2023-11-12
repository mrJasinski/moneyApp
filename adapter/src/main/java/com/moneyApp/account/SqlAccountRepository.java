package com.moneyApp.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface SqlAccountRepository extends AccountRepository, JpaRepository<Account, Long>
{
    @Transactional
    @Modifying
    @Override
    @Query(value = "UPDATE Account a SET a.actualBalance = a.actualBalance + :amount WHERE a.id = :accountId")
    void updateActualBalanceById(Double amount, Long accountId);
}
