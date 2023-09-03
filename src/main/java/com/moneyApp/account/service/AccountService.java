package com.moneyApp.account.service;

import com.moneyApp.account.Account;
import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.account.repository.AccountRepository;
import com.moneyApp.category.CategoryType;
import com.moneyApp.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService
{
    private final AccountRepository accountRepo;
    private final UserService userService;

    AccountService(AccountRepository accountRepo, UserService userService)
    {
        this.accountRepo = accountRepo;
        this.userService = userService;
    }

    public Account getAccountByNameAndUserId(String name, Long userId)
    {
        return this.accountRepo.findByNameAndUserId(name, userId)
                .orElseThrow(() -> new IllegalArgumentException("No account found for given name!"));
    }

    public Account createAccountByUserEmail(AccountDTO toSave, String email)
    {
//        check if account with given name already exists for given user
        if (this.accountRepo.existsByNameAndUserId(toSave.getName(), this.userService.getUserIdByEmail(email)))
            throw new IllegalArgumentException("Account with given name already exists!");

//        check if account name has whitespaces
        if  (toSave.getName().contains(" "))
            throw new IllegalArgumentException("Account name cannot contain whitespaces!");

        return this.accountRepo.save(new Account(toSave.getName(), toSave.getDescription(), toSave.getActualBalance(),
                    this.userService.getUserByEmail(email)));

    }

    public void updateAccountBalance(long accountId, double sum, CategoryType type)
    {
        if (type.equals(CategoryType.EXPENSE))
            this.accountRepo.updateActualBalanceById(-sum, accountId);

        if (type.equals(CategoryType.INCOME))
            this.accountRepo.updateActualBalanceById(sum, accountId);
    }

    public AccountDTO getAccountByNameAndUserEmailAsDto(String name, String email)
    {
        return new AccountDTO(getAccountByNameAndUserId(name, this.userService.getUserIdByEmail(email)));
    }

    List<Account> getAccountsByUserId(Long userId)
    {
        return this.accountRepo.findByUserId(userId);
    }

    public List<AccountDTO> getDashboardAccountsByUserIdAsDto(Long userId)
    {
        return getAccountsByUserId(userId)
                .stream()
                .map(Account::toSimpleDto)
                .collect(Collectors.toList());
    }

    List<Account> getAccountsByUserMail(String email)
    {
        return this.accountRepo.findByUserId(this.userService.getUserIdByEmail(email));
    }

    public List<AccountDTO> getAccountsByUserMailAsDto(String email)
    {
        return getAccountsByUserMail(email)
                .stream()
                .map(Account::toDto)
                .collect(Collectors.toList());
    }
}
