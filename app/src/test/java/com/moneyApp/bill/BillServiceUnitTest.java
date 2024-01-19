package com.moneyApp.bill;

import com.moneyApp.account.AccountService;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.category.CategoryService;
import com.moneyApp.category.CategoryType;
import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.payee.PayeeService;
import com.moneyApp.payee.dto.PayeeDTO;
import com.moneyApp.vo.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class BillServiceUnitTest
{
//    BillDTO toDto(BillSnapshot snap)
//    {
//        var gainerIds = snap.getPositions()
//                        .stream()
//                        .map(BillPositionSnapshot::getGainerId)
//                        .toList();
//
//        var gainers = this.payeeService.getPayeesByIdsAsDto(gainerIds);
//
//      return BillDTO.builder()
//                .withId(snap.getId())
//                .withNumber(snap.getNumber())
//                .withDate(snap.getBillDate())
//                .withPayeeName(this.payeeService.getPayeeNameById(snap.getPayeeId()))
//                .withAccountName(this.accountService.getAccountNameById(snap.getAccountId()))
//                .withDescription(snap.getDescription())
//                .withPositions(snap.getPositions()
//                        .stream()
//                        .map(bp -> BillPositionDTO.builder()
//                                .withId(bp.getId())
//                                .withCategory(this.categoryService.getCategoryByIdAsDto(bp.getCategoryId()))
//                                .withGainerName(gainers.stream().filter(g -> bp.getGainerId().equals(g.getId())).toList().get(0).getName())
//                                .withAmount(bp.getAmount())
//                                .withDescription(bp.getDescription())
//                                .build()
//                        ).collect(Collectors.toList()))
//                .build();
//    }

    @Test
    void prepareBillToSave_shouldGetBillDataWhenBillAlreadyExistsAndMonthYearMatchesIsTrueAndIsUpdated()
    {
//        given
        var dtoPositions = List.of(BillPositionDTO.builder()
                        .withId(7L)
                        .withCategory(new CategoryDTO(3L, "bar"))
                        .withGainerName("foo")
                        .withAmount(15d)
                        .build());

        var toSave = BillDTO.builder()
                .withId(2L)
                .withAccountName("foo")
                .withDate(LocalDate.now())
                .withNumber("2024_1")
                .withPayeeName("bar")
                .withDescription("foobar")
                .withPositions(dtoPositions)
                .build();

        var positions = Set.of(new BillPositionSnapshot(1L, 4L, 12d, new CategorySource(12L), new PayeeSource(8L), "foo", null));
        var bill = new BillSnapshot(
                2L
                , LocalDate.now()
                , "2024_5"
                , new PayeeSource(2L)
                , new AccountSource(9L)
                , new BudgetSource(1L)
                , "foo", positions
                , new UserSource(2L));

        var mockBillRepo = mock(BillRepository.class);
        given(mockBillRepo.save(any())).willReturn(bill);

        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.findByNumberAndUserId(anyString(), anyLong())).willReturn(Optional.of(bill));

        var mockPayeeService = mock(PayeeService.class);
        given(mockPayeeService.getPayeeSourceByNameAndUserId(anyString(), anyLong())).willReturn(new PayeeSource(2L));
        given(mockPayeeService.getPayeesByNamesAndUserIdAsDto(anySet(), anyLong())).willReturn(Set.of(new PayeeDTO(1L, "foo")));

        var mockAccountService = mock(AccountService.class);
        given(mockAccountService.getAccountSourceByNameAndUserId(anyString(), anyLong())).willReturn(new AccountSource(9L));

        var mockCategoryRepo = mock(CategoryService.class);

//        system under test
        var toTest = new BillService(mockBillRepo, mockBillQueryRepo, mockCategoryRepo, mockPayeeService, mockAccountService);

//        when
        var result = toTest.prepareBillToSave(toSave, 2L);

//        then
        assertEquals(bill.getId(), result.getId());
        assertEquals(bill.getNumber(), result.getNumber());
        assertEquals(bill.getBudget(), result.getBudget());
    }

    @Test
    void prepareBillToSave_shouldGetBillDataWhenBillAlreadyExistsAndMonthYearMatchesIsFalseAndIsUpdated()
    {
//        given
        var dtoPositions = List.of(BillPositionDTO.builder()
                .withId(7L)
                .withCategory(new CategoryDTO(3L, "bar"))
                .withGainerName("foo")
                .withAmount(15d)
                .build());

        var toSave = BillDTO.builder()
                .withId(2L)
                .withAccountName("foo")
                .withDate(LocalDate.now().plusMonths(1))
                .withNumber("2024_1")
                .withPayeeName("bar")
                .withDescription("foobar")
                .withPositions(dtoPositions)
                .build();

        var positions = Set.of(new BillPositionSnapshot(1L, 4L, 12d, new CategorySource(12L), new PayeeSource(8L), "foo", null));
        var bill = new BillSnapshot(
                2L
                , LocalDate.now()
                , "2024_5"
                , new PayeeSource(2L)
                , new AccountSource(9L)
                , new BudgetSource(1L)
                , "foo", positions
                , new UserSource(2L));

        var mockBillRepo = mock(BillRepository.class);
        given(mockBillRepo.save(any())).willReturn(bill);

        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.findByNumberAndUserId(anyString(), anyLong())).willReturn(Optional.of(bill));
        given(mockBillQueryRepo.findBillCountBetweenDatesAndUserId(any(), any(), anyLong())).willReturn(5);

        var mockPayeeService = mock(PayeeService.class);
        given(mockPayeeService.getPayeeSourceByNameAndUserId(anyString(), anyLong())).willReturn(new PayeeSource(2L));
        given(mockPayeeService.getPayeesByNamesAndUserIdAsDto(anySet(), anyLong())).willReturn(Set.of(new PayeeDTO(1L, "foo")));

        var mockAccountService = mock(AccountService.class);
        given(mockAccountService.getAccountSourceByNameAndUserId(anyString(), anyLong())).willReturn(new AccountSource(9L));

        var mockCategoryRepo = mock(CategoryService.class);

