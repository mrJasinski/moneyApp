package com.moneyApp;

import com.moneyApp.account.AccountService;
import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.user.User;
import com.moneyApp.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AccountServiceUnitTest
{
    @Test
    void getAccountByNameAndUserId_shouldReturnAccountWhenFoundInDb()
    {
        var user = new User();
        var account = new Account("ING ROR", "Moje główne konto", 1000D, user);

        var mockAccountRepo = mock(AccountRepository.class);
        given(mockAccountRepo.findByNameAndUserId(anyString(), anyLong())).willReturn(Optional.of(account));

//    system under test
        var toTest = new AccountService(mockAccountRepo, null);

//    when
        var result = toTest.getAccountByNameAndUserId("ING ROR", 1L);

//    then
        assertNotNull(result);
        assertEquals("ING ROR" , result.getName());
    }

    @Test
    void getAccountByNameAndUserId_shouldThrowExceptionWhenFoundInDb()
    {
//    given
        var mockAccountRepo = mock(AccountRepository.class);
        given(mockAccountRepo.findByNameAndUserId(anyString(), anyLong())).willReturn(Optional.empty());

//    system under test
        var toTest = new AccountService(mockAccountRepo, null);

//    when
        var result = catchThrowable(() -> toTest.getAccountByNameAndUserId("ING ROR", 1L));

//    then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No account found");

    }

    @Test
    void createAccountByUserEmail_shouldReturnCreatedAccountWhenAccountWithGivenNameNotFoundInDb()
    {
//    given
        var user = new User();
        var account = new Account("ING ROR", "Moje główne konto", 1000D, user);

        var mockAccountRepo = mock(AccountRepository.class);
        given(mockAccountRepo.existsByNameAndUserId(anyString(), anyLong())).willReturn(false);
        given(mockAccountRepo.save(any())).willReturn(account);

        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserIdByEmail(anyString())).willReturn(1L);

        var dto = new AccountDTO("ING ROR", "", 1000D);

//    system under test
        var toTest = new AccountService(mockAccountRepo, mockUserService);

//    when
        var result = toTest.createAccountByUserEmail(dto, "example@example.com");

//    then
        assertNotNull(result);
        assertEquals("ING ROR" , result.getName());
    }

    @Test
    void createAccountByUserEmail_shouldThrowIllegalArgumentExceptionWhenAccountWithGivenNameFoundInDb()
    {
//    given
        var mockAccountRepo = mock(AccountRepository.class);
        given(mockAccountRepo.existsByNameAndUserId(anyString(), anyLong())).willReturn(true);

        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserIdByEmail(anyString())).willReturn(1L);

        var dto = new AccountDTO("ING ROR", "", 1000D);

//    system under test
        var toTest = new AccountService(mockAccountRepo, mockUserService);

//    when
        var result = catchThrowable(() -> toTest.createAccountByUserEmail(dto, "example@example.com"));

//    then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account with given name already exists");

    }

//    TODO tylko integracyjny?
//    public void updateAccountBalance(long accountId, double sum, CategoryType type)
//    {
//        if (type.equals(CategoryType.EXPENSE))
//            this.accountRepo.updateActualBalanceById(-sum, accountId);
//
//        if (type.equals(CategoryType.INCOME))
//            this.accountRepo.updateActualBalanceById(sum, accountId);
//    }

//    TODO w teście encji?
//    public AccountDTO getAccountByNameAndUserEmailAsDto(String name, String email)
//    {
//        return getAccountByNameAndUserId(name, this.userService.getUserIdByEmail(email)).toDto();
//    }

//    given
//    system under test
//    when
//    then
}