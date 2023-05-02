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

    public AccountService(AccountRepository accountRepo, UserService userService)
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
//        sprawdzenie czy konto o danej nazwie już istnieje dla danego użytkownika
        if (!this.accountRepo.existsByNameAndUserId(toSave.getName(), this.userService.getUserIdByEmail(email)))
            return this.accountRepo.save(new Account(toSave.getName(), toSave.getDescription(), toSave.getActualBalance(),
                    this.userService.getUserByEmail(email)));
        else
            throw new IllegalArgumentException("Account with given name already exists!");
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
        return getAccountByNameAndUserId(name, this.userService.getUserIdByEmail(email)).toDto();
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
}
