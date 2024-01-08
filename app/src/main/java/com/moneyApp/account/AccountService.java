package com.moneyApp.account;

import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.vo.AccountSource;
import com.moneyApp.vo.UserSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService
{
    private final AccountRepository accountRepo;
    private final AccountQueryRepository accountQueryRepo;

    AccountService(AccountRepository accountRepo, AccountQueryRepository accountQueryRepo)
    {
        this.accountRepo = accountRepo;
        this.accountQueryRepo = accountQueryRepo;
    }

    public AccountDTO createAccountByUserIdAsDto(AccountDTO toSave, Long userId)
    {
//        check if account with given name already exists for given user
        if (this.accountQueryRepo.existsByNameAndUserId(toSave.getName(), userId))
            throw new IllegalArgumentException("Account with given name already exists!");

        return toDto(this.accountRepo.save(new AccountSnapshot(
                0L
                , toSave.getName()
                , toSave.getDescription()
                , toSave.getActualBalance()
                , new UserSource(userId))));
    }

    AccountDTO getAccountByNameAndUserIdAsDto(String name, Long userId)
    {
        var snap = this.accountQueryRepo.findByNameAndUserId(name, userId)
                .orElseThrow(() -> new IllegalArgumentException("No account found for given name!"));

        return toDto(snap);
    }

    List<AccountDTO> getAccountsByUserIdAsDto(Long userId)
    {
        return this.accountQueryRepo.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    AccountDTO toDto(AccountSnapshot snap)
    {
        return new AccountDTO(
                snap.getName()
                , snap.getDescription()
                , snap.getActualBalance());
    }

    public void updateAccountBalanceByAccountId(double sum, long accountId)
    {
        this.accountRepo.updateActualBalanceById(sum, accountId);
    }

    void updateAccountDataByUserId(final AccountDTO toUpdate, final Long userId)
    {
//        check if account exists in database
        var account = this.accountQueryRepo.findByNameAndUserId(toUpdate.getName(), userId)
                .orElseThrow(() -> new IllegalArgumentException("Account with given name not found!"));

        this.accountRepo.save(new AccountSnapshot(
                account.getId()
                , toUpdate.getName()
                , toUpdate.getDescription()
                , toUpdate.getActualBalance()
                , account.getUser()));
    }

    public AccountSource getAccountSourceByNameAndUserId(final String accountName, final Long userId)
    {
        return new AccountSource(this.accountQueryRepo.findIdByNameAndUserId(accountName, userId)
                .orElseThrow(() -> new IllegalArgumentException("Account with given name not found!")));
    }

    public String getAccountNameById(final long accountId)
    {
        return this.accountQueryRepo.findNameById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with given id not found!"));
    }

    void deleteAccountByNameAndUserId(final String name, final Long userId)
    {
        this.accountRepo.deleteByNameAndUserId(name, userId);
    }
}