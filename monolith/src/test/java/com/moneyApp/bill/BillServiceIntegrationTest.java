package com.moneyApp.bill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class BillServiceIntegrationTest
{
    @Autowired
    private BillRepository billRepo;

    @Autowired
    private BillQueryRepository billQueryRepo;

//    BillDTO createBillByUserIdAsDto(BillDTO toSave, Long userId)
//    {
//        return toDto(saveBill(toSave, userId));
//    }
//
//    BillDTO toDto(BillSnapshot snap)
//    {
//        var gainers = this.payeeService.getPayeesByIdsAsDto(snap.getGainerIds());
//
//        return BillDTO.builder()
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
////    TODO konkretna nazwa
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
//        final boolean finalMonthYearMatches = monthYearMatches;
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
//        var billSum = sumBillPositionsAmounts(result);
//        updateAccountBalanceByBillSum(billSum, oldBillSum, account.getId());
//
//        return result;
//    }
//
//    BillSnapshot saveBill(BillDTO toSave, Long userId)
//    {
//        return this.billRepo.save(prepareBillToSave(toSave, userId));
//    }
//
//    BillPositionSnapshot prepareBillPosition(BillPositionDTO dto, boolean monthYearMatches, int index, long gainerId)
//    {
////        set to null in two cases
////        when it's newly created object and has no budget position assigned
////        when during update bill date was changed (only month or/and year are important) and bill will be assigned to another budget
//        var budgetPosition = dto.getBudgetPosition();
//        if (!monthYearMatches)
//            budgetPosition = null;
//
//        return new BillPositionSnapshot(
//                dto.getId() == null ? 0L : dto.getId()
//                , dto.getNumber() == null ? index + 1 : dto.getNumber()
//                , dto.getAmount()
//                , new CategorySource(dto.getCategoryId())
//                , new PayeeSource(gainerId)
//                , dto.getDescription()
//                , budgetPosition);
//    }
//
//    String setBillsCountAsBillNumber(LocalDate toSaveDate, long userId)
//    {
//        //        +1 so lowest number (for first bill in month) is 1
//        var count = getBillCountByMonthYearAndUserId(toSaveDate, userId) + 1;
//
//        return String.valueOf(count);
//    }
//
//    boolean checkIfMonthYearMatch(LocalDate billDate, LocalDate toSaveDate)
//    {
//        return billDate.getYear() == toSaveDate.getYear() && billDate.getMonthValue() == toSaveDate.getMonthValue();
//    }
//
//    BillDTO updateBillByNumberAndUserAsDto(final BillDTO toUpdate, final Long userId)
//    {
//        return toDto(saveBill(toUpdate, userId));
//    }
//
//    Double sumBillPositionsAmounts(BillSnapshot bill)
//    {
//        var sum = bill.getBillSum();
//
//        var type = this.categoryService.getCategoryTypeById(bill.getExemplaryCategoryId());
//
//        if (EXPENSE.equals(type))
//            sum = -sum;
//
//        return sum;
//    }
//
//    void updateAccountBalanceByBillSum(double billSum, double oldBillSum, long accountId)
//    {
//        var sum = billSum - oldBillSum;
//
//        if (sum != 0)
//            this.accountService.updateAccountBalanceByAccountId(sum, accountId);
//    }
//
//    void deleteBillByNumberAndUserId(final String number, final Long userId)
//    {
//        this.billRepo.deleteByNumberAndUserId(number, userId);
//    }
//
//    List<BillSnapshot> getBillsByUserId(Long userId)
//    {
//        return this.billQueryRepo.findByUserId(userId);
//    }
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
        var monthYear = LocalDate.of(2011, 12, 11);
        var userId = 2L;

//        system under test
        var toTest = new BillService(null, billQueryRepo, null, null, null);

//        when
        var result = toTest.getBillCountByMonthYearAndUserId(monthYear, userId);

//        then
        assertEquals(0, result);
    }


//    TODO dlaczego zero - wygląda na problem z datami
    @Test
    void getBillCountByMonthYearAndUserId_shouldReturnCountOfBillsFound()
    {
//        given
        var monthYear = LocalDate.of(2012, 12, 11);
        var userId = 4L;

//        this.billRepo.save(new BillSnapshot(
//                null
//                , monthYear
//                , "5"
//                , new PayeeSource(1L)
//                , new AccountSource(3L)
//                , new BudgetSource(3L)
//                , ""
//                , new HashSet<>()
//                , new UserSource(4L)));
//
//        this.billRepo.save(new BillSnapshot(
//                null
//                , monthYear
//                , "5"
//                , new PayeeSource(1L)
//                , new AccountSource(3L)
//                , new BudgetSource(3L)
//                , ""
//                , new HashSet<>()
//                , new UserSource(4L)));

//        system under test
        var toTest = new BillService(null, this.billQueryRepo, null, null, null);

//        when
        var result = toTest.getBillCountByMonthYearAndUserId(monthYear, userId);

//        then
        assertEquals(6, result);
    }