//        system under test
        var toTest = new BillService(mockBillRepo, mockBillQueryRepo, mockCategoryRepo, mockPayeeService, mockAccountService);

//        when
        var result = toTest.prepareBillToSave(toSave, 2L);

//        then
        assertEquals(bill.getId(), result.getId());
        assertEquals("20242_6", result.getNumber());
        assertNull(result.getBudget());
    }

    @Test
    void prepareBillToSave_shouldSaveNewDataWhenBillDoesntExist()
    {
//        given
        var dtoPositions = List.of(BillPositionDTO.builder()
                .withId(7L)
                .withCategory(new CategoryDTO(3L, "bar"))
                .withGainerName("foo")
                .withAmount(15d)
                .build());

        var toSave = BillDTO.builder()
                .withId(2L)
                .withAccountName("foo")
                .withDate(LocalDate.now().plusMonths(1))
                .withNumber("2024_1")
                .withPayeeName("bar")
                .withDescription("foobar")
                .withPositions(dtoPositions)
                .build();

        var bill = new BillSnapshot();

        var mockBillRepo = mock(BillRepository.class);
        given(mockBillRepo.save(any())).willReturn(bill);

        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.findByNumberAndUserId(anyString(), anyLong())).willReturn(Optional.empty());
        given(mockBillQueryRepo.findBillCountBetweenDatesAndUserId(any(), any(), anyLong())).willReturn(5);

        var mockPayeeService = mock(PayeeService.class);
        given(mockPayeeService.getPayeeSourceByNameAndUserId(anyString(), anyLong())).willReturn(new PayeeSource(2L));
        given(mockPayeeService.getPayeesByNamesAndUserIdAsDto(anySet(), anyLong())).willReturn(Set.of(new PayeeDTO(1L, "foo")));

        var mockAccountService = mock(AccountService.class);
        given(mockAccountService.getAccountSourceByNameAndUserId(anyString(), anyLong())).willReturn(new AccountSource(9L));

        var mockCategoryRepo = mock(CategoryService.class);

