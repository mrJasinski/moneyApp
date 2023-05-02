package com.moneyApp.payee.repository;

import com.moneyApp.payee.Payee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlPayeeRepository extends PayeeRepository, JpaRepository<Payee, Long>
{
}
