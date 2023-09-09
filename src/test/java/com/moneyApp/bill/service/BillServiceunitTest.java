package com.moneyApp.bill.service;

import com.moneyApp.account.Account;
import com.moneyApp.account.AccountService;
import com.moneyApp.bill.Bill;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.repository.BillRepository;
import com.moneyApp.budget.Budget;
import com.moneyApp.budget.service.BudgetService;
import com.moneyApp.payee.Payee;
import com.moneyApp.payee.service.PayeeService;
import com.moneyApp.bill.BillPosition;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.user.User;
import com.moneyApp.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class BillServiceunitTest
{
//    public Bill createBill(BillDTO toSave)
//    {
//        var user = this.userService.getUserByEmail(toSave.getUserEmail());
//        var payee = this.payeeService.getPayeeByNameAndUserId(toSave.getPayeeName(), user.getId());
//        var account = this.accountService.getAccountByNameAndUserId(toSave.getAccountName(), user.getId());
//        var budget = this.budgetService.getBudgetByDateAndUserId(toSave.getDate(), user.getId());
//        var number = getHighestBillNumberByMonthYearAndUserId(toSave.getDate(), user.getId()) + 1;
//
//        var bill = this.billRepo.save(new Bill(number, toSave.getDate(), payee, account, budget, toSave.getDescription(), user));
//
//        var transactions = this.transactionService.createTransactionsByBillAndUser(toSave.getTransactions(),bill, user);
//
//         var sum = transactions.stream().mapToDouble(Transaction::getAmount).sum();
//
//        this.accountService.updateAccountBalance(account.getId(), sum, transactions.get(0).getCategory().getType());
//
//        return bill;
//    }

//TODO naprawić!
    @Test
    void createBill_shouldReturnCreatedBill()
    {
//    given
        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserByEmail(anyString())).willReturn(new User(1L, "foo@example.com"));
        var mockPayeeService = mock(PayeeService.class);
        given(mockPayeeService.getPayeeByNameAndUserId(anyString(), anyLong())).willReturn(new Payee());
        var mockAccountService = mock(AccountService.class);
        given(mockAccountService.getAccountByNameAndUserId(anyString(), anyLong())).willReturn(new Account());
        var mockBudgetService = mock(BudgetService.class);
        given(mockBudgetService.getBudgetByDateAndUserId(any(), anyLong())).willReturn(new Budget());
        var mockBillRepo = mock(BillRepository.class);
        given(mockBillRepo.save(any())).willReturn(new Bill());
        given(mockBillRepo.findHighestBillNumberByMonthYearAndUserId(anyInt(), anyInt(), anyLong())).willReturn(Optional.of(1L));
        var mockBillPositionService = mock(BillPositionService.class);
        given(mockBillPositionService.createTransactionsByBill(anyList(), any())).willReturn(List.of(
                new BillPosition(0L, 12D, null, null, null, null, null),
                new BillPosition(0L, 12D, null, null, null, null, null)));

//    system under test
        var toTest = new BillServiceImpl(null, null, mockPayeeService, mockUserService, mockAccountService, mockBudgetService,
                 mockBillRepo);
//    when
//        TODO
        var result = toTest.createBillByUserEmail(new BillDTO(LocalDate.now(), "payee", "account",
                "desc", List.of(new BillPositionDTO(), new BillPositionDTO())), "foo@example.com");
//    then
        assertNotNull(result);
    }

    @Test
    void getHighestBillNumberByMonthYearAndUserId_shouldReturnHighestNumberStoredInDbForGivenMonthAndYear()
    {
//    given
        var mockBillRepo = mock(BillRepository.class);
        given(mockBillRepo.findHighestBillNumberByMonthYearAndUserId(anyInt(), anyInt(), anyLong())).willReturn(Optional.of(1L));

//    system under test
        var toTest = new BillServiceImpl(null, null, null, null, null,
                null, mockBillRepo);

//    when
        var result = toTest.getHighestBillNumberByMonthYearAndUserId(LocalDate.now(), 5L);

//    then
        assertNotEquals(0, result);
        assertEquals(1L, result);
    }

    @Test
    void getHighestBillNumberByMonthYearAndUserId_shouldReturnZeroInNoNumberInDbForGivenMonthAndYear()
    {
//    given
        var mockBillRepo = mock(BillRepository.class);
        given(mockBillRepo.findHighestBillNumberByMonthYearAndUserId(anyInt(), anyInt(), anyLong())).willReturn(Optional.empty());

//    system under test
        var toTest = new BillServiceImpl(null, null, null, null, null,
                null, mockBillRepo);

//    when
        var result = toTest.getHighestBillNumberByMonthYearAndUserId(LocalDate.now(), 5L);

//    then
        assertEquals(0, result);
    }

//List<Bill> getBillsByUserId(long userId)
//{
//    return this.billRepo.findByUserId(userId);
//}

    @Test
    void getBillsByUserId_shouldReturnBillsList()
    {
//    given
        var mockBillRepo = mock(BillRepository.class);
        given(mockBillRepo.findByUserId(anyLong())).willReturn(List.of(new Bill(), new Bill()));
//    system under test
        var toTest = new BillServiceImpl(null, null, null, null, null,
                null, mockBillRepo);
//    when
        var result = toTest.getBillsByUserId(5L);

//    then
        assertTrue(result.size() > 0);
        assertEquals(2, result.size());
    }

    @Test
    void getBillsByUserId_shouldReturnEmptyListWhenNoBillsInDb()
    {
        //    given
        var mockBillRepo = mock(BillRepository.class);
        given(mockBillRepo.findByUserId(anyLong())).willReturn(List.of());
//    system under test
        var toTest = new BillServiceImpl(null, null, null, null, null,
                null, mockBillRepo);
//    when
        var result = toTest.getBillsByUserId(5L);

//    then
        assertEquals(0, result.size());
    }
//TODO konwersja do dto w encji?
//    public List<BillDTO> getBillsByUserEmailAsDto(String email)
//    {
//        return getBillsByUserId(this.userService.getUserIdByEmail(email))
//                .stream()
//                .map(Bill::toDto)
//                .collect(Collectors.toList());
//    }


//    given
//    system under test
//    when
//    then
}