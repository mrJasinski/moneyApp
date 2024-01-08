package com.moneyApp.budget;

import com.moneyApp.bill.BillService;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import com.moneyApp.category.CategoryService;
import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.vo.CategorySource;
import com.moneyApp.vo.UserSource;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


class BudgetServiceUnitTest
{
    @Test
    void prepareBudget_shouldReturnBudgetWithSamePositionsAmountWhenNoBillPositionsWithoutBudgetPositionIdFoundForGivenBudget()
    {
//        given
        var positions = Set.of(new BudgetPositionSnapshot());

        var budget = new BudgetSnapshot(5L, LocalDate.now(), "", new UserSource(2L), positions);

        var mockBillService = mock(BillService.class);
        given(mockBillService.getBillPositionsWithoutBudgetPositionByMonthYearAndUserIdAsDto(any(), anyLong())).willReturn(new ArrayList<>());

//        system under test
        var toTest = new BudgetService(null, null, mockBillService, null);

//        when
        var result = toTest.prepareBudget(budget);

//        then
        assertEquals(1, result.getPositions().size());
    }
//gdy catId jest takie samo powinna się zwiększyć ilość billPos w stosownej budPos
    @Test
    void prepareBudget_shouldReturnBudgetWithSamePositionsAmountAndIncreaseBillPositionsInBudgetPositionsWhenBillPositionsWithoutBudgetPositionIdFoundForGivenBudgetAndBudgetDoesHavePositionsForAllBillsCategories()
    {

    }
//    jeśli jest inne to powinno się zwiększyć ilość budPos
    @Test
    void prepareBudget_shouldReturnBudgetWithIncreasedPositionsAmountWhenBillPositionsWithoutBudgetPositionIdFoundForGivenBudgetAndBudgetDoesNotHavePositionsForAllBillsCategories()
    {
//        given
        var budget = new BudgetSnapshot(5L, LocalDate.now(), "", new UserSource(2L), new HashSet<>());

        var positions = Set.of(new BudgetPositionSnapshot(2L, new CategorySource(1L), 0d, "", null, new HashSet<>()));

        budget.addPositions(positions);

        var billPosition1 = BillPositionDTO.builder()
                .withId(2L)
                .withAmount(10d)
                .withDate(LocalDate.now())
                .withCategory(new CategoryDTO(8L, ""))
                .build();

        var billPositions = List.of(billPosition1);

        var mockBudgetRepo = mock(BudgetRepository.class);

        var mockBillService = mock(BillService.class);
        given(mockBillService.getBillPositionsWithoutBudgetPositionByMonthYearAndUserIdAsDto(any(), anyLong())).willReturn(billPositions);

//        system under test
        var toTest = new BudgetService(mockBudgetRepo, null, mockBillService, null);

//        when
        var result = toTest.prepareBudget(budget);

//        then
        assertEquals(2, result.getPositions().size());
    }


//    BudgetSnapshot prepareBudget(final BudgetSnapshot budget)
//    {
//        var userId = budget.getUser().getId();
//        var monthYear = budget.getMonthYear();
//
//        this.billService.updateBudgetInBillsByMonthYearAndUserId(monthYear, budget.getId(), userId);
//
//        var billPositionsDto = this.billService.getBillPositionsWithoutBudgetPositionByMonthYearAndUserIdAsDto(monthYear, userId);
//
//        if (!billPositionsDto.isEmpty())
//        {
//            var categoriesIdFromBudgetPositions = budget.getPositions()
//                                                    .stream()
//                                                    .map(BudgetPositionSnapshot::getCategory)
//                                                    .map(CategorySource::getId)
//                                                    .toList();
//
//            var map = new HashMap<Long, List<Long>>();
//
//            for (BillPositionDTO bp : billPositionsDto)
//            {
//                var billPositionCategoryId = bp.getCategory().getId();
//
//                if (categoriesIdFromBudgetPositions.contains(billPositionCategoryId))
//                {
//                    var budgetPosition = budget.getPositions()
//                            .stream()
//                            .filter(p -> p.getCategory().getId().equals(billPositionCategoryId)).
//                            toList()
//                            .get(0);
//
//                    var billPositionId = bp.getId();
//
//                    budgetPosition.addBillPositionSource(new BillPositionSource(billPositionId));
//
//                    var budgetPositionId = budgetPosition.getId();
//
//                    if (!map.containsKey(budgetPositionId))
//                        map.put(budgetPositionId, new ArrayList<>(List.of(billPositionId)));
//                    else
//                        map.get(budgetPositionId).add(billPositionId);
//                }
//                else
//                    budget.addPosition(new BudgetPositionSnapshot(
//                            0L
//                            , new CategorySource(billPositionCategoryId)
//                            , 0d
//                            , ""
//                            , budget
//                            , Set.of(new BillPositionSource(bp.getId()))
//                    ));
//            }
//
//            var positionsToSave = budget.getPositions()
//                                    .stream()
//                                    .filter(p -> p.getId() < 1)
//                                    .toList();
//
//            positionsToSave.forEach(this::saveBudgetPositionInDb);
//
//            map.forEach(this.billService::updateBudgetPositionInBillPositionById);
//        }
//
//        return budget;
//    }

