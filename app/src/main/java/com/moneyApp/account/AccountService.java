package com.moneyApp.account;

import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.user.UserService;
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
    private final UserService userService;

    AccountService(
            AccountRepository accountRepo
            , AccountQueryRepository accountQueryRepo
            , UserService userService)
    {
        this.accountRepo = accountRepo;
        this.accountQueryRepo = accountQueryRepo;
        this.userService = userService;
    }

    public AccountDTO createAccountByUserIdAsDto(AccountDTO toSave, Long userId)
    {
//        check if account with given name already exists for given user
        if (this.accountQueryRepo.existsByNameAndUserId(toSave.getName(), userId))
            throw new IllegalArgumentException("Account with given name already exists!");

//        check if account name has whitespaces
        if  (toSave.getName().contains(" "))
            throw new IllegalArgumentException("Account name cannot contain whitespaces!");

        return toDto(this.accountRepo.save(new AccountSnapshot(
                null
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
                , snap.getActualBalance()
        );
    }

    AccountDTO toDto(Account account)
    {
        var snap = account.getSnapshot();

        return new AccountDTO(
                snap.getName()
                , snap.getDescription()
                , snap.getActualBalance()
        );
    }

    public void updateAccountBalanceByAccountId(double sum, long accountId)
    {
        this.accountRepo.updateActualBalanceById(sum, accountId);
    }

    void updateAccountDataByUserId(final AccountDTO toUpdate, final Long userId)
    {
////        check if account exists in database
//        var account = this.accountQueryService.getAccountByNameAndUserId(toUpdate.getName(), userId);
//
////        update only if value given is not null and differs from actual name
//        if (!toUpdate.getName().isEmpty() && !toUpdate.getName().equals(account.getName()))
//            account.setName(toUpdate.getName());
//
////        update only if value given is not null and differs from actual description
//        if (!toUpdate.getName().isEmpty() && !toUpdate.getDescription().equals(account.getDescription()))
//            account.setDescription(toUpdate.getDescription());
//
////        update only if value given is not null and differs from actual value of balance
//        if (toUpdate.getActualBalance() != null && !toUpdate.getActualBalance().equals(account.getActualBalance()))
//            account.setActualBalance(toUpdate.getActualBalance());

//        this.accountRepo.update(account);
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

//    public Account getAccountByNameAndUserId(String name, Long userId)
//    {
//        return this.accountQueryRepo.findByNameAndUserId(name, userId)
//                .orElseThrow(() -> new IllegalArgumentException("No account found for given name!"));
//    }

//    public List<AccountSimpleDTO> getDashboardAccountsByUserIdAsDto(Long userId)
//    {
//        return this.accountQueryRepo.findSimpleDtoByUserId(userId);
//    }
//
//    public SimpleAccount getSimpleAccountByNameAndUserId(String name, Long userId)
//    {
//        var result = this.accountQueryRepo.findByNameAndUserId(name, userId)
//                .orElseThrow(() -> new IllegalArgumentException("No account found for given name!"));
//
//        return new SimpleAccount(Math.toIntExact(result.getId()), result.getName(), String.valueOf(result.getUser().getId()));
//    }
}
