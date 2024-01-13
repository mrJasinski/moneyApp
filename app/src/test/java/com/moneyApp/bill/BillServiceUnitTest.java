package com.moneyApp.bill;

import com.moneyApp.category.CategoryService;
import com.moneyApp.category.CategoryType;
import com.moneyApp.vo.CategorySource;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class BillServiceUnitTest
{
//    najpewniej zsotanie złączone z update więc test będzei później już dla wspólnej metody

//    BillDTO createBillByUserId(BillDTO toSave, Long userId)
//    {
//        var payee = this.payeeService.getPayeeSourceByNameAndUserId(toSave.getPayeeName(), userId);
//        var account = this.accountService.getAccountSourceByNameAndUserId(toSave.getAccountName(), userId);
//
////        +1 so lowest number (for first bill in month) is 1
//        var count = getBillCountByMonthYearAndUserId(toSave.getDate(), userId) + 1;
//
//        var gainerNames = toSave.getPositions()
//                            .stream()
//                            .map(BillPositionDTO::getGainerName)
//                            .toList();
//
//        var gainers = this.payeeService.getPayeesByNamesAndUserIdAsDto(gainerNames, userId);
//
//        var bill = this.billRepo.save(new BillSnapshot(
//                null
//                , toSave.getDate()
//                , String.valueOf(count)
//
//                , payee
//                , account
//                , null
//                , toSave.getDescription()
//                , toSave.getPositions()
//                    .stream()
//                    .map(dto -> new BillPositionSnapshot(
//                            null
//                        , toSave.getPositions().indexOf(dto) + 1
//                        , dto.getAmount()
//                        , new CategorySource(dto.getCategory().getId())
//                        , new PayeeSource(gainers.stream().filter(p -> dto.getGainerName().equals(p.getName())).toList().get(0).getId())
//                        , dto.getDescription()
//                        ,null))
//                    .collect(Collectors.toSet())
//                , new UserSource(userId)));
//
//        var dto = toDto(bill);
////TODO aktualizacja stanu konta w dopiero gdy jest ono wywoływane? bo tak ot tez update powinien wywoływać i aktualizować o róznicę jeśli odszło do zmian
//        var billSum = sumBillPositionsAmounts(bill);
//        this.accountService.updateAccountBalanceByAccountId(billSum, account.getId());
//
//        return dto;
//    }
//
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
//
//    BillDTO updateBillByNumberAndUserAsDto(final BillDTO toUpdate, final Long userId)
//    {
//        var bill = getBillByNumberAndUserId(toUpdate.getNumber(), userId);
//
//        var oldBillSum = sumBillPositionsAmounts(bill);
//
//        var gainerNames = toUpdate.getPositions()
//                .stream()
//                .map(BillPositionDTO::getGainerName)
//                .toList();
//
//        var gainers = this.payeeService.getPayeesByNamesAndUserIdAsDto(gainerNames, userId);
//
//        var billNumber = toUpdate.getNumber();
//        var budget = bill.getBudget();
//
//        if (bill.getBillDate().getYear() != toUpdate.getDate().getYear() || bill.getBillDate().getMonthValue() != toUpdate.getDate().getMonthValue())
//        {
//            var count = getBillCountByMonthYearAndUserId(toUpdate.getDate(), userId) + 1;
//
//            billNumber = String.valueOf(count);
//            budget = null;
//        }
//
////        var payee = this.payeeService.getPayeeSourceByNameAndUserId(toSave.getPayeeName(), userId);
////        var account = this.accountService.getAccountSourceByNameAndUserId(toSave.getAccountName(), userId);
//
////        var bill = this.billRepo.save(new BillSnapshot(
////                null                                                                                                  xxxxxxx
////                , toSave.getDate()
////                , String.valueOf(count)                                                                               xxxxxxx
////                , payee
////                , account
////                , null                                                                                                xxxxxxx
////                , toSave.getDescription()
////                , toSave.getPositions()
////                    .stream()
////                    .map(dto -> new BillPositionSnapshot(
////                            null
////                        , toSave.getPositions().indexOf(dto) + 1
////                        , dto.getAmount()
////                        , new CategorySource(dto.getCategory().getId())
////                        , new PayeeSource(gainers.stream().filter(p -> dto.getGainerName().equals(p.getName())).toList().get(0).getId())
////                        , dto.getDescription()
////                        ,null))
////                    .collect(Collectors.toSet())
////                , new UserSource(userId)));
//
//        var payee = this.payeeService.getPayeeSourceByNameAndUserId(toUpdate.getPayeeName(), userId);
//        var account = this.accountService.getAccountSourceByNameAndUserId(toUpdate.getAccountName(), userId);
//
//        var result = this.billRepo.save(new BillSnapshot(
//                bill.getId()
//                , toUpdate.getDate()
//                , billNumber
//                , payee
//                , account
//                , budget
//                , toUpdate.getDescription()
//                , toUpdate.getPositions()
//                    .stream()
//                    .map(dto ->
//                    {
//                        var budgetPosition = bill.getPositions().stream().toList().get(Math.toIntExact(dto.getId())).getBudgetPosition();
//
//                        if (bill.getBillDate().getYear() != toUpdate.getDate().getYear() || bill.getBillDate().getMonthValue() != toUpdate.getDate().getMonthValue())
//                            budgetPosition = null;
//
//                       return new BillPositionSnapshot(
//                                dto.getId()
//                                , dto.getNumber()
//                                , dto.getAmount()
//                                , new CategorySource(dto.getCategory().getId())
//                                , new PayeeSource(gainers.stream().filter(g -> dto.getGainerName().equals(g.getName())).toList().get(0).getId())
//                                , dto.getDescription()
//                                , budgetPosition);
//                    })
//                .collect(Collectors.toSet())
//                , bill.getUser()
//        ));
//
//        var billSum = sumBillPositionsAmounts(bill);
//
//        updateAccountBalanceWhenBillIsUpdated(billSum, oldBillSum, account.getId());
//
//        return toDto(result);
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

//    Double sumBillPositionsAmounts(BillSnapshot bill)
//    {
//        var sum = bill.getPositions().stream().mapToDouble(BillPositionSnapshot::getAmount).sum();
//
//        var type = this.categoryService.getCategoryTypeById(bill.getPositions().stream().toList().get(0).getCategory().getId());
//
//        if (EXPENSE.equals(type))
//            sum = -sum;
//
//        return sum;
//    }
//
//    void updateAccountBalanceWhenBillIsUpdated(double billSum, double oldBillSum, long accountId)
//    {
//        if (oldBillSum != billSum)
//        {
//            var diff = billSum - oldBillSum;
//
//            this.accountService.updateAccountBalanceByAccountId(diff, accountId);
//        }
//    }

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
//
//    List<BillDTO> getBillsByUserIdAsDto(Long userId)
//    {
//        return this.billQueryRepo.findByUserId(userId)
//                .stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
//
//    Integer getBillCountByMonthYearAndUserId(final LocalDate date, final Long userId)
//    {
//        var startDate = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
//        var endDate = LocalDate.of(date.getYear(), date.getMonthValue(), date.lengthOfMonth());
//
//        return this.billQueryRepo.findBillCountBetweenDatesAndUserId(startDate, endDate, userId);
//    }
//
//    BillDTO getBillByNumberAndUserIdAsDto(final String number, final Long userId)
//    {
//        return toDto(getBillByNumberAndUserId(number, userId));
//    }
//
//    boolean existsByNumberAndUserId(final String number, final Long userId)
//    {
//        return this.billQueryRepo.existsByNumberAndUserId(number, userId);
//    }

//    TODO - przekazanie null skutkuje nieznalezieniem rachunku z danym numerem więc nie potrzeba zabezpieczenia przeciwnullowego
    @Test
    void getBillByNumberAndUserId_whatWillHappenIfNullNumberGiven()
    {
//        given
        var mockBillQueryRepo = mock(BillQueryRepository.class);
        given(mockBillQueryRepo.findByNumberAndUserId(anyString(), anyLong())).willReturn(Optional.of(new BillSnapshot()));

//        system under test
        var toTest = new BillService(null, mockBillQueryRepo, null ,null, null);

//        when
        var result = catchThrowable(() -> toTest.getBillByNumberAndUserId(null, 1L));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given number not found");
    }
//    BillSnapshot getBillByNumberAndUserId(final String number, final Long userId)
//    {
//        return this.billQueryRepo.findByNumberAndUserId(number, userId)
//                .orElseThrow(() -> new IllegalArgumentException("Bill with given number not found!"));
//    }
//
//    public Set<BillWithSumsDTO> getBudgetPositionsIdsWithSumsByBillPositionIds(final List<Long> billPosIds)
//    {
//        return this.billQueryRepo.findBudgetPositionsIdsWithSumsByBillPositionIds(billPosIds);
//    }
//
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
//    public Set<BillPositionSource> getBillPositionSourcesByBudgetPositionId(final Long budPosId)
//    {
//        return this.billQueryRepo.findBillPositionIdsByBudgetPositionId(budPosId)
//                .stream()
//                .map(BillPositionSource::new)
//                .collect(Collectors.toSet());
//    }
//
//    public List<BillPositionDTO> getBillPositionsWithoutBudgetPositionByMonthYearAndUserIdAsDto(final LocalDate monthYear, final Long userId)
//    {
//        var startDate = Utils.getMonthYearStartDate(monthYear);
//        var endDate = Utils.getMonthYearEndDate(monthYear);
//
//        var bills = this.billQueryRepo.findByDatesAndUserId(startDate, endDate, userId);
//        var positions = new ArrayList<BillPositionDTO>();
//
//        for (BillSnapshot b : bills)
//            positions.addAll(b.getPositions()
//                    .stream()
//                    .filter(p -> p.getBudgetPosition() == null)
//                    .map(this::toDto)
//                    .toList());
//
//        return positions;
//    }
//
//    public void updateBudgetInBillsByMonthYearAndUserId(final LocalDate monthYear, final long budgetId, final Long userId)
//    {
//        var startDate = Utils.getMonthYearStartDate(monthYear);
//        var endDate = Utils.getMonthYearEndDate(monthYear);
//
//        var billIds = this.billQueryRepo.findByDatesAndUserId(startDate, endDate, userId)
//                .stream()
//                .filter(b -> b.getBudget() == null)
//                .map(BillSnapshot::getId)
//                .toList();
//
//        this.billRepo.updateBudgetInBills(budgetId, billIds);
//    }

}