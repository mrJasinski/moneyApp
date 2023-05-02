package com.moneyApp.payee.repository;

import com.moneyApp.payee.Payee;
import com.moneyApp.payee.PayeeRole;
import com.moneyApp.payee.dto.PayeeDTO;
import com.moneyApp.payment.Payment;

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
