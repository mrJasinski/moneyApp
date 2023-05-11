package com.moneyApp.budget.service;

import com.moneyApp.budget.Budget;
import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import com.moneyApp.budget.repository.BudgetPositionRepository;
import com.moneyApp.budget.repository.BudgetRepository;
import com.moneyApp.category.Category;
import com.moneyApp.category.CategoryType;
import com.moneyApp.category.service.CategoryService;
import com.moneyApp.transaction.Transaction;
import com.moneyApp.transaction.service.TransactionService;
import com.moneyApp.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService
{
    private final BudgetRepository budgetRepo;
    private final BudgetPositionRepository positionRepo;
    private final UserService userService;
    private final CategoryService categoryService;
    private final TransactionService transactionService;

    public BudgetService(BudgetRepository budgetRepo, BudgetPositionRepository positionRepo, UserService userService, CategoryService categoryService, TransactionService transactionService)
    {
        this.budgetRepo = budgetRepo;
        this.positionRepo = positionRepo;
        this.userService = userService;
        this.categoryService = categoryService;
        this.transactionService = transactionService;
    }

    public BudgetDTO getBudgetByNumberAndUserEmailAsDto(String number, String email)
    {
        var userId = this.userService.getUserIdByEmail(email);
        var dto = getBudgetByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId).toDto();

        getBudgetDtoAmounts(dto, userId);

        return dto;
    }

    Budget getBudgetByMonthYearAndUserId(LocalDate date, Long userId)
    {
        return this.budgetRepo.findByMonthYearAndUserId(date, userId)
                .orElseThrow(() -> new IllegalArgumentException("No budget for given month and year!"));
    }

    public void getBudgetDtoAmounts(BudgetDTO dto, Long userId)
    {
        var budgetId = getBudgetIdByNumberAndUserId(dto.getNumber(), userId);

        dto.setIncomes(getBudgetPositionsByBudgetIdAndCategoryTypeAsDto(budgetId, CategoryType.INCOME));
        dto.setExpenses(getBudgetPositionsByBudgetIdAndCategoryTypeAsDto(budgetId, CategoryType.EXPENSE));

        dto.setPlannedIncomes(dto.getIncomes().stream().mapToDouble(BudgetPositionDTO::getPlannedAmount).sum());
        dto.setPlannedExpenses(dto.getExpenses().stream().mapToDouble(BudgetPositionDTO::getPlannedAmount).sum());
        dto.setPlannedSum(dto.getPlannedIncomes() - dto.getPlannedExpenses());

        dto.setActualIncomes(dto.getIncomes().stream().mapToDouble(BudgetPositionDTO::getActualAmount).sum());
        dto.setActualExpenses(dto.getExpenses().stream().mapToDouble(BudgetPositionDTO::getActualAmount).sum());
        dto.setActualSum(dto.getActualIncomes() - dto.getActualExpenses());

        dto.setIncomesSum(dto.getPlannedIncomes() - dto.getActualIncomes());
        dto.setExpensesSum(dto.getPlannedExpenses() - dto.getActualExpenses());;
    }

    Long getBudgetIdByNumberAndUserId(String number, Long userId)
    {
        return this.budgetRepo.findIdByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId)
                .orElseThrow(() -> new IllegalArgumentException("No budget for given number!"));
    }

    LocalDate getBudgetMonthYearByNumber(String number)
    {
        if (number.length() == 6 && number.matches("\\d+"))
        {
            var month = Integer.parseInt(number.substring(0, 2));
            var year = Integer.parseInt(number.substring(2, 6));

            return LocalDate.of(year, month, 1);
        }
        else
            throw new IllegalArgumentException("Wrong budget number!");
    }

    List<BudgetPosition> getBudgetPositionsByBudgetIdAndCategoryType(long budgetId, CategoryType categoryType)
    {
        return this.positionRepo.findByBudgetIdAndCategoryType(budgetId, categoryType);
    }

    List<BudgetPositionDTO> getBudgetPositionsByBudgetIdAndCategoryTypeAsDto(long budgetId, CategoryType categoryType)
    {
        var userId = getUserIdByBudgetId(budgetId);

        var positions = getBudgetPositionsByBudgetIdAndCategoryType(budgetId, categoryType);

        var categories = this.categoryService.getCategoriesByTypeAndUserId(categoryType, userId);

        var posCategories = positions.stream().map(BudgetPosition::getCategory).toList();

        createBudgetPositionsByCategories(categories, posCategories, positions, budgetId);

        var transactions = this.transactionService.getTransactionsByMonthYearAndUserId(getBudgetMonthYearById(budgetId), userId);

        assignTransactionsToBudgetPositions(transactions, positions, userId);

        return convertBudgetPositionsToDto(positions);
    }

    List<BudgetPositionDTO> convertBudgetPositionsToDto(List<BudgetPosition> positions)
    {
        var dto = new ArrayList<BudgetPositionDTO>();

        positions.forEach(p ->
        {
            var pos = p.toDto();

            pos.setActualAmount(p.getTransactions().stream().mapToDouble(Transaction::getAmount).sum());
            pos.setAmountSum(pos.getPlannedAmount() - pos.getActualAmount());

            dto.add(pos);
        });

        return dto;
    }

    void assignTransactionsToBudgetPositions(List<Transaction> transactions, List<BudgetPosition> positions, long userId)
    {
        transactions.forEach(t ->
        {
            if (t.getPosition() == null)
                positions.forEach(p ->
                {
                    if ((p.getCategory()).equals(t.getCategory()))
                    {
                        t.setPosition(p);
                        this.transactionService.updatePositionIdInDb(t.getId(), p, userId);
                        p.getTransactions().add(t);
                    }
                });
        });
    }

    void createBudgetPositionsByCategories(List<Category> categories, List<Category> posCategories,
                                           List<BudgetPosition> positions, long budgetId)
    {
        categories.removeIf(posCategories::contains);

        if (categories.size() != 0)
        {
            var budget = getBudgetById(budgetId);

            categories.forEach(c ->
            {
                var p = new BudgetPosition(c, budget);
                positions.add(p);
                this.positionRepo.save(p);
            });
        }
    }

    private LocalDate getBudgetMonthYearById(long budgetId)
    {
        return this.budgetRepo.findMonthYearByBudgetId(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("No month and year for given budget id!"));
    }

    Budget getBudgetById(long budgetId)
    {
        return this.budgetRepo.findById(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("No budget found for given id!"));
    }

    private long getUserIdByBudgetId(long budgetId)
    {
        return this.budgetRepo.findUserIdByBudgetId(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("No user id for given budget id!"));
    }

    public Budget createBudgetByUserEmail(BudgetDTO toSave, String email)
    {
//        sprawdzenie czy data została zapisana w przyjętym formacie tj RRRR-MM-1
        if (toSave.getMonthYear().getDayOfMonth() != 1)
            toSave.setMonthYear(LocalDate.of(toSave.getMonthYear().getYear(), toSave.getMonthYear().getMonth().getValue(), 1));

//        sprawdzenie czy budżet na dany miesiąc i rok już istnieje w bazie
        if (this.budgetRepo.existsByMonthYearAndUserId(toSave.getMonthYear(), this.userService.getUserIdByEmail(email)))
            throw new IllegalArgumentException("Budget for given month and year already exists!");

        var budget = toSave.toBudget();

        budget.setUser(this.userService.getUserByEmail(email));

        return this.budgetRepo.save(budget);


    }

    public List<Budget> getAllBudgets()
    {
        return this.budgetRepo.findAll();
    }

    public List<BudgetDTO> getAllBudgetsAsDto()
    {
        return mapToDtoAndSetAmountsForBudgetsList((getAllBudgets()));
    }

    public List<Budget> getBudgetsByUserId(Long userId)
    {
        return this.budgetRepo.findByUserId(userId);
    }

    public List<BudgetDTO> getBudgetsByUserEmailAsDto(String email)
    {
       return mapToDtoAndSetAmountsForBudgetsList(getBudgetsByUserId(this.userService.getUserIdByEmail(email)));
    }

    List<BudgetDTO> mapToDtoAndSetAmountsForBudgetsList(List<Budget> budgets)
    {
        var result = budgets
                .stream()
                .map(Budget::toDto)
                .toList();
//  userId jest wyciągniete z pierwszej pozycji listy budżetów z bazy ponieważ lista jest już wyciągnięta dla danego
//  użytkownika
        result.forEach(d -> getBudgetDtoAmounts(d, budgets.get(0).getUser().getId()));

        return result;
    }

    Budget getBudgetByNumberAndUserId(String number, long userId)
    {
        return this.budgetRepo.findByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId)
                .orElseThrow(() -> new IllegalArgumentException("No budget for given month and year!"));
    }

    public BudgetDTO getBudgetByNumberAndUserEmailAsDto(String number, long userId)
    {
        var dto = getBudgetByNumberAndUserId(number, userId).toDto();

        getBudgetDtoAmounts(dto, userId);

        return dto;
    }

    public Budget getBudgetByDateAndUserId(LocalDate date, Long userId)
    {
//        TODO względnie czy nie zostawić pustego jeśli nie ma dostępneego budżetu?
        return this.budgetRepo.findByMonthYearAndUserId(LocalDate.of(date.getYear(), date.getMonth().getValue(), 1), userId)
                .orElseThrow(() -> new IllegalArgumentException("No budget found for given date!"));
    }

    public BudgetDTO getBudgetByDateAndUserIdAsDto(LocalDate date, Long userId)
    {
        var result = getBudgetByDateAndUserId(date, userId).toDto();

        getBudgetDtoAmounts(result, userId);

        return result;
    }


}