//        system under test
        var toTest = new BillService(mockBillRepo, mockBillQueryRepo, mockCategoryRepo, mockPayeeService, mockAccountService);

//        when
        var result = toTest.prepareBillToSave(toSave, 2L);

//        then
        assertEquals(0L, result.getId());
        assertEquals("20242_6", result.getNumber());
        assertNull(result.getBudget());
    }

//    BillSnapshot prepareBillToSave(BillDTO toSave, Long userId)
//    {
//        var payee = this.payeeService.getPayeeSourceByNameAndUserId(toSave.getPayeeName(), userId);
//        var account = this.accountService.getAccountSourceByNameAndUserId(toSave.getAccountName(), userId);
//
//        var gainerNames = toSave.getGainerNames();
//
//        var gainers = this.payeeService.getPayeesByNamesAndUserIdAsDto(gainerNames, userId);
//
//        var toSaveDate = toSave.getDate();
//
//        long billId;
//        String billNumber;
//        BudgetSource budget;
//        var oldBillSum = 0d;
//
//        var monthYearMatches = false;
//
//        try
//        {
//            var bill = getBillByNumberAndUserId(toSave.getNumber(), userId);
//
//            var billDate = bill.getBillDate();
//            monthYearMatches = checkIfMonthYearMatch(billDate, toSaveDate);
//
//            billId = bill.getId();
//            billNumber = monthYearMatches ? bill.getNumber() : setBillsCountAsBillNumber(toSaveDate, userId);
//            budget = monthYearMatches ? bill.getBudget() : null;
//
//            oldBillSum = sumBillPositionsAmounts(bill);
//        }
//        catch (IllegalArgumentException ex)
//        {
//            billId = 0L;
//            billNumber = setBillsCountAsBillNumber(toSaveDate, userId);
//            budget = null;
//        }
////TODO jak to obejść?
//      final boolean finalMonthYearMatches = monthYearMatches;
//
//        var result = new BillSnapshot(
//                billId
//                , toSaveDate
//                , billNumber
//                , payee
//                , account
//                , budget
//                , toSave.getDescription()
//                , toSave.getPositions()
//                .stream()
//                .map(dto ->
//                {
//                    var index = toSave.getPositionIndex(dto);
//                    //                                    TODO czy tu się też nie oprzeć o obiekt jak w przypadku category?
//                    var gainerId = gainers.stream().filter(g -> dto.getGainerName().equals(g.getName())).toList().get(0).getId();
//
//                    return prepareBillPosition(dto, finalMonthYearMatches, index, gainerId);
//                })
//                .collect(Collectors.toSet())
//                , new UserSource(userId));
//
//        this.billRepo.save(result);
//
//        var billSum = sumBillPositionsAmounts(result);
//        updateAccountBalanceByBillSum(billSum, oldBillSum, account.getId());
//
//        return result;
//    }

    @Test
    void prepareBillPosition_shouldSetBudgetPositionAsNullForDtoWithoutBudgetPositionAndMonthYearMatchesIsFalse()
    {
//        given
        var dto = BillPositionDTO.builder()
                .withCategory(new CategoryDTO(1L, ""))
                .withBudgetPosition(null)
                .build();

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.prepareBillPosition(dto, false, 0, 0);

//        then
        assertNull(result.getBudgetPosition());
    }

    @Test
    void prepareBillPosition_shouldSetBudgetPositionAsNullForDtoWithoutBudgetPositionAndMonthYearMatchesIsTrue()
    {
//        given
        var dto = BillPositionDTO.builder()
                .withCategory(new CategoryDTO(1L, ""))
                .withBudgetPosition(null)
                .build();

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.prepareBillPosition(dto, true, 0, 0);

//        then
        assertNull(result.getBudgetPosition());
    }

    @Test
    void prepareBillPosition_shouldSetBudgetPositionAsNullWhenMonthYearMatchesIsFalseAndDtoHasNullBudgetPosition()
    {
//        given
        var dto = BillPositionDTO.builder()
                .withCategory(new CategoryDTO(1L, ""))
                .withBudgetPosition(null)
                .build();

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.prepareBillPosition(dto, false, 0, 0);

//        then
        assertNull(result.getBudgetPosition());
    }

    @Test
    void prepareBillPosition_shouldSetBudgetPositionAsNullWhenMonthYearMatchesIsFalseEvenIfDtoHasBudgetPosition()
    {
//        given
        var dto = BillPositionDTO.builder()
                .withCategory(new CategoryDTO(1L, ""))
                .withBudgetPosition(new BudgetPositionSource(10L))
                .build();

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.prepareBillPosition(dto, false, 0, 0);

//        then
        assertNull(result.getBudgetPosition());
    }

    @Test
    void prepareBillPosition_shouldSetBudgetPositionValueWhenMonthYearMatchesIsTrueAndDtoHasBudgetPosition()
    {
//        given
        var dto = BillPositionDTO.builder()
                .withCategory(new CategoryDTO(1L, ""))
                .withBudgetPosition(new BudgetPositionSource(11L))
                .build();

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.prepareBillPosition(dto, true, 0, 0);

//        then
        assertNotNull(result.getBudgetPosition());
        assertEquals(11L, dto.getBudgetPosition().getId());
    }

    @Test
    void prepareBillPosition_shouldSetBillPositionIdZeroWhenIdFromDtoIsNull()
    {
//        given
        var dto = BillPositionDTO.builder()
                .withId(null)
                .withCategory(new CategoryDTO(1L, ""))
                .build();

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.prepareBillPosition(dto, false, 0, 0);

//        then
        assertEquals(0L, result.getId());
    }

    @Test
    void prepareBillPosition_shouldSetBillPositionIdAsDtoIdWhenIdFromDtoIsNotNull()
    {
//        given
        var dto = BillPositionDTO.builder()
                .withId(5L)
                .withCategory(new CategoryDTO(1L, ""))
                .build();

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.prepareBillPosition(dto, false, 0, 0);

//        then
        assertEquals(5L, result.getId());
    }

    @Test
    void prepareBillPosition_shouldSetNumberToGivenIndexPlusOneWhenBillPositionNumberIsNull()
    {
//        given
        var dto = BillPositionDTO.builder()
                .withCategory(new CategoryDTO(1L, ""))
                .build();

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.prepareBillPosition(dto, false, 2, 0);

//        then
        assertEquals(3L, result.getNumber());
    }

    @Test
    void prepareBillPosition_shouldSetNumberToDtoNumberWhenBillPositionNumberIsNotNull()
    {
//        given
        var dto = BillPositionDTO.builder()
                .withNumber(4L)
                .withCategory(new CategoryDTO(1L, ""))
                .build();

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.prepareBillPosition(dto, false, 2, 0);

//        then
        assertEquals(4L, result.getNumber());
    }

    @Test
    void checkIfMonthYearMatch_shouldReturnTrueWhenMonthsAndYearsOfGivenDatesMatch()
    {
//        given
        var billDate = LocalDate.now();
        var toSaveDate = LocalDate.now();

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.checkIfMonthYearMatch(billDate, toSaveDate);

//        then
        assertTrue(result);
    }

    @Test
    void checkIfMonthYearMatch_shouldReturnFalseWhenMonthsOfGivenDatesDontMatch()
    {
//        given
        var billDate = LocalDate.now().minusMonths(1);
        var toSaveDate = LocalDate.now();

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.checkIfMonthYearMatch(billDate, toSaveDate);

//        then
        assertFalse(result);
    }

    @Test
    void checkIfMonthYearMatch_shouldReturnFalseWhenYearsOfGivenDatesDontMatch()
    {
//        given
        var billDate = LocalDate.now();
        var toSaveDate = LocalDate.now().plusYears(1);

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.checkIfMonthYearMatch(billDate, toSaveDate);

//        then
        assertFalse(result);
    }

    @Test
    void checkIfMonthYearMatch_shouldReturnFalseWhenMonthsAndYearsOfGivenDatesDontMatch()
    {
//        given
        var billDate = LocalDate.now().minusMonths(1);
        var toSaveDate = LocalDate.now().plusYears(1);

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.checkIfMonthYearMatch(billDate, toSaveDate);

//        then
        assertFalse(result);
    }

