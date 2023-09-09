package com.moneyApp.budget.service;

import com.moneyApp.budget.Budget;
import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.repository.BudgetPositionRepository;
import com.moneyApp.budget.repository.BudgetRepository;
import com.moneyApp.category.CategoryType;
import com.moneyApp.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class BudgetServiceUnitTest
{
//    public BudgetDTO getBudgetByNumberAndUserEmailAsDto(String number, String email)
//    {
//        var userId = this.userService.getUserIdByEmail(email);
//        var dto = getBudgetByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId).toDto();
//
//        getBudgetDtoAmounts(dto, userId);
//
//        return dto;
//    }

    @Test
    void getBudgetByMonthYearAndUserId_shouldReturnBudgetIfFoundInDb()
    {
//        given
        var mockBudgetRepo = mock(BudgetRepository.class);
        given(mockBudgetRepo.findByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.of(new Budget(LocalDate.now(),
                "budget", new User())));

//        system under test
        var toTest = new BudgetServiceImpl(mockBudgetRepo, null, null, null);

//        when
        var result = toTest.getBudgetByMonthYearAndUserId(LocalDate.now(), 5L);

//        then
        assertNotNull(result);
        assertThat(result).isInstanceOf(Budget.class);
        assertEquals(LocalDate.now(), result.getMonthYear());
        assertEquals("budget", result.getDescription());
        assertNotNull(result.getUser());
    }

    @Test
    void getBudgetByMonthYearAndUserId_shouldThrowExceptionIfNotFoundInDb()
    {
//        given
        var mockBudgetRepo = mock(BudgetRepository.class);
        given(mockBudgetRepo.findByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.empty());

//        system under test
        var toTest = new BudgetServiceImpl(mockBudgetRepo, null, null, null);

//        when
        var result = catchThrowable(() -> toTest.getBudgetByMonthYearAndUserId(LocalDate.now(), 3L));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No budget for given month and year");
    }
//
//    public void getBudgetDtoAmounts(BudgetDTO dto, Long userId)
//    {
//        var budgetId = getBudgetIdByNumberAndUserId(dto.getNumber(), userId);
//
//        dto.setIncomes(getBudgetPositionsByBudgetIdAndCategoryTypeAsDto(budgetId, CategoryType.INCOME));
//        dto.setExpenses(getBudgetPositionsByBudgetIdAndCategoryTypeAsDto(budgetId, CategoryType.EXPENSE));
//
//        dto.setPlannedIncomes(dto.getIncomes().stream().mapToDouble(BudgetPositionDTO::getPlannedAmount).sum());
//        dto.setPlannedExpenses(dto.getExpenses().stream().mapToDouble(BudgetPositionDTO::getPlannedAmount).sum());
//        dto.setPlannedSum(dto.getPlannedIncomes() - dto.getPlannedExpenses());
//
//        dto.setActualIncomes(dto.getIncomes().stream().mapToDouble(BudgetPositionDTO::getActualAmount).sum());
//        dto.setActualExpenses(dto.getExpenses().stream().mapToDouble(BudgetPositionDTO::getActualAmount).sum());
//        dto.setActualSum(dto.getActualIncomes() - dto.getActualExpenses());
//
//        dto.setIncomesSum(dto.getPlannedIncomes() - dto.getActualIncomes());
//        dto.setExpensesSum(dto.getPlannedExpenses() - dto.getActualExpenses());;
//    }

    @Test
    void getBudgetDtoAmounts_shouldSetIncomesAndExpensesSumsForGivenBudgetDto()
    {
//        TODO wpierw testy metod pośrednich
//        given
        var mockBudgetRepo = mock(BudgetRepository.class);
        given(mockBudgetRepo.findIdByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.of(1L));

        var expenses = List.of(new BudgetPosition());
        var incomes = List.of(new BudgetPosition());

        var mockBudgetPositionRepo = mock(BudgetPositionRepository.class);
        given(mockBudgetPositionRepo.findByBudgetIdAndCategoryType(anyLong(), any())).willReturn(incomes);
        given(mockBudgetPositionRepo.findByBudgetIdAndCategoryType(anyLong(), any())).willReturn(expenses);

        var budget = new BudgetDTO(LocalDate.now(), "foo");

//        system under test
        var toTest = new BudgetServiceImpl(mockBudgetRepo, mockBudgetPositionRepo, null, null);

//        when
        toTest.getBudgetDtoAmounts(budget, 7L);

//        then
        assertTrue(budget.getIncomes().size() > 0);
        assertTrue(budget.getExpenses().size() > 0);

    }

    @Test
    void getBudgetIdByNumberAndUserId_shouldReturnIdIfBudgetFoundInDb()
    {
//        given
        var mockBudgetRepo = mock(BudgetRepository.class);
        given(mockBudgetRepo.findIdByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.of(2L));

//        system under test
        var toTest = new BudgetServiceImpl(mockBudgetRepo, null, null, null);

//        when
        var result = toTest.getBudgetIdByNumberAndUserId("052023", 6L);

//        then
        assertEquals(2L, result);
    }

    @Test
    void getBudgetIdByNumberAndUserId_shouldThrowExceptionIfBudgetNotFoundInDb()
    {
//        given
        var mockBudgetRepo = mock(BudgetRepository.class);
        given(mockBudgetRepo.findIdByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.empty());

//        system under test
        var toTest = new BudgetServiceImpl(mockBudgetRepo, null, null, null);

//        when
        var result = catchThrowable(() -> toTest.getBudgetIdByNumberAndUserId("042023", 1L));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No budget for given number");
    }

    @Test
    void getBudgetMonthYearByNumber_shouldReturnDateWhenCorrectNumberIsGiven()
    {
//        given
        var number = "022023";

//        system under test
        var toTest = new BudgetServiceImpl(null, null, null, null);

//        when
        var result = toTest.getBudgetMonthYearByNumber(number);

//        then
        assertEquals(LocalDate.of(2023, 2, 1), result);

    }

    @Test
    void getBudgetMonthYearByNumber_shouldThrowExceptionWhenWrongNumberIsGiven()
    {
//        given
        var number1 = "0x2023";
        var number2 = "02202312";
        var number3 = "22023";

//        system under test
        var toTest = new BudgetServiceImpl(null, null, null, null);

//        when
        var result1 = catchThrowable(() -> toTest.getBudgetMonthYearByNumber(number1));
        var result2 = catchThrowable(() -> toTest.getBudgetMonthYearByNumber(number2));
        var result3 = catchThrowable(() -> toTest.getBudgetMonthYearByNumber(number3));

//        then
        assertThat(result1)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Wrong budget number");
        assertThat(result2)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Wrong budget number");
        assertThat(result3)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Wrong budget number");
    }

    @Test
    void getBudgetPositionsByBudgetIdAndCategoryType_shouldReturnBudgetPositionsList()
    {
//        given
        var mockBudgetPositionRepo = mock(BudgetPositionRepository.class);
        given(mockBudgetPositionRepo.findByBudgetIdAndCategoryType(anyLong(), any())).willReturn(List.of(new BudgetPosition(),
                new BudgetPosition(), new BudgetPosition()));

//        system under test
        var toTest = new BudgetServiceImpl(null, mockBudgetPositionRepo, null, null);

//        when
        var result = toTest.getBudgetPositionsByBudgetIdAndCategoryType(4L, CategoryType.EXPENSE);

//        then
        assertTrue(result.size() > 0);
        assertEquals(3, result.size());
        assertThat(result.get(0)).isInstanceOf(BudgetPosition.class);
    }

//
//    List<BudgetPositionDTO> getBudgetPositionsByBudgetIdAndCategoryTypeAsDto(long budgetId, CategoryType categoryType)
//    {
//        var userId = getUserIdByBudgetId(budgetId);
//
//        var positions = getBudgetPositionsByBudgetIdAndCategoryType(budgetId, categoryType);
//
//        var categories = this.categoryService.getCategoriesByTypeAndUserId(categoryType, userId);
//
//        var posCategories = positions.stream().map(BudgetPosition::getCategory).toList();
//
//        createBudgetPositionsByCategories(categories, posCategories, positions, budgetId);
//
//        var transactions = this.transactionService.getTransactionsByMonthYearAndUserId(getBudgetMonthYearById(budgetId), userId);
//
//        assignTransactionsToBudgetPositions(transactions, positions, userId);
//
//        return convertBudgetPositionsToDto(positions);
//    }

//
//    List<BudgetPositionDTO> convertBudgetPositionsToDto(List<BudgetPosition> positions)
//    {
//        var dto = new ArrayList<BudgetPositionDTO>();
//
//        positions.forEach(p ->
//        {
//            var pos = p.toDto();
//
//            pos.setActualAmount(p.getTransactions().stream().mapToDouble(Transaction::getAmount).sum());
//            pos.setAmountSum(pos.getPlannedAmount() - pos.getActualAmount());
//
//            dto.add(pos);
//        });
//
//        return dto;
//    }
//
//    void assignTransactionsToBudgetPositions(List<Transaction> transactions, List<BudgetPosition> positions, long userId)
//    {
//        transactions.forEach(t ->
//        {
//            if (t.getPosition() == null)
//                positions.forEach(p ->
//                {
//                    if ((p.getCategory()).equals(t.getCategory()))
//                    {
//                        t.setPosition(p);
//                        this.transactionService.updatePositionIdInDb(t.getId(), p, userId);
//                        p.getTransactions().add(t);
//                    }
//                });
//        });
//    }
//
//    void createBudgetPositionsByCategories(List<Category> categories, List<Category> posCategories,
//                                           List<BudgetPosition> positions, long budgetId)
//    {
//        categories.removeIf(posCategories::contains);
//
//        if (categories.size() != 0)
//        {
//            var budget = getBudgetById(budgetId);
//
//            categories.forEach(c ->
//            {
//                var p = new BudgetPosition(c, budget);
//                positions.add(p);
//                this.positionRepo.save(p);
//            });
//        }
//    }
//
//    private LocalDate getBudgetMonthYearById(long budgetId)
//    {
//        return this.budgetRepo.findMonthYearByBudgetId(budgetId)
//                .orElseThrow(() -> new IllegalArgumentException("No month and year for given budget id!"));
//    }
//
//    Budget getBudgetById(long budgetId)
//    {
//        return this.budgetRepo.findById(budgetId)
//                .orElseThrow(() -> new IllegalArgumentException("No budget found for given id!"));
//    }
//
//    private long getUserIdByBudgetId(long budgetId)
//    {
//        return this.budgetRepo.findUserIdByBudgetId(budgetId)
//                .orElseThrow(() -> new IllegalArgumentException("No user id for given budget id!"));
//    }
//
//    public Budget createBudgetByUserEmail(BudgetDTO toSave, String email)
//    {
////        sprawdzenie czy data została zapisana w przyjętym formacie tj RRRR-MM-1
//        if (toSave.getMonthYear().getDayOfMonth() != 1)
//            toSave.setMonthYear(LocalDate.of(toSave.getMonthYear().getYear(), toSave.getMonthYear().getMonth().getValue(), 1));
//
////        sprawdzenie czy budżet na dany miesiąc i rok już istnieje w bazie
//         if (this.budgetRepo.existsByMonthYearAndUserId(toSave.getMonthYear(), this.userService.getUserIdByEmail(email)))
//            throw new IllegalArgumentException("Budget for given month and year already exists!");
//
//        var budget = toSave.toBudget();
//
//        budget.setUser(this.userService.getUserByEmail(email));
//
//        return this.budgetRepo.save(budget);
//    }
//
//    public List<Budget> getAllBudgets()
//    {
//        return this.budgetRepo.findAll();
//    }
//
//    public List<BudgetDTO> getAllBudgetsAsDto()
//    {
//        return mapToDtoAndSetAmountsForBudgetsList((getAllBudgets()));
//    }

    @Test
    void getBudgetsByUserId_shouldReturnBudgetsList()
    {
//        given
        var mockBudgetRepo = mock(BudgetRepository.class);
        given(mockBudgetRepo.findByUserId(anyLong())).willReturn(List.of(new Budget(), new Budget(), new Budget()));

//        system under test
        var toTest = new BudgetServiceImpl(mockBudgetRepo, null, null, null);

//        when
        var result = toTest.getBudgetsByUserId(2L);

//        then
        assertTrue(result.size() > 0);
        assertEquals(3, result.size());
        assertThat(result.get(0)).isInstanceOf(Budget.class);
    }
//    public List<BudgetDTO> getBudgetsByUserEmailAsDto(String email)
//    {
//       return mapToDtoAndSetAmountsForBudgetsList(getBudgetsByUserId(this.userService.getUserIdByEmail(email)));
//    }
//
//    List<BudgetDTO> mapToDtoAndSetAmountsForBudgetsList(List<Budget> budgets)
//    {
//        var result = budgets
//                .stream()
//                .map(Budget::toDto)
//                .toList();
////  userId jest wyciągniete z pierwszej pozycji listy budżetów z bazy ponieważ lista jest już wyciągnięta dla danego
////  użytkownika
//        result.forEach(d -> getBudgetDtoAmounts(d, budgets.get(0).getUser().getId()));
//
//        return result;
//    }

    @Test
    void getBudgetByNumberAndUserId_shouldReturnBudgetIfFoundInDb()
    {
//        given
        var mockBudgetRepo = mock(BudgetRepository.class);
        given(mockBudgetRepo.findByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.of(new Budget(LocalDate.now(), "foo")));

//        system under test
        var toTest = new BudgetServiceImpl(mockBudgetRepo, null, null, null);

//        when
        var result = toTest.getBudgetByNumberAndUserId("052023", 9L);

//        then
        assertNotNull(result);
        assertThat(result).isInstanceOf(Budget.class);
        assertEquals(LocalDate.now(), result.getMonthYear());
    }

    @Test
    void getBudgetByNumberAndUserId_shouldThrowExceptionIFBudgetNotFoundInDb()
    {
//        given
        var mockBudgetRepo = mock(BudgetRepository.class);
        given(mockBudgetRepo.findByMonthYearAndUserId(any(), anyLong())).willReturn(Optional.empty());

//        system under test
        var toTest = new BudgetServiceImpl(mockBudgetRepo, null, null, null);

//        when
        var result = catchThrowable(() -> toTest.getBudgetByNumberAndUserId("052023", 9L));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No budget for given month and year");
    }
//
//    public BudgetDTO getBudgetByNumberAndUserEmailAsDto(String number, long userId)
//    {
//        var dto = getBudgetByNumberAndUserId(number, userId).toDto();
//
//        getBudgetDtoAmounts(dto, userId);
//
//        return dto;
//    }
//
//    public Budget getBudgetByDateAndUserId(LocalDate date, Long userId)
//    {
////        TODO względnie czy nie zostawić pustego jeśli nie ma dostępneego budżetu?
//        return this.budgetRepo.findByMonthYearAndUserId(LocalDate.of(date.getYear(), date.getMonth().getValue(), 1), userId)
//                .orElseThrow(() -> new IllegalArgumentException("No budget found for given date!"));
//    }
//
//    public BudgetDTO getBudgetByDateAndUserIdAsDto(LocalDate date, Long userId)
//    {
//        var result = getBudgetByDateAndUserId(date, userId).toDto();
//
//        getBudgetDtoAmounts(result, userId);
//
//        return result;
//    }
}