    @Test
    void getBudgetByNumberAndUserId_shouldThrowExceptionWhenBudgetForGivenNumberNotFound()
    {
//        given
        var number = "012024";
        var userId = 1L;

        var mockBudgetQueryRepo = mock(BudgetQueryRepository.class);
        given(mockBudgetQueryRepo.findByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.empty());

//        system under test
        var toTest = new BudgetService(null, mockBudgetQueryRepo, null, null);

//        when
        var result = catchThrowable(() -> toTest.getBudgetByNumberAndUserId(number, userId));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("monthYear and user id not found");
    }

    @Test
    void getBudgetByNumberAndUserId_shouldReturnBudgetFoundForGivenNumber()
    {
//        given
        var number = "012024";
        var userId = 2L;

        var mockBudgetQueryRepo = mock(BudgetQueryRepository.class);
        given(mockBudgetQueryRepo.findByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.of(new BudgetSnapshot()));

//        system under test
        var toTest = new BudgetService(null, mockBudgetQueryRepo, null, null);

//        when
        var result = toTest.getBudgetByNumberAndUserId(number, userId);

//        then
        assertNotNull(result);
        assertThat(result)
                .isInstanceOf(BudgetSnapshot.class);
    }

    @Test
    void getBudgetByMonthYearAndUserId_shouldThrowExceptionWhenBudgetNotFound()
    {
//        given
        var number = "022024";
        var userId = 6L;

        var mockBudgetQueryRepo = mock(BudgetQueryRepository.class);
        given(mockBudgetQueryRepo.findByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.empty());

//        system under test
        var toTest = new BudgetService(null, mockBudgetQueryRepo, null, null);

//        when
        var result = catchThrowable(() -> toTest.getBudgetByNumberAndUserId(number, userId));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("monthYear and user id not found");
    }

    @Test
    void getBudgetByMonthYearAndUserId_shouldReturnBudgetSnapshotWhenFound()
    {
//        given
        var number = "022024";
        var userId = 6L;

        var mockBudgetQueryRepo = mock(BudgetQueryRepository.class);
        given(mockBudgetQueryRepo.findByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.of(new BudgetSnapshot()));

//        system under test
        var toTest = new BudgetService(null, mockBudgetQueryRepo, null, null);

//        when
        var result = toTest.getBudgetByNumberAndUserId(number, userId);

//        then
        assertThat(result)
                .isInstanceOf(BudgetSnapshot.class);
        assertNotNull(result);
    }

    @Test
    void getBudgetMonthYearByNumber_shouldThrowExceptionIfBudgetNumberContainsOtherCharsThanDigits()
    {
//        given
        var number = "1xx456";

//        system under test
        var toTest = new BudgetService(null, null, null, null);

//        when
        var result = catchThrowable(() -> toTest.getBudgetMonthYearByNumber(number));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("digits only");
    }

    @Test
    void getBudgetMonthYearByNumber_shouldThrowExceptionIfBudgetNumberHasLengthOtherThanSixChars()
    {
//        given
        var number1 = "1234567";
        var number2 = "12345";

//        system under test
        var toTest = new BudgetService(null, null, null, null);

//        when
        var result1 = catchThrowable(() -> toTest.getBudgetMonthYearByNumber(number1));
        var result2 = catchThrowable(() -> toTest.getBudgetMonthYearByNumber(number2));

//        then
        assertThat(result1)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("six chars");

        assertThat(result2)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("six chars");
    }

    @Test
    void getBudgetMonthYearByNumber_shouldReturnLocalDateForProperBudgetNumber()
    {
//        given
        var number = "012024";

//        system under test
        var toTest = new BudgetService(null, null, null, null);

//        when
        var result = toTest.getBudgetMonthYearByNumber(number);

//        then
        assertEquals(LocalDate.of(2024, 1, 1), result);
    }

//    BudgetDTO toDto(BudgetSnapshot budget)
//    {
//        var catIds = budget.getPositions().stream().map(BudgetPositionSnapshot::getCategory).map(CategorySource::getId).toList();
//
//        var categories = this.categoryService.getCategoriesByIdsAsDto(catIds);
//
//        var billPosIds = new ArrayList<Long>();
//
//        budget.getPositions().forEach(p -> p.addBillPositionSources(this.billService.getBillPositionSourcesByBudgetPositionId(p.getId())));
//
//        for (BudgetPositionSnapshot bp : budget.getPositions())
//                for (BillPositionSource b : bp.getBillPositions())
//                    billPosIds.add(b.getId());
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

