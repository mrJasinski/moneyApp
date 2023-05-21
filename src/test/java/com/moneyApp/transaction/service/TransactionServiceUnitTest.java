package com.moneyApp.transaction.service;

import com.moneyApp.bill.Bill;
import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.category.Category;
import com.moneyApp.category.service.CategoryService;
import com.moneyApp.payee.Payee;
import com.moneyApp.payee.service.PayeeService;
import com.moneyApp.transaction.Transaction;
import com.moneyApp.transaction.dto.TransactionDTO;
import com.moneyApp.transaction.repository.TransactionRepository;
import com.moneyApp.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TransactionServiceUnitTest
{
    @Test
    void getTransactionsByMonthYearAndUserId_shouldReturnTransactionsList()
    {
//        given
        var mockTransactionRepo = mock(TransactionRepository.class);
        given(mockTransactionRepo.findTransactionsBetweenDatesAndUserId(any(), any(), anyLong())).willReturn(List.of(
                new Transaction(), new Transaction(), new Transaction()));

        var date = LocalDate.now();

//        system under test
        var toTest = new TransactionService(mockTransactionRepo, null, null);

//        when
        var result = toTest.getTransactionsByMonthYearAndUserId(date, 5L);

//        then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertThat(result.get(0)).isInstanceOf(Transaction.class);
    }

    @Test
    void createTransactionsByBill_shouldReturnCreatedTransactionsList()
    {
//        given
        var mockCategoryService = mock(CategoryService.class);
        given(mockCategoryService.getCategoryByNameAndUserId(anyString(), anyLong())).willReturn(new Category());
        given(mockCategoryService.getCategoryByNameAndUserId(anyString(), anyLong())).willReturn(new Category());

        var mockPayeeService = mock(PayeeService.class);
        given(mockPayeeService.getPayeeByNameAndUserId(anyString(), anyLong())).willReturn(new Payee());
        given(mockPayeeService.getPayeeByNameAndUserId(anyString(), anyLong())).willReturn(new Payee());

        var mockTransactionRepo = mock(TransactionRepository.class);
        given(mockTransactionRepo.findHighestNumberByBillId(anyLong())).willReturn(Optional.of(4L));
        given(mockTransactionRepo.findHighestNumberByBillId(anyLong())).willReturn(Optional.of(5L));
        given(mockTransactionRepo.save(any())).willReturn(new Transaction());
        given(mockTransactionRepo.save(any())).willReturn(new Transaction());

        var toSave1 = new TransactionDTO(15D, "Spożywka : Jedzenie", "Ja", "foo");
        var toSave2 = new TransactionDTO(25D, "Spożywka : Słodycze", "Ja", "bar");

//        system under test
        var toTest = new TransactionService(mockTransactionRepo, mockCategoryService, mockPayeeService);

//        when
        var result = toTest.createTransactionsByBill(List.of(toSave1, toSave2), new Bill(2L, 3L, LocalDate.now(), null,
                null, null, null, new User(5L, "foo@example.com")));

//        then
        assertEquals(2, result.size());
        assertThat(result.get(0)).isInstanceOf(Transaction.class);
    }

    @Test
    void createTransactionByBill_shouldReturnCreatedTransaction()
    {
//        given
        var mockCategoryService = mock(CategoryService.class);
        given(mockCategoryService.getCategoryByNameAndUserId(anyString(), anyLong())).willReturn(new Category());

        var mockPayeeService = mock(PayeeService.class);
        given(mockPayeeService.getPayeeByNameAndUserId(anyString(), anyLong())).willReturn(new Payee());

        var mockTransactionRepo = mock(TransactionRepository.class);
        given(mockTransactionRepo.findHighestNumberByBillId(anyLong())).willReturn(Optional.of(4L));
        given(mockTransactionRepo.save(any())).willReturn(new Transaction());

        var toSave = new TransactionDTO(15D, "Spożywka : Jedzenie", "Ja", "foo");

//        system under test
        var toTest = new TransactionService(mockTransactionRepo, mockCategoryService, mockPayeeService);

//        when
        var result = toTest.createTransactionByBill(toSave, new Bill(2L, 3L, LocalDate.now(), null,
                null, null, null, new User(5L, "foo@example.com")));

//        then
        assertNotNull(result);
        assertThat(result).isInstanceOf(Transaction.class);
    }

    @Test
    void getHighestTransactionNumberByBillId_shouldReturnNumberFromDb()
    {
//        given
        var mockTransactionRepo = mock(TransactionRepository.class);
        given(mockTransactionRepo.findHighestNumberByBillId(anyLong())).willReturn(Optional.of(5L));

//        system under test
        var toTest = new TransactionService(mockTransactionRepo, null, null);

//        when
        var result = toTest.getHighestTransactionNumberByBillId(2L);

//        then
        assertEquals(5L, result);
    }

    @Test
    void getHighestTransactionNumberByBillId_shouldReturnZeroWhenNoNumberInDb()
    {
//        given
        var mockTransactionRepo = mock(TransactionRepository.class);
        given(mockTransactionRepo.findHighestNumberByBillId(anyLong())).willReturn(Optional.empty());

//        system under test
        var toTest = new TransactionService(mockTransactionRepo, null, null);

//        when
        var result = toTest.getHighestTransactionNumberByBillId(5L);

//        then
        assertEquals(0L, result);
    }

    @Test
    void updatePositionInTransaction_shouldUpdateBudgetPositionInTransaction()
    {
//        given
        var mockTransactionRepo = mock(TransactionRepository.class);

        var transaction = new Transaction();

//        system under test
        var toTest = new TransactionService(mockTransactionRepo, null, null);

//        when
        toTest.updatePositionInTransaction(transaction, new BudgetPosition(), 6L);

//        then
        assertNotNull(transaction.getPosition());
    }

    @Test
    void getTransactionsWithoutBudgetPositionByDateAndUserId_shouldReturnTransactionsList()
    {
//        given
        var mockTransactionRepo = mock(TransactionRepository.class);
        given(mockTransactionRepo.findTransactionsBetweenDatesWithoutBudgetPositionByUserId(any(), any(), anyLong()))
                .willReturn(List.of(new Transaction(), new Transaction(), new Transaction()));

//        system under test
        var toTest = new TransactionService(mockTransactionRepo, null, null);

//        when
        var result = toTest.getTransactionsWithoutBudgetPositionByDateAndUserId(LocalDate.now(), LocalDate.now(), 8L);

//        then
        assertEquals(3, result.size());
        assertThat(result.get(0)).isInstanceOf(Transaction.class);
    }

    @Test
    void getTransactionsWithoutBudgetPositionsByUserId_shouldReturnTransactionsList()
    {
//        given
        var mockTransactionRepo = mock(TransactionRepository.class);
        given(mockTransactionRepo.findTransactionsWithoutBudgetPositionByUserId(anyLong())).willReturn(List.of(new Transaction(), new Transaction(), new Transaction()));

//        system under test
        var toTest = new TransactionService(mockTransactionRepo, null, null);

//        when
        var result = toTest.getTransactionsWithoutBudgetPositionsByUserId(9L);

//        then
        assertEquals(3, result.size());
        assertThat(result.get(0)).isInstanceOf(Transaction.class);
    }

}