//    BillDTO updateBillByNumberAndUserAsDto(final BillDTO toUpdate, final Long userId)
//    {
//        toDto(saveBill(toUpdate, userId));
//    }

    @Test
    void sumBillPositionsAmounts_shouldReturnMinusSumWhenBillCategoriesTypeIsExpense()
    {
//        given
        var position1 = new BillPositionSnapshot(null, 0, 12d, new CategorySource(1L), null, null,null);
        var position2 = new BillPositionSnapshot(null, 0, 13d, new CategorySource(2L), null, null,null);
        var positions = Set.of(position1, position2);
        var bill = new BillSnapshot(null , null, "0_0", null, null, null, null, positions, null);

        var mockCategoryService = mock(CategoryService.class);
        given(mockCategoryService.getCategoryTypeById(anyLong())).willReturn(CategoryType.EXPENSE);

//        system under test
        var toTest = new BillService(null, null, mockCategoryService, null, null);

//        when
        var result = toTest.sumBillPositionsAmounts(bill);

//        then
        assertTrue(result < 0);
        assertEquals(-25d, result);
    }

    @Test
    void sumBillPositionsAmounts_shouldReturnSumWhenBillCategoriesTypeIsIncome()
    {
//        given
        var position1 = new BillPositionSnapshot(null, 0, 12d, new CategorySource(1L), null, null,null);
        var position2 = new BillPositionSnapshot(null, 0, 13d, new CategorySource(2L), null, null,null);
        var positions = Set.of(position1, position2);
        var bill = new BillSnapshot(null , null, "0_0", null, null, null, null, positions, null);

        var mockCategoryService = mock(CategoryService.class);
        given(mockCategoryService.getCategoryTypeById(anyLong())).willReturn(CategoryType.INCOME);

//        system under test
        var toTest = new BillService(null, null, mockCategoryService, null, null);

//        when
        var result = toTest.sumBillPositionsAmounts(bill);

//        then
        assertTrue(result > 0);
        assertEquals(25d, result);
    }

    @Test
    void checkIfMonthYearMatch_shouldReturnFalseWhenYearsDontMatch()
    {
//        given
        var billDate = LocalDate.of(2024, 1, 12);
        var toSaveDate = LocalDate.of(2023, 1, 12);

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.checkIfMonthYearMatch(billDate, toSaveDate);

//        then
        assertFalse(result);
    }

    @Test
    void checkIfMonthYearMatch_shouldReturnFalseWhenMonthsDontMatch()
    {
//        given
        var billDate = LocalDate.of(2024, 1, 12);
        var toSaveDate = LocalDate.of(2024, 11, 12);

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.checkIfMonthYearMatch(billDate, toSaveDate);

//        then
        assertFalse(result);
    }

    @Test
    void checkIfMonthYearMatch_shouldReturnFalseWhenYearsAndMonthsDontMatch()
    {
//        given
        var billDate = LocalDate.of(2024, 1, 12);
        var toSaveDate = LocalDate.of(2023, 11, 12);

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.checkIfMonthYearMatch(billDate, toSaveDate);

//        then
        assertFalse(result);
    }

    @Test
    void checkIfMonthYearMatch_shouldReturnTrueWhenYearsAndMonthsMatch()
    {
//        given
        var billDate = LocalDate.of(2024, 1, 12);
        var toSaveDate = LocalDate.of(2024, 1, 12);

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.checkIfMonthYearMatch(billDate, toSaveDate);

//        then
        assertTrue(result);
    }

