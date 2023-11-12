package com.moneyApp.account;

import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.category.CategoryType;
import com.moneyApp.user.UserService;
import org.springframework.stereotype.Service;

//  accountService is used for operations other than reading from db - reading ops are in accountQueryService

@Service
public class AccountService
{
    private final AccountRepository accountRepo;
    private final AccountQueryService accountQueryService;
    private final UserService userService;

    AccountService(
            AccountRepository accountRepo
            , AccountQueryService accountQueryService
            , UserService userService)
    {
        this.accountRepo = accountRepo;
        this.accountQueryService = accountQueryService;
        this.userService = userService;
    }

    public Account createAccountByUserId(AccountDTO toSave, Long userId)
    {
//        check if account with given name already exists for given user
        if (this.accountRepo.existsByNameAndUserId(toSave.getName(), userId))
            throw new IllegalArgumentException("Account with given name already exists!");

//        check if account name has whitespaces
        if  (toSave.getName().contains(" "))
            throw new IllegalArgumentException("Account name cannot contain whitespaces!");

        return this.accountRepo.save(new Account(toSave.getName(), toSave.getDescription(), toSave.getActualBalance(),
                    this.userService.getUserById(userId)));
    }

    public void updateAccountBalanceByAccountId(long accountId, double sum, CategoryType type)
    {
//        amounts are stored as positive values, so it's required to change it into negative because expenses are subtracted from account balance
        if (type.equals(CategoryType.EXPENSE))
            sum = -sum;

        this.accountRepo.updateActualBalanceById(sum, accountId);
    }

    void updateAccountDataByUserId(final AccountDTO toUpdate, final Long userId)
    {
//        check if account exists in database
        var account = this.accountQueryService.getAccountByNameAndUserId(toUpdate.getName(), userId);

//        update only if value given is not null and differs from actual name
        if (!toUpdate.getName().isEmpty() && !toUpdate.getName().equals(account.getName()))
            account.setName(toUpdate.getName());

//        update only if value given is not null and differs from actual description
        if (!toUpdate.getName().isEmpty() && !toUpdate.getDescription().equals(account.getDescription()))
            account.setDescription(toUpdate.getDescription());

//        update only if value given is not null and differs from actual value of balance
        if (toUpdate.getActualBalance() != null && !toUpdate.getActualBalance().equals(account.getActualBalance()))
            account.setActualBalance(toUpdate.getActualBalance());

        this.accountRepo.update(account);
    }
}
