package com.moneyApp.bill;

import com.moneyApp.account.AccountService;
import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.category.CategoryService;
import com.moneyApp.category.CategoryType;
import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.payee.PayeeRole;
import com.moneyApp.payee.PayeeService;
import com.moneyApp.payee.dto.PayeeDTO;
import com.moneyApp.utils.Utils;
import com.moneyApp.vo.CategorySource;
import com.moneyApp.vo.PayeeSource;
import com.moneyApp.vo.UserSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

    @Autowired
    private AccountService accountService;

    @Autowired
    private PayeeService payeeService;

    @Autowired
    private CategoryService categoryService;

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

    @Test
    void prepareBillToSave_shouldReturnBillWithoutBudgetWhenNewBillIsSaved()
    {
//        given
        var userId = 5L;
        var toSaveDate = LocalDate.of(2004, 9, 12);

        var count = this.billQueryRepo.findBillCountBetweenDatesAndUserId(Utils.getMonthYearStartDate(toSaveDate), Utils.getMonthYearEndDate(toSaveDate), userId) + 1;
        var number = BillSnapshot.createBillNumber(toSaveDate , count);
        var exists = this.billQueryRepo.existsByNumberAndUserId(number, userId);

        while(exists)
        {
            count = this.billQueryRepo.findBillCountBetweenDatesAndUserId(Utils.getMonthYearStartDate(toSaveDate), Utils.getMonthYearEndDate(toSaveDate), userId) + 1;
            number = BillSnapshot.createBillNumber(toSaveDate , count);
            exists = this.billQueryRepo.existsByNumberAndUserId(number, userId);
            toSaveDate = toSaveDate.plusMonths(1);
        }

        var catDto1 = new CategoryDTO(1L,"Spożywka", "Jedzenie", CategoryType.EXPENSE, "");
        var catDto2 = new CategoryDTO(2L,"Spożywka", "Słodycze", CategoryType.EXPENSE, "");

        if (!this.categoryService.existsByNameAndUserId(catDto1.getName(), userId))
            catDto1 = this.categoryService.createCategoryByUserIdAsDto(catDto1, userId);

        if (!this.categoryService.existsByNameAndUserId(catDto2.getName(), userId))
            catDto2 = this.categoryService.createCategoryByUserIdAsDto(catDto2, userId);

        var gainerDto = new PayeeDTO("Ja", PayeeRole.GAINER);
        var payeeDto = new PayeeDTO("Lidl", PayeeRole.PAYEE);

        if (!this.payeeService.existsByNameAndUserId(gainerDto.getName(), userId))
            gainerDto = this.payeeService.createPayeeByUserIdAsDto(gainerDto, userId);

        if (!this.payeeService.existsByNameAndUserId(payeeDto.getName(), userId))
            payeeDto = this.payeeService.createPayeeByUserIdAsDto(payeeDto, userId);

        var accountDto = new AccountDTO("Millenium_ROR", "", 1000d);

        if (!this.accountService.existsByNameAndUserId(accountDto.getName(), userId))
            accountDto = this.accountService.createAccountByUserIdAsDto(accountDto, userId);

        var position1 = BillPositionDTO.builder()
                .withAmount(8.2)
                .withCategory(catDto1)
                .withGainerName(gainerDto.getName())
                .withDescription("Serek pleśniowy")
                .build();

        var position2 = BillPositionDTO.builder()
                .withAmount(6.8)
                .withCategory(catDto2)
                .withGainerName(gainerDto.getName())
                .withDescription("Czekolada")
                .build();

        var positions = List.of(position1, position2);

        var dto = BillDTO.builder()
                .withDate(toSaveDate)
                .withPayeeName(payeeDto.getName())
                .withAccountName(accountDto.getName())
                .withDescription("")
                .withPositions(positions)
                .build();

//        system under test
        var toTest = new BillService(this.billRepo, this.billQueryRepo, this.categoryService, this.payeeService, this.accountService);

//        when
        var result = toTest.prepareBillToSave(dto, userId);

//        then
        assertEquals(0L, result.getId());
        assertEquals(BillSnapshot.createBillNumber(toSaveDate, count) , result.getNumber());
        assertNull(result.getBudget());
    }

    @Test
    void prepareBillToSave_shouldGetDataFromUpdatedBillWhenMonthYearMatchesIsTrue()
    {
//        given
        var userId = 5L;
        var toSaveDate = LocalDate.of(2004, 9, 12);
        var count = 1;

        var number = BillSnapshot.createBillNumber(toSaveDate, count);

        var catDto1 = new CategoryDTO(1L,"Spożywka", "Jedzenie", CategoryType.EXPENSE, "");
        var catDto2 = new CategoryDTO(2L,"Spożywka", "Słodycze", CategoryType.EXPENSE, "");

        if (!this.categoryService.existsByNameAndUserId(catDto1.getName(), userId))
            catDto1 = this.categoryService.createCategoryByUserIdAsDto(catDto1, userId);

        if (!this.categoryService.existsByNameAndUserId(catDto2.getName(), userId))
            catDto2 = this.categoryService.createCategoryByUserIdAsDto(catDto2, userId);

        var gainerDto = new PayeeDTO("Ja", PayeeRole.GAINER);
        var payeeDto = new PayeeDTO("Lidl", PayeeRole.PAYEE);

        if (!this.payeeService.existsByNameAndUserId(gainerDto.getName(), userId))
            gainerDto = this.payeeService.createPayeeByUserIdAsDto(gainerDto, userId);

        if (!this.payeeService.existsByNameAndUserId(payeeDto.getName(), userId))
            payeeDto = this.payeeService.createPayeeByUserIdAsDto(payeeDto, userId);

        var accountDto = new AccountDTO("Millenium_ROR", "", 1000d);

        if (!this.accountService.existsByNameAndUserId(accountDto.getName(), userId))
            accountDto = this.accountService.createAccountByUserIdAsDto(accountDto, userId);

        BillSnapshot bill;

        if (!this.billQueryRepo.existsByNumberAndUserId(number, userId))
            bill = this.billRepo.save(new BillSnapshot(
                    null
                    , toSaveDate
                    , String.valueOf(count)
                    , null
                    , null
                    , null
                    , null
                    , new HashSet<>()
                    , new UserSource(userId)));
        else
            bill = this.billQueryRepo.findByNumberAndUserId(number, userId).get();

        var position1 = BillPositionDTO.builder()
                .withAmount(8.2)
                .withCategory(catDto1)
                .withGainerName(gainerDto.getName())
                .withDescription("Serek pleśniowy")
                .build();

        var position2 = BillPositionDTO.builder()
                .withAmount(6.8)
                .withCategory(catDto2)
                .withGainerName(gainerDto.getName())
                .withDescription("Czekolada")
                .build();

        var positions = List.of(position1, position2);

        var dto = BillDTO.builder()
                .withDate(toSaveDate)
                .withNumber(String.valueOf(number))
                .withPayeeName(payeeDto.getName())
                .withAccountName(accountDto.getName())
                .withDescription("")
                .withPositions(positions)
                .build();

//        system under test
        var toTest = new BillService(this.billRepo, this.billQueryRepo, this.categoryService, this.payeeService, this.accountService);

//        when
        var result = toTest.prepareBillToSave(dto, userId);

//        then
        assertEquals(bill.getId(), result.getId());
        assertEquals(bill.getNumber()  , result.getNumber());
        assertEquals(bill.getBudget(), result.getBudget());
    }

    @Test
    void prepareBillToSave_shouldChangeBudgetToNullWhenMonthYearMatchesIsFalse()
    {
        var userId = 5L;
        var toSaveDate = LocalDate.of(2004, 9, 12);
        var count = 1;

        var number = BillSnapshot.createBillNumber(toSaveDate, count);

        var catDto1 = new CategoryDTO(1L,"Spożywka", "Jedzenie", CategoryType.EXPENSE, "");
        var catDto2 = new CategoryDTO(2L,"Spożywka", "Słodycze", CategoryType.EXPENSE, "");

        if (!this.categoryService.existsByNameAndUserId(catDto1.getName(), userId))
            catDto1 = this.categoryService.createCategoryByUserIdAsDto(catDto1, userId);

        if (!this.categoryService.existsByNameAndUserId(catDto2.getName(), userId))
            catDto2 = this.categoryService.createCategoryByUserIdAsDto(catDto2, userId);

        var gainerDto = new PayeeDTO("Ja", PayeeRole.GAINER);
        var payeeDto = new PayeeDTO("Lidl", PayeeRole.PAYEE);

        if (!this.payeeService.existsByNameAndUserId(gainerDto.getName(), userId))
            gainerDto = this.payeeService.createPayeeByUserIdAsDto(gainerDto, userId);

        if (!this.payeeService.existsByNameAndUserId(payeeDto.getName(), userId))
            payeeDto = this.payeeService.createPayeeByUserIdAsDto(payeeDto, userId);

        var accountDto = new AccountDTO("Millenium_ROR", "", 1000d);

        if (!this.accountService.existsByNameAndUserId(accountDto.getName(), userId))
            accountDto = this.accountService.createAccountByUserIdAsDto(accountDto, userId);

        BillSnapshot bill;

        if (!this.billQueryRepo.existsByNumberAndUserId(number, userId))
            bill = this.billRepo.save(new BillSnapshot(
                    null
                    , toSaveDate
                    , String.valueOf(count)
                    , null
                    , null
                    , null
                    , null
                    , new HashSet<>()
                    , new UserSource(userId)));
        else
            bill = this.billQueryRepo.findByNumberAndUserId(number, userId).get();

        var position1 = BillPositionDTO.builder()
                .withAmount(8.2)
                .withCategory(catDto1)
                .withGainerName(gainerDto.getName())
                .withDescription("Serek pleśniowy")
                .build();

        var position2 = BillPositionDTO.builder()
                .withAmount(6.8)
                .withCategory(catDto2)
                .withGainerName(gainerDto.getName())
                .withDescription("Czekolada")
                .build();

        var positions = List.of(position1, position2);

        var dto = BillDTO.builder()
                .withDate(toSaveDate)
                .withNumber(String.valueOf(number))
                .withPayeeName(payeeDto.getName())
                .withAccountName(accountDto.getName())
                .withDescription("")
                .withPositions(positions)
                .build();

//        system under test
        var toTest = new BillService(this.billRepo, this.billQueryRepo, this.categoryService, this.payeeService, this.accountService);

//        when
        var result = toTest.prepareBillToSave(dto, userId);

//        then
        assertEquals(bill.getId(), result.getId());
        assertEquals(bill.getNumber()  , result.getNumber());
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

//    BillSnapshot saveBill(BillDTO toSave, Long userId)
//    {
//        return this.billRepo.save(prepareBillToSave(toSave, userId));
//    }

    @Test
    void setBillsCountAsBillNumber_shouldReturnBillCountFromGivenMonthPlusOne()
    {
//        given
        var billDate = LocalDate.of(2012, 6, 12);
        var userId = 4L;

        var count = this.billQueryRepo.findBillCountBetweenDatesAndUserId(Utils.getMonthYearStartDate(billDate), Utils.getMonthYearEndDate(billDate), userId);

//        system under test
        var toTest = new BillService(null, this.billQueryRepo, null, null, null);

//        when
        var result = toTest.setBillsCountAsBillNumber(billDate, userId);

//        then
        assertEquals(String.valueOf(count + 1), result);
    }

    @Test
    void setBillsCountAsBillNumber_shouldReturnOneWhenThereAreNoBillsInGivenMonth()
    {
//        given
        var billDate = LocalDate.of(2008, 6, 12);
        var userId = 4L;

        var count = 1;
        while(count != 0)
        {
            count = this.billQueryRepo.findBillCountBetweenDatesAndUserId(Utils.getMonthYearStartDate(billDate), Utils.getMonthYearEndDate(billDate), userId);
            billDate = billDate.plusMonths(1);
        }

//        system under test
        var toTest = new BillService(null, this.billQueryRepo, null, null, null);

//        when
        var result = toTest.setBillsCountAsBillNumber(billDate, userId);

//        then
        assertEquals("1", result);
    }

//
//    BillDTO updateBillByNumberAndUserAsDto(final BillDTO toUpdate, final Long userId)
//    {
//        return toDto(saveBill(toUpdate, userId));
//    }

    @Test
    void sumBillPositionsAmounts_shouldNotCallCategoryServiceWhenNoPositionsInBillAndReturnZero()
    {
//        given
        var bill = new BillSnapshot(null, null, "0_0", null, null, null, null, new HashSet<>(), null);

//        system under test
        var toTest = new BillService(null, null, null, null, null);

//        when
        var result = toTest.sumBillPositionsAmounts(bill);

//        then
        assertEquals(0, result);
    }

    @Test
    void sumBillPositionsAmounts_shouldReturnMinusSumWhenCategoryTypeIsExpense()
    {
//        given
        var userId = 4L;

        ;

        var cat1 = new CategorySource(this.categoryService.getExemplaryCategoryIdByType(CategoryType.EXPENSE));
        var cat2 = new CategorySource(this.categoryService.getExemplaryCategoryIdByType(CategoryType.EXPENSE));

        var position1 = new BillPositionSnapshot(null, 0, 12d, cat1, null, null ,null);
        var position2 = new BillPositionSnapshot(null, 1, 13d, cat2, null, null ,null);
        var positions = Set.of(position1, position2);

        try
        {
            this.categoryService.getCategoryTypeById(cat1.getId());
        }
        catch (IllegalArgumentException ex)
        {
            this.categoryService.createCategoryByUserIdAsDto(new CategoryDTO("x", "x", CategoryType.EXPENSE), userId);
        }

        try
        {
            this.categoryService.getCategoryTypeById(cat2.getId());
        }
        catch (IllegalArgumentException ex)
        {
            this.categoryService.createCategoryByUserIdAsDto(new CategoryDTO("x", "x", CategoryType.EXPENSE), userId);
        }

        var bill = new BillSnapshot(null, null, "0_0", null, null, null, null, positions, null);

//        system under test
        var toTest = new BillService(null, null, this.categoryService, null, null);

//        when
        var result = toTest.sumBillPositionsAmounts(bill);

//        then
        assertTrue(result < 0);
        assertEquals(-bill.getBillSum(), result);
    }

    @Test
    void sumBillPositionsAmounts_shouldReturnSumWhenCategoryTypeIsExpense()
    {
//        given
        var userId = 4L;

        var cat1 = new CategorySource(this.categoryService.getExemplaryCategoryIdByType(CategoryType.INCOME));
        var cat2 = new CategorySource(this.categoryService.getExemplaryCategoryIdByType(CategoryType.INCOME));

        var position1 = new BillPositionSnapshot(null, 0, 12d, cat1, null, null ,null);
        var position2 = new BillPositionSnapshot(null, 1, 13d, cat2, null, null ,null);
        var positions = Set.of(position1, position2);

        try
        {
            this.categoryService.getCategoryTypeById(cat1.getId());
        }
        catch (IllegalArgumentException ex)
        {
            this.categoryService.createCategoryByUserIdAsDto(new CategoryDTO("y", "y", CategoryType.INCOME), userId);
        }

        try
        {
            this.categoryService.getCategoryTypeById(cat2.getId());
        }
        catch (IllegalArgumentException ex)
        {
            this.categoryService.createCategoryByUserIdAsDto(new CategoryDTO("z", "z", CategoryType.INCOME), userId);
        }

        var bill = new BillSnapshot(null, null, "0_0", null, null, null, null, positions, null);

//        system under test
        var toTest = new BillService(null, null, this.categoryService, null, null);

//        when
        var result = toTest.sumBillPositionsAmounts(bill);

//        then
        assertTrue(result > 0);
        assertEquals(bill.getBillSum(), result);
    }

    @Test
    void updateAccountBalanceByBillSum_shouldNotCallAccountServiceWhenBillsDifferenceIsZero()
    {
//        given
        var billSum = 12d;
        var accountId = 1L;

//        system under test
        var toTest = new BillService(null, null ,null, null, null);

//        when
        toTest.updateAccountBalanceByBillSum(billSum, billSum, accountId);

//        then
//        no assertion as method is void and nothing should happen
    }

    @Test
    void updateAccountBalanceByBillSum_shouldIncreaseAccountBalanceWhenBillSumIsBiggerThanOldBillSum()
    {
//        given
        var billSum = 150d;
        var oldBillSum = 100d;

        var accountId = this.accountService.getExemplaryAccountId();
        var balance = this.accountService.getAccountBalanceByAccountId(accountId);

//        system under test
        var toTest = new BillService(null, null ,null, null, this.accountService);

//        when
        toTest.updateAccountBalanceByBillSum(billSum, oldBillSum, accountId);

        var actualBalance = this.accountService.getAccountBalanceByAccountId(accountId);

//        then
        assertTrue(balance < actualBalance);
        assertEquals(balance + (billSum - oldBillSum), actualBalance);
    }

    @Test
    void updateAccountBalanceByBillSum_shouldDecreaseAccountBalanceWhenBillSumIsLowerThanOldBillSum()
    {
//        given
        var billSum = 150d;
        var oldBillSum = 200d;

        var accountId = this.accountService.getExemplaryAccountId();
        var balance = this.accountService.getAccountBalanceByAccountId(accountId);

//        system under test
        var toTest = new BillService(null, null ,null, null, this.accountService);

//        when
        toTest.updateAccountBalanceByBillSum(billSum, oldBillSum, accountId);

        var actualBalance = this.accountService.getAccountBalanceByAccountId(accountId);

//        then
        assertTrue(balance > actualBalance);
        assertEquals(balance + (billSum - oldBillSum), actualBalance);
    }


//    void deleteBillByNumberAndUserId(final String number, final Long userId)
//    {
//        this.billRepo.deleteByNumberAndUserId(number, userId);
//    }


//    List<BillSnapshot> getBillsByUserId(Long userId)
//    {
//        return this.billQueryRepo.findByUserId(userId);
//    }


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
        var toTest = new BillService(null, this.billQueryRepo, null, null, null);

//        when
        var result = toTest.getBillCountByMonthYearAndUserId(monthYear, userId);

//        then
        assertEquals(0, result);
    }

    @Test
    void getBillCountByMonthYearAndUserId_shouldReturnCountOfBillsFound()
    {
////        given
//        var monthYear = LocalDate.of(2012, 12, 11);
//        var userId = 4L;

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

////        system under test
//        var toTest = new BillService(null, this.billQueryRepo, null, null, null);
//
////        when
//        var result = toTest.getBillCountByMonthYearAndUserId(monthYear, userId);
//
////        then
//        assertEquals(6, result);
    }

    @Test
    void getBillCountByMonthYearAndUserId_shouldReturnAmountOFBillsInGivenMonth()
    {
//        given
        var monthYear = LocalDate.of(2009, 4, 20);
        var count = 3;
        var userId = 2L;

        var startDate = Utils.getMonthYearStartDate(monthYear);
        var endDate = Utils.getMonthYearEndDate(monthYear);

        if (this.billQueryRepo.findBillCountBetweenDatesAndUserId(startDate, endDate, userId) == 0)
        {
            for (int i = 0; i < 20; i++)
                this.billRepo.save(new BillSnapshot(
                        null
                        , monthYear
                        , String.valueOf(count)
                        , null
                        , null
                        , null
                        , null
                        , new HashSet<>()
                        , new UserSource(userId)));
        }

        var billCount = this.billQueryRepo.findByUserId(userId)
                .stream()
                .filter(b -> b.getBillDate().isAfter(startDate) && b.getBillDate().isBefore(endDate))
                .toList()
                .size();

//        system under test
        var toTest = new BillService(null, this.billQueryRepo,null, null, null);

//        when
        var result = toTest.getBillCountByMonthYearAndUserId(monthYear, userId);

//        then
        assertEquals(billCount, result);
    }

    @Test
    void getBillCountByMonthYearAndUserId_shouldReturnZeroWhenThereAreNoBillsInGivenMonth()
    {
//        given
        var monthYear = LocalDate.of(2009, 4, 20);
        var userId = 8L;

        var startDate = Utils.getMonthYearStartDate(monthYear);
        var endDate = Utils.getMonthYearEndDate(monthYear);

        while(!this.billQueryRepo.findByDatesAndUserId(startDate, endDate, userId).isEmpty())
        {
            monthYear = monthYear.plusMonths(1);
            startDate = Utils.getMonthYearStartDate(monthYear);
            endDate = Utils.getMonthYearEndDate(monthYear);
        }

//        system under test
        var toTest = new BillService(null, this.billQueryRepo,null, null, null);

//        when
        var result = toTest.getBillCountByMonthYearAndUserId(monthYear, userId);

//        then
        assertEquals(0 , result);
    }

