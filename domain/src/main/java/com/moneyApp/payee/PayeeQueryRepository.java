package com.moneyApp.payee;

import java.util.Optional;

interface PayeeQueryRepository
{
    Optional<Long> findIdByNameAndUserId(String payeeName, Long userId);

    Optional<String> findNameById(long payeeId);

    Optional<PayeeSnapshot> findByNameAndUserId(String name, Long userId);
}
