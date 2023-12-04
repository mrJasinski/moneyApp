package com.moneyApp.payee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface SqlPayeeQueryRepository extends PayeeQueryRepository, JpaRepository<PayeeSnapshot, Long>
{
    @Override
    @Query(value = "SELECT p.id FROM PayeeSnapshot p WHERE p.name = :payeeName AND p.user.id = :userId")
    Optional<Long> findIdByNameAndUserId(String payeeName, Long userId);
}
