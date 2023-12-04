package com.moneyApp.account;

import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.account.dto.AccountSimpleDTO;
import com.moneyApp.vo.AccountSource;

import java.util.List;
import java.util.Optional;


interface AccountQueryRepository
{
    Optional<Long> findIdByNameAndUserId(String accountName, Long userId);

    Optional<String> findNameById(long accountId);

    List<AccountSnapshot> findByUserId(Long userId);

    Optional<AccountSnapshot> findByNameAndUserId(String name, Long userId);

    boolean existsByNameAndUserId(String name, Long userId);


//    Optional<AccountDTO> findDtoByNameAndUserId(String name, Long userId);
//    Optional<AccountDTO> findDtoById(Long accountId);
//    Optional<Account> findByNameAndUserId(String name, Long userId);
//
//    List<AccountDTO> findDtoByUserId(Long userId);
//    List<AccountSimpleDTO> findSimpleDtoByUserId(Long userId);
}
