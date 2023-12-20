package com.moneyApp.account;

import java.util.List;
import java.util.Optional;


interface AccountQueryRepository
{
    Optional<Long> findIdByNameAndUserId(String accountName, Long userId);

    Optional<String> findNameById(long accountId);

    List<AccountSnapshot> findByUserId(Long userId);

    Optional<AccountSnapshot> findByNameAndUserId(String name, Long userId);

    boolean existsByNameAndUserId(String name, Long userId);
}
