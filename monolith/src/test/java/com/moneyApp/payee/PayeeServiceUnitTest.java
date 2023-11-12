package com.moneyApp.payee;

import com.moneyApp.payee.Payee;
import com.moneyApp.payee.PayeeRole;
import com.moneyApp.payee.dto.PayeeDTO;
import com.moneyApp.payee.repository.PayeeRepository;
import com.moneyApp.user.User;
import com.moneyApp.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PayeeServiceUnitTest
{
    @Test
    void getPayeeByNameAndUserId_shouldReturnPayeeWhenPayeeNameFound()
    {
//        given
        var mockPayeeRepo = mock(PayeeRepository.class);
        given(mockPayeeRepo.findByNameAndUserId(anyString(), anyLong())).willReturn(Optional.of(new Payee("Lidl", PayeeRole.PAYEE, new User())));

//        system under test
        var toTest = new PayeeService(mockPayeeRepo, null);

//        when
        var result = toTest.getPayeeByNameAndUserId("Lidl", 2L);

//        then
        assertNotNull(result);
        assertThat(result)
                .isInstanceOf(Payee.class);
    }

    @Test
    void getPayeeByNameAndUserId_shouldThrowExceptionWhenPayeeNameNotFound()
    {
//        given
        var mockPayeeRepo = mock(PayeeRepository.class);
        given(mockPayeeRepo.findByNameAndUserId(anyString(), anyLong())).willReturn(Optional.empty());

//        system under test
        var toTest = new PayeeService(mockPayeeRepo, null);

//        when
        var result = catchThrowable(() -> toTest.getPayeeByNameAndUserId("Biedronka", 5L));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No payee found");

    }

    @Test
    void createPayeeByUserEmail_shouldReturnCreatedPayeeWhenPayeeNameNotExistsForGivenUser()
    {
//        given
        var mockPayeeRepo = mock(PayeeRepository.class);
        given(mockPayeeRepo.existsByNameAndUserId(anyString(), anyLong())).willReturn(false);
        given(mockPayeeRepo.save(any())).willReturn(new Payee("Aldi", PayeeRole.PAYEE, null));

        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserIdByEmail(anyString())).willReturn(anyLong());

//        system under test
        var toTest = new PayeeService(mockPayeeRepo, mockUserService);

//        when
        var result = toTest.createPayeeByUserEmail(new PayeeDTO("Aldi", PayeeRole.PAYEE), "foo@example.com");

//        then
        assertNotNull(result);
        assertThat(result).isInstanceOf(Payee.class);
    }

    @Test
    void createPayeeByUserEmail_shouldThrowExceptionWhenPayeeNameAlreadyExistsForGivenUser()
    {
//        given
        var mockPayeeRepo = mock(PayeeRepository.class);
        given(mockPayeeRepo.existsByNameAndUserId(anyString(), anyLong())).willReturn(true);

        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserIdByEmail(anyString())).willReturn(anyLong());

//        system under test
        var toTest = new PayeeService(mockPayeeRepo, mockUserService);

//        when
        var result = catchThrowable(() -> toTest.createPayeeByUserEmail(new PayeeDTO("Aldi", PayeeRole.PAYEE), "foo@example.com"));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name already exists");
    }

    @Test
    void gePayeesByUserEmail_shouldReturnPayeesList()
    {
//        given
        var mockPayeeRepo = mock(PayeeRepository.class);
        given(mockPayeeRepo.findByUserId(anyLong())).willReturn(List.of(new Payee(), new Payee(), new Payee()));

        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserIdByEmail(anyString())).willReturn(anyLong());

//        system under test
        var toTest = new PayeeService(mockPayeeRepo, mockUserService);

//        when
        var result = toTest.gePayeesByUserEmail("foo@example.com");

//        then
        assertTrue(result.size() > 0);
        assertEquals(3, result.size());
        assertThat(result.get(0)).isInstanceOf(Payee.class);
    }

    @Test
    void gePayeesByUserEmailAsDto_shouldReturnPayeesDtoList()
    {
//        given
        var mockPayeeRepo = mock(PayeeRepository.class);
        given(mockPayeeRepo.findByUserId(anyLong())).willReturn(List.of(new Payee(), new Payee(), new Payee()));

        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserIdByEmail(anyString())).willReturn(anyLong());

//        system under test
        var toTest = new PayeeService(mockPayeeRepo, mockUserService);

//        when
        var result = toTest.gePayeesByUserEmailAsDto("foo@example.com");

//        then
        assertTrue(result.size() > 0);
        assertEquals(3, result.size());
        assertThat(result.get(0)).isInstanceOf(PayeeDTO.class);
    }

    @Test
    void getPayeesByRoleAndUserEmail_shouldReturnPayeesList()
    {
//        given
        var mockPayeeRepo = mock(PayeeRepository.class);
        given(mockPayeeRepo.findByRoleAndUserId(any(), anyLong())).willReturn(List.of(new Payee(), new Payee(), new Payee()));

        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserIdByEmail(anyString())).willReturn(anyLong());

//        system under test
        var toTest = new PayeeService(mockPayeeRepo, mockUserService);

//        when
        var result = toTest.getPayeesByRoleAndUserEmail(PayeeRole.PAYEE, "foo@example.com");

//        then
        assertTrue(result.size() > 0);
        assertEquals(3, result.size());
        assertThat(result.get(0)).isInstanceOf(Payee.class);
    }

    @Test
    void getPayeesByRoleAndUserEmailAsDto_shouldReturnPayeesDtoList()
    {
//        given
        var mockPayeeRepo = mock(PayeeRepository.class);
        given(mockPayeeRepo.findByRoleAndUserId(any(), anyLong())).willReturn(List.of(new Payee(), new Payee(), new Payee()));

        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserIdByEmail(anyString())).willReturn(anyLong());

//        system under test
        var toTest = new PayeeService(mockPayeeRepo, mockUserService);

//        when
        var result = toTest.getPayeesByRoleAndUserEmailAsDto(PayeeRole.PAYEE, "foo@example.com");

//        then
        assertTrue(result.size() > 0);
        assertEquals(3, result.size());
        assertThat(result.get(0)).isInstanceOf(PayeeDTO.class);
    }
}