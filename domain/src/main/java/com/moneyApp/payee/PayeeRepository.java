package com.moneyApp.payee;

import java.util.List;
import java.util.Optional;

interface PayeeRepository
{
    Optional<PayeeSnapshot> findByNameAndUserId(String name, Long userId);

    PayeeSnapshot save(PayeeSnapshot entity);

    Boolean existsByNameAndUserId(String name, Long userId);

    List<PayeeSnapshot> findByUserId(Long userId);
    List<PayeeSnapshot> findByRoleAndUserId(PayeeRole role, Long userId);
}
