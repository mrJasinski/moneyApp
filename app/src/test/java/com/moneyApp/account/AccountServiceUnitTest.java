package com.moneyApp.account;

import com.moneyApp.account.dto.AccountDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountServiceUnitTest
{
    @Test
    void toDto_shouldReturnDtoCreatedFromSnapshot()
    {
//        given
        var account = new AccountSnapshot(null, "ING_ROR", "My main daily account", 1234.56, null);

//        system under test
        var toTest = new AccountService(null, null);

//        then
        var result = toTest.toDto(account);

//        when
        assertThat(result).isInstanceOf(AccountDTO.class);
        assertEquals("ING_ROR", result.getName());
        assertEquals("My main daily account", result.getDescription());
        assertEquals(1234.56, result.getActualBalance());
    }

}