//    void deleteBillByNumberAndUserId(final String number, final Long userId)
//    {
//        this.billRepo.deleteByNumberAndUserId(number, userId);
//    }

    @Test
    void getBillsByUserId_shouldReturnBillsFromDbForGivenUser()
    {
//        given
        var bills = List.of(new BillSnapshot(), new BillSnapshot(), new BillSnapshot());

        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.findByUserId(anyLong())).willReturn(bills);

//        system under test
        var toTest = new BillService(null, mockBillQueryRepo, null, null, null);

//        when
        var result = toTest.getBillsByUserId(12L);

//        then
        assertEquals(3, result.size());
    }

//
//    List<BillDTO> getBillsByUserIdAsDto(Long userId)
//    {
//        return getBillsByUserId(userId)
//                .stream()
//                .map(this::toDto)
//                .toList();
//    }

    @Test
    void getBillCountByMonthYearAndUserId_shouldReturnZeroWhenNoBillsFound()
    {
//        given
        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.findBillCountBetweenDatesAndUserId(any(), any(), anyLong())).willReturn(0);

//        system under test
        var toTest = new BillService(null, mockBillQueryRepo, null, null, null);

//        when
        var result = toTest.getBillCountByMonthYearAndUserId(LocalDate.now(), 3L);

//        then
        assertEquals(0, result);
    }

    @Test
    void getBillCountByMonthYearAndUserId_shouldReturnCountedBillsFound()
    {
//        given
        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.findBillCountBetweenDatesAndUserId(any(), any(), anyLong())).willReturn(5);

