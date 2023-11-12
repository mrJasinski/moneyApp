package com.moneyApp.account;

import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.account.dto.AccountSimpleDTO;

import java.util.List;
import java.util.Optional;


interface AccountQueryRepository
{
    Optional<AccountDTO> findDtoByNameAndUserId(String name, Long userId);
    Optional<AccountDTO> findDtoById(Long accountId);
    Optional<Account> findByNameAndUserId(String name, Long userId);

    List<AccountDTO> findDtoByUserId(Long userId);
    List<AccountSimpleDTO> findSimpleDtoByUserId(Long userId);
}
