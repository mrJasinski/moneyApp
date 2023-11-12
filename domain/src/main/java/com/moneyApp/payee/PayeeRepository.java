package com.moneyApp.payee;

import com.moneyApp.payee.Payee;
import com.moneyApp.payee.PayeeRole;

import java.util.List;
import java.util.Optional;

public interface PayeeRepository
{
    Optional<Payee> findByNameAndUserId(String name, Long userId);

    Payee save(Payee entity);

    Boolean existsByNameAndUserId(String name, Long userId);

    List<Payee> findByUserId(Long userId);
    List<Payee> findByRoleAndUserId(PayeeRole role, Long userId);
}
