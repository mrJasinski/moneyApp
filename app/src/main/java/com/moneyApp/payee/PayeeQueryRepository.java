package com.moneyApp.payee;

import com.moneyApp.payee.dto.PayeeWithIdAndNameDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface PayeeQueryRepository
{
    Optional<Long> findIdByNameAndUserId(String payeeName, Long userId);

    Optional<String> findNameById(long payeeId);

    Optional<PayeeSnapshot> findByNameAndUserId(String name, Long userId);

    Set<PayeeSnapshot> findPayeesByNamesAndUserId(Set<String> payeeNames, Long userId);

    List<PayeeWithIdAndNameDTO> findPayeesIdsAndNamesByIds(List<Long> payeeIds);
}
