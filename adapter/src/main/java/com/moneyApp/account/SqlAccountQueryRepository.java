package com.moneyApp.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlAccountQueryRepository extends AccountQueryRepository, JpaRepository<Account, Long>
{

}