//    int getBillCountByMonthYearAndUserId(final LocalDate monthYear, final Long userId)
//    {
//        var startDate = Utils.getMonthYearStartDate(monthYear);
//        var endDate = Utils.getMonthYearEndDate(monthYear);
//
//        return this.billQueryRepo.findBillCountBetweenDatesAndUserId(startDate, endDate, userId);
//    }

//    BillDTO getBillByNumberAndUserIdAsDto(final String number, final Long userId)
//    {
//        return toDto(getBillByNumberAndUserId(number, userId));
//    }

    @Test
    void existsByNumberAndUserId_shouldReturnTrueWhenBillIsFoundInDb()
    {
//        given
        var userId = 2L;
        var date = LocalDate.of(2023, 12, 1);
        var count = 4;
        var billNumber = BillSnapshot.createBillNumber(date, count);

        if (!this.billQueryRepo.existsByNumberAndUserId(billNumber, userId))
            this.billRepo.save(new BillSnapshot(
                    null
                    , date
                    , String.valueOf(count)
                    , null
                    , null
                    , null
                    , null
                    , new HashSet<>()
                    , new UserSource(userId)));

//        system under test
        var toTest = new BillService(null, this.billQueryRepo, null, null, null);

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

        if (this.billQueryRepo.existsByNumberAndUserId(billNumber, userId))
            this.billRepo.deleteByNumberAndUserId(billNumber, userId);

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

        if (this.billQueryRepo.existsByNumberAndUserId(billNumber, userId))
            this.billRepo.deleteByNumberAndUserId(billNumber, userId);

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

        if (!this.billQueryRepo.existsByNumberAndUserId(billNumber, userId))
            this.billRepo.save(new BillSnapshot(
                    null
                    , LocalDate.of(2023, 12, 1)
                    , "1"
                    , null
                    , null
                    , null
                    , null
                    , new HashSet<>()
                    , new UserSource(userId)));

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
        var billPositionIds = List.of(billPositionId1, billPositionId2, billPositionId3);

        var budgetPositionId = 5L;

//        system under test
        var toTest = new BillService(this.billRepo, null, null, null, null);

//        when
        toTest.updateBudgetPositionInBillPositionById(budgetPositionId, billPositionIds);

//        then
//      used optional.get() since it's 100% sure that it exists
        assertEquals(budgetPositionId, this.billQueryRepo.findPositionById(billPositionId1).get().getBudgetPositionId());
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
