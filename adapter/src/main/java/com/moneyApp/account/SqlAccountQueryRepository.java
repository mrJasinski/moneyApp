package com.moneyApp.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
interface SqlAccountQueryRepository extends AccountQueryRepository, JpaRepository<AccountSnapshot, Long>
{
    @Override
    @Query(value = "SELECT a.id " +
                   "FROM AccountSnapshot a " +
                   "WHERE a.name = :accountName AND a.user.id = :userId")
    Optional<Long> findIdByNameAndUserId(String accountName, Long userId);

    @Override
    @Query(value = "SELECT a.name " +
                   "FROM AccountSnapshot a " +
                   "WHERE a.id = :accountId")
    Optional<String> findNameById(long accountId);

    @Override
    @Query(value = "SELECT a.id " +
                   "FROM AccountSnapshot a")
    Set<Long> findAllIds();

    @Override
    @Query(value = "SELECT a.actualBalance " +
                   "FROM AccountSnapshot a " +
                   "WHERE a.id = :accountId")
    Optional<Double> findBalanceByAccountId(long accountId);
}

