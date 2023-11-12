package com.moneyApp.payee;

import com.moneyApp.payee.Payee;
import com.moneyApp.payee.PayeeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlPayeeRepository extends PayeeRepository, JpaRepository<Payee, Long>
{
}
