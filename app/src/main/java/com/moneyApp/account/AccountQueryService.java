package com.moneyApp.account;

import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.account.dto.AccountSimpleDTO;
import com.moneyApp.vo.SimpleAccount;
import org.springframework.stereotype.Service;

import java.util.List;


//  accountQueryService is used for operations that only reads from db - other are in accountService

@Service
public class AccountQueryService
{
    private final AccountQueryRepository accountQueryRepo;

    AccountQueryService(final AccountQueryRepository accountQueryRepo)
    {
        this.accountQueryRepo = accountQueryRepo;
    }

    public Account getAccountByNameAndUserId(String name, Long userId)
    {
        return this.accountQueryRepo.findByNameAndUserId(name, userId)
                .orElseThrow(() -> new IllegalArgumentException("No account found for given name!"));
    }

    public AccountDTO getAccountByNameAndUserIdAsDto(String name, Long userId)
    {
        return this.accountQueryRepo.findDtoByNameAndUserId(name, userId)
                .orElseThrow(() -> new IllegalArgumentException("No account found for given name!"));
    }

    List<AccountDTO> getAccountsByUserIdAsDto(Long userId)
    {
        return this.accountQueryRepo.findDtoByUserId(userId);
    }

    public List<AccountSimpleDTO> getDashboardAccountsByUserIdAsDto(Long userId)
    {
        return this.accountQueryRepo.findSimpleDtoByUserId(userId);
    }

    public SimpleAccount getSimpleAccountByNameAndUserId(String name, Long userId)
    {
        var result = this.accountQueryRepo.findByNameAndUserId(name, userId)
                .orElseThrow(() -> new IllegalArgumentException("No account found for given name!"));

        return new SimpleAccount(String.valueOf(result.getId()), result.getName(), String.valueOf(result.getUser().getId()));
    }
}