    @Test
    void getBudgetByDateAndUserIdAsDto_shouldReturnEmptyBudgetDtoWhenExceptionIsThrown()
    {
//        given
        var mockBudgetQueryRepo = mock(BudgetQueryRepository.class);
        given(mockBudgetQueryRepo.findByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.empty());

//        system under test
        var toTest = new BudgetService(null, mockBudgetQueryRepo, null, null);

//        when
        var result = toTest.getBudgetByDateAndUserIdAsDto(LocalDate.now(), 3L);

//        then
        assertNull(result.getNumber());
        assertNull(result.getMonthYear());
    }

    @Test
    void getBudgetByDateAndUserIdAsDto_shouldReturnPreparedBudgetDtoWhenExceptionIsNotThrown()
    {
//        given
        var budget = new BudgetSnapshot(3L, LocalDate.now(), "foo", new UserSource(5L), new HashSet<>());

        var mockBudgetRepo = mock(BudgetRepository.class);
        given(mockBudgetRepo.save((BudgetSnapshot) any())).willReturn(budget);

        var mockBudgetQueryRepo = mock(BudgetQueryRepository.class);
        given(mockBudgetQueryRepo.findByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.of(budget));

        var mockBillService = mock(BillService.class);
        given(mockBillService.getBillPositionSourcesByBudgetPositionId(anyLong())).willReturn(new HashSet<>());
        given(mockBillService.getBudgetPositionsIdsWithSumsByBillPositionIds(any())).willReturn(new HashSet<>());

        var mockCategoryService = mock(CategoryService.class);
        given(mockCategoryService.getCategoriesByIdsAsDto(any())).willReturn(new ArrayList<>());

//        system under test
        var toTest =  new BudgetService(mockBudgetRepo, mockBudgetQueryRepo, mockBillService, mockCategoryService);

//        when
        var result = toTest.getBudgetByDateAndUserIdAsDto(LocalDate.now(), 5L);

//        then
        assertNotNull(result.getMonthYear());
        if (LocalDate.now().getMonthValue() < 10)
            assertEquals(String.format("0%s%s", LocalDate.now().getMonthValue(), LocalDate.now().getYear()), result.getNumber());
        else
            assertEquals(String.format("%s%s", LocalDate.now().getMonthValue(), LocalDate.now().getYear()), result.getNumber());
        assertEquals("foo", result.getDescription());
    }

    @Test
    void convertDateToMonthYear_shouldSetDayOfMonthToOneIfDayOfMonthFromGivenDateIsDifferentThanOne()
    {
//        given
        var date = LocalDate.of(2024, 1, 7);

//        system under test
        var toTest = new BudgetService(null, null, null, null);

//        when
        var result = toTest.convertDateToMonthYear(date);

//        then
        assertEquals(1, result.getDayOfMonth());
    }

    @Test
    void createBudgetByUserIdAsDto_shouldThrowExceptionIfBudgetForGivenMonthAlreadyExists()
    {
//        given
        var mockBudgetQueryRepo = mock(BudgetQueryRepository.class);
        given(mockBudgetQueryRepo.existsByMonthYearAndUserId(any(), anyLong())).willReturn(true);

//        system under test
        var toTest = new BudgetService(null, mockBudgetQueryRepo, null, null);

//        when
        var result = catchThrowable(() -> toTest.createBudgetByUserIdAsDto(new BudgetDTO(LocalDate.now(), ""), 1L));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void createBudgetByUserIdAsDto_shouldReturnCreatedBudgetAsDtoIfBudgetForGivenMonthDoesNotExist()
    {
//        given
        var budget = new BudgetSnapshot(1L, LocalDate.now(), "", new UserSource(2L), new HashSet<>());

        var mockBudgetRepo = mock(BudgetRepository.class);
        given(mockBudgetRepo.save((BudgetSnapshot) any())).willReturn(budget);

        var mockBudgetQueryRepo = mock(BudgetQueryRepository.class);
        given(mockBudgetQueryRepo.existsByMonthYearAndUserId(any(), anyLong())).willReturn(false);

        var mockBillService = mock(BillService.class);
        given(mockBillService.getBillPositionSourcesByBudgetPositionId(anyLong())).willReturn(new HashSet<>());
        given(mockBillService.getBudgetPositionsIdsWithSumsByBillPositionIds(any())).willReturn(new HashSet<>());

        var mockCategoryService = mock(CategoryService.class);
        given(mockCategoryService.getCategoriesByIdsAsDto(any())).willReturn(new ArrayList<>());

//        system under test
        var toTest = new BudgetService(mockBudgetRepo, mockBudgetQueryRepo, mockBillService, mockCategoryService);

//        when
        var result = toTest.createBudgetByUserIdAsDto(new BudgetDTO(LocalDate.now(), ""), 2L);

//        then
        assertNotNull(result);
        assertThat(result)
                .isInstanceOf(BudgetDTO.class);
    }

}