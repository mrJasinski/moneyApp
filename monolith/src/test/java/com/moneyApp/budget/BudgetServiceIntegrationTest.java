package com.moneyApp.budget;

import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.vo.UserSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class BudgetServiceIntegrationTest
{
    @Autowired
    private BudgetRepository budgetRepo;

    @Autowired
    private BudgetQueryRepository budgetQueryRepo;

//
//    BudgetSnapshot prepareBudget(final BudgetSnapshot budget)
//    {
//        var userId = budget.getUserId();
//        var monthYear = budget.getMonthYear();
//
//        this.billService.updateBudgetInBillsByMonthYearAndUserId(monthYear, budget.getId(), userId);
//
//        var billPositionsDto = this.billService.getBillPositionsWithoutBudgetPositionByMonthYearAndUserIdAsDto(monthYear, userId);
//
//        if (!billPositionsDto.isEmpty())
//        {
//            var categoriesIdsFromBudgetPositions = extractCategoriesIdsFromBudgetPositions(budget.getPositions());
//
//            var map = new HashMap<Long, Set<Long>>();
//
//            for (BillPositionDTO bp : billPositionsDto)
//            {
//                var billPositionCategoryId = bp.getCategoryId();
//
//                if (!categoriesIdsFromBudgetPositions.contains(billPositionCategoryId))
//                {
//                    var budPos = new BudgetPositionSnapshot(
//                            0L
//                            , new CategorySource(billPositionCategoryId)
//                            , 0d
//                            , ""
//                            , budget
//                            , Set.of(new BillPositionSource(bp.getId())));
//
//                    budget.addPosition(budPos);
//
//                    this.budgetRepo.save(budPos);
//                }
//
//                updateBudgetPositionsMap(budget.getPositions(), bp, map);
//            }
//
//            map.forEach(this.billService::updateBudgetPositionInBillPositionById);
//        }
//
//        return budget;
//    }
//
//    BudgetSnapshot getBudgetByNumberAndUserId(String number, Long userId)
//    {
//        var monthYear = getBudgetMonthYearByNumber(number);
//
//        return getBudgetByMonthYearAndUserId(monthYear, userId);
//    }


    @Test
    void getBudgetByMonthYearAndUserId_shouldThrowExceptionWhenBudgetNotFound()
    {
//        given
        var monthYear = LocalDate.of(1974, 11, 1);
        var userId = 99L;

//        system under test
        var toTest = new BudgetService(null, this.budgetQueryRepo, null, null);

//        when
        var result = catchThrowable(() -> toTest.getBudgetByMonthYearAndUserId(monthYear, userId));

//        then
        assertInstanceOf(IllegalArgumentException.class, result);
    }

    @Test
    void getBudgetByMonthYearAndUserId_shouldReturnBudgetForGivenMonthYearAndUserId()
    {
//        given
        var monthYear = LocalDate.now();
        var userId = 1L;
        this.budgetRepo.save(new BudgetSnapshot(0L, LocalDate.now(), "", new UserSource(1L), new HashSet<>()));

//        system under test
        var toTest = new BudgetService(null, this.budgetQueryRepo, null, null);

//        when
        var result = toTest.getBudgetByMonthYearAndUserId(monthYear, userId);

//        then
        assertEquals(LocalDate.now(), result.getMonthYear());
    }

    @Test
    void getBudgetsByUserId_shouldReturnListOfAllBudgetsForGivenUser()
    {
//        given
        this.budgetRepo.save(new BudgetSnapshot(0L, LocalDate.now(), "", new UserSource(6L), new HashSet<>()));
        this.budgetRepo.save(new BudgetSnapshot(0L, LocalDate.now(), "", new UserSource(6L), new HashSet<>()));

//        system under test
        var toTest = new BudgetService(null, this.budgetQueryRepo, null, null);

//        when
        var result = toTest.getBudgetsByUserId(6L);

//        then
        assertEquals(2, result.size());
    }

//    BudgetDTO toDto(BudgetSnapshot budget)
//    {
//        var catIds = extractCategoriesIdsFromBudgetPositions(budget.getPositions());
//
//        var categories = this.categoryService.getCategoriesByIdsAsDto(catIds);
//
//        var billPosIds = new ArrayList<Long>();
//
//        budget.getPositions().forEach(p -> p.addBillPositionSources(this.billService.getBillPositionSourcesByBudgetPositionId(p.getId())));
//
//        for (BudgetPositionSnapshot bp : budget.getPositions())
//            billPosIds.addAll(bp.getBillPositions()
//                    .stream()
//                    .map(BillPositionSource::getId)
//                    .toList());
//
//        var budPos = this.billService.getBudgetPositionsIdsWithSumsByBillPositionIds(billPosIds);
//
//        return new BudgetDTO(
//                budget.getMonthYear()
//                , budget.getDescription()
//                , budget.getPositions()
//                    .stream()
//                    .map(p -> new BudgetPositionDTO(
//                            categories.stream().filter(c -> p.getCategory().getId().equals(c.getId())).toList().get(0)
//                            , p.getPlannedAmount()
//                            , budPos.stream().filter( b -> p.getId().equals(b.getBudgetPositionId())).toList().get(0).getSum()
//                            , p.getDescription()
//                    ))
//                    .toList());
//    }
//
//    public BudgetDTO getBudgetByDateAndUserIdAsDto(LocalDate date, Long userId)
//    {
//        var monthYear = convertDateToMonthYear(date);
//
//        try
//        {
//            var budget = getBudgetByMonthYearAndUserId(monthYear, userId);
//
//            return prepareBudgetAsDto(budget);
//        }
//        catch (IllegalArgumentException ex)
//        {
//            return new BudgetDTO();
//        }
//    }

    @Test
    void createBudgetByUserId_shouldThrowExceptionIfBudgetForGivenMonthAlreadyExists()
    {
//        given
        var monthYear = LocalDate.of(2021, 12, 1);
        var toSave = new BudgetDTO(monthYear, "");

        var userId = 6L;
        this.budgetRepo.save(new BudgetSnapshot(0L, monthYear, "", new UserSource(userId), new HashSet<>()));

//        system under test
        var toTest = new BudgetService(null, this.budgetQueryRepo, null, null);

//        when
        var result = catchThrowable(() -> toTest.createBudgetByUserId(toSave, userId));

//        then
        assertInstanceOf(IllegalArgumentException.class, result);
    }

    @Test
    void createBudgetByUserId_shouldReturnCreatedBudget()
    {
//        given
        var monthYear = LocalDate.of(2019, 10, 1);
        var toSave = new BudgetDTO(monthYear, "");

//        system under test
        var toTest = new BudgetService(this.budgetRepo, this.budgetQueryRepo, null, null);

//        when
        var result = toTest.createBudgetByUserId(toSave, 1L);

//        then
        assertEquals(monthYear, result.getMonthYear());
    }
//
//    void deleteBudgetByNumberAndUserId(final String number, final Long userId)
//    {
//        this.budgetRepo.deleteByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId);
//    }

}