//    int getBillCountByMonthYearAndUserId(final LocalDate monthYear, final Long userId)
//    {
//        var startDate = Utils.getMonthYearStartDate(monthYear);
//        var endDate = Utils.getMonthYearEndDate(monthYear);
//
//        return this.billQueryRepo.findBillCountBetweenDatesAndUserId(startDate, endDate, userId);
//    }
//
//    BillDTO getBillByNumberAndUserIdAsDto(final String number, final Long userId)
//    {
//        return toDto(getBillByNumberAndUserId(number, userId));
//    }

    @Test
    void existsByNumberAndUserId_shouldReturnTrueWhenBillIsFoundInDb()
    {
//        given
        var billNumber = "202312_4";
        var userId = 2L;

//        system under test
        var toTest = new BillService(null,this.billQueryRepo, null, null, null);

//        when
        var result = toTest.existsByNumberAndUserId(billNumber, userId);

//        then
        assertTrue(result);
    }

    @Test
    void existsByNumberAndUserId_shouldReturnFalseWhenBillIsNotFoundInDb()
    {
//        given
        var billNumber = "201312_4";
        var userId = 2L;

//        system under test
        var toTest = new BillService(null, this.billQueryRepo, null, null, null);

//        when
        var result = toTest.existsByNumberAndUserId(billNumber, userId);

//        then
        assertFalse(result);
    }

    @Test
    void getBillByNumberAndUserId_shouldThrowExceptionWhenBilIsNotFoundInDb()
    {
//        given
        var billNumber = "1998_5";
        var userId = 2L;

//        system under test
        var toTest =  new BillService(null, this.billQueryRepo, null, null, null);

//        when
        var result = catchThrowable(() -> toTest.getBillByNumberAndUserId(billNumber, userId));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("number not found");
    }

    @Test
    void getBillByNumberAndUserId_shouldReturnBillWhenBilIsFoundInDb()
    {
//        given
        var billNumber = "202312_1";
        var userId = 2L;

//        system under test
        var toTest =  new BillService(null, this.billQueryRepo, null, null, null);

//        when
        var result = toTest.getBillByNumberAndUserId(billNumber, userId);

//        then
        assertNotNull(result);
        assertInstanceOf(BillSnapshot.class, result);
    }

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

    @Test
    void updateBudgetPositionInBillPositionById_shouldSetBudgetPositionToGivenInGivenBillSet()
    {
//        given
        var billPositionId1 = 7L;
        var billPositionId2 = 8L;
        var billPositionId3 = 9L;
        var billPositionIds = Set.of(billPositionId1, billPositionId2, billPositionId3);

        var budgetPositionId = 5L;

//        system under test
        var toTest = new BillService(this.billRepo, null, null, null, null);

//        when
        toTest.updateBudgetPositionInBillPositionById(budgetPositionId, billPositionIds);

//        then
//        TODO poprawić wyjście z optionala
        assertEquals(budgetPositionId, this.billQueryRepo.findPositionById(billPositionId1).get().getBudgetPosition().getId());
    }

//    public void updateBudgetPositionInBillPositionById(final Long budgetPositionId, final Set<Long> billPositionIds)
//    {
//        this.billRepo.updateBudgetPositionInBillPositionByIds(budgetPositionId, billPositionIds);
//    }

    @Test
    void getBillPositionIdsByBudgetPositionId_shouldReturnEmptyListWhenNoBillsWithAssignedGivenBudgetPositionIdFound()
    {
//        given
        var budgetPositionId = 8L;

//        system under test
        var toTest = new BillService( null, this.billQueryRepo,null, null, null);

//        when
        var result = toTest.getBillPositionIdsByBudgetPositionId(budgetPositionId);

//        then
        assertTrue(result.isEmpty());
    }
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

    @Test
    void getBillsByMonthYearAndUserId_shouldReturnBillsListForGivenMonthYear()
    {
//        given
        var monthYear = LocalDate.of(2012, 12, 11);
        var userId = 4L;

//        system under test
        var toTest = new BillService(null,this.billQueryRepo, null, null, null);

//        when
        var result = toTest.getBillsByMonthYearAndUserId(monthYear, userId);

//        then
        assertEquals(6, result.size());
    }

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
