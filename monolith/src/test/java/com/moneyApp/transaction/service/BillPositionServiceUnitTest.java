package com.moneyApp.transaction.service;

import com.moneyApp.bill.service.BillPositionService;
import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.bill.BillPosition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class BillPositionServiceUnitTest
{
//    TODO przeniesienie do BillServiceImpl?
//    @Test
//    void getTransactionsByMonthYearAndUserId_shouldReturnTransactionsList()
//    {
////        given
//        var mockTransactionRepo = mock(BillPositionRepository.class);
//        given(mockTransactionRepo.findTransactionsBetweenDatesAndUserId(any(), any(), anyLong())).willReturn(List.of(
//                new BillPosition(), new BillPosition(), new BillPosition()));
//
//        var date = LocalDate.now();
//
////        system under test
//        var toTest = new BillPositionService(mockTransactionRepo, null, null, null);
//
////        when
//        var result = toTest.getTransactionsByMonthYearAndUserId(date, 5L);
//
////        then
//        assertNotNull(result);
//        assertEquals(3, result.size());
//        assertThat(result.get(0)).isInstanceOf(BillPosition.class);
//    }
//
//    @Test
//    void createTransactionsByBill_shouldReturnCreatedTransactionsList()
//    {
////        given
//        var mockCategoryService = mock(CategoryService.class);
//        given(mockCategoryService.getCategoryByNameAndUserId(anyString(), anyLong())).willReturn(new Category());
//        given(mockCategoryService.getCategoryByNameAndUserId(anyString(), anyLong())).willReturn(new Category());
//
//        var mockPayeeService = mock(PayeeService.class);
//        given(mockPayeeService.getPayeeByNameAndUserId(anyString(), anyLong())).willReturn(new Payee());
//        given(mockPayeeService.getPayeeByNameAndUserId(anyString(), anyLong())).willReturn(new Payee());
//
//        var mockTransactionRepo = mock(BillPositionRepository.class);
//        given(mockTransactionRepo.findHighestNumberByBillId(anyLong())).willReturn(Optional.of(4L));
//        given(mockTransactionRepo.findHighestNumberByBillId(anyLong())).willReturn(Optional.of(5L));
//        given(mockTransactionRepo.save(any())).willReturn(new BillPosition());
//        given(mockTransactionRepo.save(any())).willReturn(new BillPosition());
//
//        var toSave1 = new BillPositionDTO(15D, "Spożywka : Jedzenie", "Ja", "foo");
//        var toSave2 = new BillPositionDTO(25D, "Spożywka : Słodycze", "Ja", "bar");
//
////        system under test
//        var toTest = new BillPositionService(mockTransactionRepo, mockCategoryService, mockPayeeService, null);
//
////        when
//        var result = toTest.createTransactionsByBill(List.of(toSave1, toSave2), new Bill(2L, 3L, LocalDate.now(), null,
//                null, null, null, new User(5L, "foo@example.com")));
//
////        then
//        assertEquals(2, result.size());
//        assertThat(result.get(0)).isInstanceOf(BillPosition.class);
//    }
//
//    @Test
//    void createTransactionByBill_shouldReturnCreatedTransaction()
//    {
////        given
//        var mockCategoryService = mock(CategoryService.class);
//        given(mockCategoryService.getCategoryByNameAndUserId(anyString(), anyLong())).willReturn(new Category());
//
//        var mockPayeeService = mock(PayeeService.class);
//        given(mockPayeeService.getPayeeByNameAndUserId(anyString(), anyLong())).willReturn(new Payee());
//
//        var mockTransactionRepo = mock(BillPositionRepository.class);
//        given(mockTransactionRepo.findHighestNumberByBillId(anyLong())).willReturn(Optional.of(4L));
//        given(mockTransactionRepo.save(any())).willReturn(new BillPosition());
//
//        var toSave = new BillPositionDTO(15D, "Spożywka : Jedzenie", "Ja", "foo");
//
////        system under test
//        var toTest = new BillPositionService(mockTransactionRepo, mockCategoryService, mockPayeeService, null);
//
////        when
//        var result = toTest.createTransactionByBill(toSave, new Bill(2L, 3L, LocalDate.now(), null,
//                null, null, null, new User(5L, "foo@example.com")));
//
////        then
//        assertNotNull(result);
//        assertThat(result).isInstanceOf(BillPosition.class);
//    }
//
//    @Test
//    void getHighestTransactionNumberByBillId_shouldReturnNumberFromDb()
//    {
////        given
//        var mockTransactionRepo = mock(BillPositionRepository.class);
//        given(mockTransactionRepo.findHighestNumberByBillId(anyLong())).willReturn(Optional.of(5L));
//
////        system under test
//        var toTest = new BillPositionService(mockTransactionRepo, null, null, null);
//
////        when
//        var result = toTest.getHighestTransactionNumberByBillId(2L);
//
////        then
//        assertEquals(5L, result);
//    }
//
//    @Test
//    void getHighestTransactionNumberByBillId_shouldReturnZeroWhenNoNumberInDb()
//    {
////        given
//        var mockTransactionRepo = mock(BillPositionRepository.class);
//        given(mockTransactionRepo.findHighestNumberByBillId(anyLong())).willReturn(Optional.empty());
//
////        system under test
//        var toTest = new BillPositionService(mockTransactionRepo, null, null, null);
//
////        when
//        var result = toTest.getHighestTransactionNumberByBillId(5L);
//
////        then
//        assertEquals(0L, result);
//    }
//
//    @Test
//    void updatePositionInTransaction_shouldUpdateBudgetPositionInTransaction()
//    {
////        given
//        var mockTransactionRepo = mock(BillPositionRepository.class);
//
//        var transaction = new BillPosition();
//
////        system under test
//        var toTest = new BillPositionService(mockTransactionRepo, null, null, null);
//
////        when
//        toTest.updatePositionInTransaction(transaction, new BudgetPosition(), 6L);
//
////        then
//        assertNotNull(transaction.getPosition());
//    }
//
//    @Test
//    void getTransactionsWithoutBudgetPositionByDateAndUserId_shouldReturnTransactionsList()
//    {
////        given
//        var mockTransactionRepo = mock(BillPositionRepository.class);
//        given(mockTransactionRepo.findTransactionsBetweenDatesWithoutBudgetPositionByUserId(any(), any(), anyLong()))
//                .willReturn(List.of(new BillPosition(), new BillPosition(), new BillPosition()));
//
////        system under test
//        var toTest = new BillPositionService(mockTransactionRepo, null, null, null);
//
////        when
//        var result = toTest.getTransactionsWithoutBudgetPositionByDateAndUserId(LocalDate.now(), LocalDate.now(), 8L);
//
////        then
//        assertEquals(3, result.size());
//        assertThat(result.get(0)).isInstanceOf(BillPosition.class);
//    }
//
//    @Test
//    void getTransactionsWithoutBudgetPositionsByUserId_shouldReturnTransactionsList()
//    {
////        given
//        var mockTransactionRepo = mock(BillPositionRepository.class);
//        given(mockTransactionRepo.findTransactionsWithoutBudgetPositionByUserId(anyLong())).willReturn(List.of(new BillPosition(), new BillPosition(), new BillPosition()));
//
////        system under test
//        var toTest = new BillPositionService(mockTransactionRepo, null, null, null);
//
////        when
//        var result = toTest.getTransactionsWithoutBudgetPositionsByUserId(9L);
//
////        then
//        assertEquals(3, result.size());
//        assertThat(result.get(0)).isInstanceOf(BillPosition.class);
//    }

//    public List<TransactionDTO> getTransactionsByDatesAndUserMailAsDto(LocalDate startDate, LocalDate endDate, String mail)
//    {
//        return getTransactionsByDatesAndUserId(startDate, endDate, this.userService.getUserIdByEmail(mail))
//                .stream()
//                .map(Transaction::toDto)
//                .collect(Collectors.toList());
//    }
//
//    private List<Transaction> getTransactionsByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId)
//    {
//        return this.transactionRepo.findTransactionsBetweenDatesAndUserId(startDate, endDate, userId);
//    }

}