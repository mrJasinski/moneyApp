package com.moneyApp.payee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlPayeeRepository extends PayeeRepository, JpaRepository<PayeeSnapshot, Long>
{
}