//        system under test
        var toTest = new BillService(null, mockBillQueryRepo, null, null, null);

//        when
        var result = toTest.getBillCountByMonthYearAndUserId(LocalDate.now(), 3L);

//        then
        assertEquals(5, result);
    }

//    BillDTO getBillByNumberAndUserIdAsDto(final String number, final Long userId)
//    {
//        return toDto(getBillByNumberAndUserId(number, userId));
//    }

    @Test
    void existsByNumberAndUserId_shouldReturnTrueIfBillExistsInDb()
    {
//        given
        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.existsByNumberAndUserId(anyString(), anyLong())).willReturn(true);

//        system under test
        var toTest = new BillService(null, mockBillQueryRepo, null, null, null);

//        when
        var result = toTest.existsByNumberAndUserId("202401_12", 2L);

//        then
        assertTrue(result);
    }

    @Test
    void existsByNumberAndUserId_shouldReturnFalseIfBillDoesntExistInDb()
    {
//        given
        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.existsByNumberAndUserId(anyString(), anyLong())).willReturn(false);

//        system under test
        var toTest = new BillService(null, mockBillQueryRepo, null, null, null);

//        when
        var result = toTest.existsByNumberAndUserId("202401_12", 2L);

//        then
        assertFalse(result);
    }

    @Test
    void getBillByNumberAndUserId_shouldThrowExceptionBecauseBillWithNullNumberIsNotFound()
    {
//        given
        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.findByNumberAndUserId(anyString(), anyLong())).willReturn(Optional.empty());

//        system under test
        var toTest = new BillService(null, mockBillQueryRepo, null ,null, null);

//        when
        var result = catchThrowable(() -> toTest.getBillByNumberAndUserId(null, 1L));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given number not found");
    }

    @Test
    void getBillByNumberAndUserId_shouldThrowExceptionWhenBillWithGivenNumberIsNotFound()
    {
//        given
        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.findByNumberAndUserId(anyString(), anyLong())).willReturn(Optional.empty());

//        system under test
        var toTest = new BillService(null, mockBillQueryRepo, null ,null, null);

//        when
        var result = catchThrowable(() -> toTest.getBillByNumberAndUserId("2022_10", 1L));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given number not found");
    }

    @Test
    void getBillByNumberAndUserId_shouldReturnBillWhenBilLWithGivenNumberIsFound()
    {
//        given
        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.findByNumberAndUserId(anyString(), anyLong())).willReturn(Optional.of(new BillSnapshot()));

//        system under test
        var toTest = new BillService(null, mockBillQueryRepo, null ,null, null);

//        when
        var result = toTest.getBillByNumberAndUserId("2022_10", 1L);

//        then
        assertNotNull(result);
    }

//    public Set<BillWithSumsDTO> getBudgetPositionsIdsWithSumsByBillPositionIds(final List<Long> billPosIds)
//    {
//        return this.billQueryRepo.findBudgetPositionsIdsWithSumsByBillPositionIds(billPosIds);
//    }

//    BillPositionDTO toDto(BillPositionSnapshot snap)
//    {
//        return BillPositionDTO.builder()
//                .withId(snap.getId())
//                .withCategory(this.categoryService.getCategoryByIdAsDto(snap.getCategoryId()))
//                .withAmount(snap.getAmount())
//                .withGainerName(this.payeeService.getPayeeNameById(snap.getGainerId()))
//                .withDescription(snap.getDescription())
//                .build();
//    }
//
//    public void updateBudgetPositionInBillPositionById(final Long budgetPositionId, final Set<Long> billPositionIds)
//    {
//        this.billRepo.updateBudgetPositionInBillPositionByIds(budgetPositionId, billPositionIds);
//    }
//
//    Set<Long> getBillPositionIdsByBudgetPositionId(final long budPosId)
//    {
//        return this.billQueryRepo.findBillPositionIdsByBudgetPositionId(budPosId);
//    }
//
//    public Set<BillPositionSource> getBillPositionSourcesByBudgetPositionId(final Long budPosId)
//    {
//        return getBillPositionIdsByBudgetPositionId(budPosId)
//                .stream()
//                .map(BillPositionSource::new)
//                .collect(Collectors.toSet());
//    }
//
//    List<BillSnapshot> getBillsByMonthYearAndUserId(final LocalDate monthYear, final long userId)
//    {
//        var startDate = Utils.getMonthYearStartDate(monthYear);
//        var endDate = Utils.getMonthYearEndDate(monthYear);
//
//        return this.billQueryRepo.findByDatesAndUserId(startDate, endDate, userId);
//    }
//
//    List<BillPositionSnapshot> getBillPositionsWithoutBudgetPositionByMonthYearAndUserId(final LocalDate monthYear, final Long userId)
//    {
//        var bills = getBillsByMonthYearAndUserId(monthYear, userId);
//        var positions = new ArrayList<BillPositionSnapshot>();
//
//        for (BillSnapshot b : bills)
//            positions.addAll(b.filterPositionsWithoutBudgetPositions());
//
//        return positions;
//    }
//
//    public List<BillPositionDTO> getBillPositionsWithoutBudgetPositionByMonthYearAndUserIdAsDto(final LocalDate monthYear, final Long userId)
//    {
//        return getBillPositionsWithoutBudgetPositionByMonthYearAndUserId(monthYear, userId)
//                .stream()
//                .map(this::toDto)
//                .toList();
//    }
//
//    public void updateBudgetInBillsByMonthYearAndUserId(final LocalDate monthYear, final long budgetId, final Long userId)
//    {
//        var billIds = getBillsByMonthYearAndUserId(monthYear, userId)
//                .stream()
//                .filter(b -> b.getBudget() == null)
//                .map(BillSnapshot::getId)
//                .toList();
//
//        this.billRepo.updateBudgetInBills(budgetId, billIds);
//    }
}