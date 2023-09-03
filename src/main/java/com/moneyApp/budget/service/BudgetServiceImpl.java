package com.moneyApp.budget.service;

import com.moneyApp.bill.BillPosition;
import com.moneyApp.bill.service.BillPositionService;
import com.moneyApp.budget.Budget;
import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.budget.CalculationType;
import com.moneyApp.budget.MonthType;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.dto.BudgetPlanDTO;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import com.moneyApp.budget.repository.BudgetPositionRepository;
import com.moneyApp.budget.repository.BudgetRepository;
import com.moneyApp.category.Category;
import com.moneyApp.category.CategoryType;
import com.moneyApp.category.service.CategoryService;
import com.moneyApp.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService, BudgetPositionService
{
    private final BudgetRepository budgetRepo;
    private final BudgetPositionRepository positionRepo;
    private final UserService userService;
    private final CategoryService categoryService;
//    private final BillPositionService billPositionService;

    BudgetServiceImpl(BudgetRepository budgetRepo, BudgetPositionRepository positionRepo, UserService userService, CategoryService categoryService)
//            TODO tymczasowe
//                             ,BillPositionService billPositionService)
    {
        this.budgetRepo = budgetRepo;
        this.positionRepo = positionRepo;
        this.userService = userService;
        this.categoryService = categoryService;
//        this.billPositionService = billPositionService;
    }

    public BudgetDTO getBudgetByNumberAndUserEmailAsDto(String number, String email)
    {
        var userId = this.userService.getUserIdByEmail(email);
        var dto = getBudgetByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId).toDto();

        getBudgetDtoAmounts(dto, userId);

        return dto;
    }

    @Override
    public BudgetPlanDTO getBudgetPlannedAsCopyOfMonth(LocalDate date, String email)
    {
        var userId = this.userService.getUserIdByEmail(email);

        if (!this.budgetRepo.existsByMonthYearAndUserId(date, userId))
            throw new IllegalArgumentException("Budget for given data does not exists!");


        return new BudgetPlanDTO(getBudgetByDateAndUserId(date, userId));
    }

    @Override
    public BudgetPlanDTO getBudgetPlannedAsCopyByMethod(Integer number, CalculationType type, String email)
    {
        var userId = this.userService.getUserIdByEmail(email);

        if (getAmountOfBudgetsUserId(userId) < number)
            throw new IllegalArgumentException("Not enough budgets in database!");

        var budgets = getLatestBudgetsByAmount(number, userId);

        var values = new HashMap<String, List<Double>>();

        budgets.forEach(b ->
        {
            b.getPositions().forEach(bp ->
            {
                var categoryName = bp.getCategory().getCategoryName();

                if (!values.containsKey(categoryName))
                    values.put(categoryName, new ArrayList<>());

                values.get(categoryName).add(bp.getPlannedAmount());
            });
        });

        var result = new HashMap<String, Double>();

        switch (type)
        {
            case AVERAGE ->
                values.forEach((k, v) ->
                {
                    var sum = 0d;

                   sum = v.stream().mapToDouble(Double::doubleValue).sum();

                    sum = sum / v.size();

                    result.put(k, sum);
                });

            case MEDIAN ->
                values.forEach((k, v) ->
                {
                    var arr = v.stream().mapToDouble(Double::doubleValue).toArray();
                   Arrays.sort(arr);

                   var med = 0d;

                   if (arr.length % 2 != 0)
                       med = arr[arr.length / 2];

                   if (arr.length % 2 == 0)
                       med = (arr[arr.length / 2] + arr[arr.length / 2 - 1]) / 2;

                   result.put(k, med);
                });
            }

        var positions = new ArrayList<BudgetPositionDTO>();

        result.forEach((k, v) -> positions.add(new BudgetPositionDTO(k, v)));

        return new BudgetPlanDTO(positions);
    }

    @Override
    public Integer getAmountOfBudgetsUserId(Long userId)
    {
        return this.budgetRepo.findAmountOfBudgetsByUserId(userId);
    }

    @Override
    public List<Budget> getLatestBudgetsByAmount(Integer number, Long userId)
    {
        return this.budgetRepo.findLatestBudgetsByAmountAndUserId(number, userId);
    }

    public Budget getBudgetByMonthYearAndUserId(LocalDate date, Long userId)
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

    public Long getBudgetIdByNumberAndUserId(String number, Long userId)
    {
        return this.budgetRepo.findIdByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId)
                .orElseThrow(() -> new IllegalArgumentException("No budget for given number!"));
    }

    public LocalDate getBudgetMonthYearByNumber(String number)
    {
//        check if budget number has correct format (6 digits)
        if (!(number.length() == 6 && number.matches("\\d+")))
            throw new IllegalArgumentException("Wrong budget number!");

        var month = Integer.parseInt(number.substring(0, 2));
        var year = Integer.parseInt(number.substring(2, 6));

        return LocalDate.of(year, month, 1);
    }

    public List<BudgetPosition> getBudgetPositionsByBudgetIdAndCategoryType(long budgetId, CategoryType categoryType)
    {
        return this.positionRepo.findByBudgetIdAndCategoryType(budgetId, categoryType);
    }

    public List<BudgetPositionDTO> getBudgetPositionsByBudgetIdAndCategoryTypeAsDto(long budgetId, CategoryType categoryType)
    {
        var userId = getUserIdByBudgetId(budgetId);

        var positions = getBudgetPositionsByBudgetIdAndCategoryType(budgetId, categoryType);

        var categories = this.categoryService.getCategoriesByTypeAndUserId(categoryType, userId);

        var posCategories = positions.stream().map(BudgetPosition::getCategory).toList();
//TODO czy da się to skrócić do podnie jak w getBudgetForList
        createBudgetPositionsByCategories(categories, posCategories, positions, budgetId);

//        TODO tymczasowe
//        var transactions = this.billPositionService.getTransactionsByMonthYearAndUserId(getBudgetMonthYearById(budgetId), userId);
//        assignBillPositionsToBudgetPositions(transactions, positions, userId);

        return convertBudgetPositionsToDto(positions);
    }

    public List<BudgetPositionDTO> convertBudgetPositionsToDto(List<BudgetPosition> positions)
    {
        var dto = new ArrayList<BudgetPositionDTO>();

        positions.forEach(p ->
        {
            var pos = p.toDto();

            pos.setActualAmount(p.getTransactions().stream().mapToDouble(BillPosition::getAmount).sum());
            pos.setAmountSum(pos.getPlannedAmount() - pos.getActualAmount());

            dto.add(pos);
        });

        return dto;
    }

    public void assignBillPositionsToBudgetPositions(List<BillPosition> billPositions, List<BudgetPosition> positions, long userId)
    {
        billPositions.forEach(t ->
        {
            if (t.getPosition() == null)
                positions.forEach(p ->
                {
                    if ((p.getCategory()).equals(t.getCategory()))
                    {
//                        TODO tymczasowe
//                        this.billPositionService.updatePositionInTransaction(t, p, userId);
                        p.getTransactions().add(t);
                    }
                });
        });
    }

    public void createBudgetPositionsByCategories(List<Category> categories, List<Category> posCategories,
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

    public LocalDate getBudgetMonthYearById(long budgetId)
    {
        return this.budgetRepo.findMonthYearByBudgetId(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("No month and year for given budget id!"));
    }

    public Budget getBudgetById(long budgetId)
    {
        return this.budgetRepo.findById(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("No budget found for given id!"));
    }

    public long getUserIdByBudgetId(long budgetId)
    {
        return this.budgetRepo.findUserIdByBudgetId(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("No user id for given budget id!"));
    }



    public Budget createBudgetByUserEmail(BudgetDTO toSave, MonthType monthType, CalculationType calculationType, Integer monthsCount, String email)
    {
//        check if date has correct format (YYYY-MM-1)
        if (toSave.getMonthYear().getDayOfMonth() != 1)
            toSave.setMonthYear(LocalDate.of(toSave.getMonthYear().getYear(), toSave.getMonthYear().getMonth().getValue(), 1));

//        check if budget for given month already exists in database for user
        if (this.budgetRepo.existsByMonthYearAndUserId(toSave.getMonthYear(), this.userService.getUserIdByEmail(email)))
            throw new IllegalArgumentException("Budget for given month and year already exists!");

//        sprawdzenie kopiowania z miesiąca/mediany/ilość mscy

        if (monthType != null)
        {
//            last lub adequate

            switch (monthType)
            {
                case LAST_MONTH ->
                {
//                    TODO
//                    check if exists any previous budget
//                    if exists copy planned
//                    if not throw exception
                }
                case ADEQUATE_MONTH ->
                {
//                    TODO
//                    check if exists budget for adequate month (eg now is created budget for 11/2023 so check is for 11/2022)
//                    if exists copy planned
//                    if not throw exception
                }
            }
        }

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



    public List<BudgetDTO> getBudgetForList(String email, Long userId)
    {

//        przed wyciągnięciem listy sprawdzenie czy powinny zostać przypisane jakieś pozycje transakcje itd
//sprawdzenie czy są transakcje z danego miesiąca bez przypisanej pozycji
//                TODO tymczasowe
//        var transactions = this.billPositionService.getTransactionsWithoutBudgetPositionsByUserId(userId);

//        if (transactions.size() != 0)
//            for (BillPosition t : transactions)
//            {
//                var position = this.positionRepo.findBudgetPositionByDateAndCategoryAndUserId(t.getBill().getBillDate(), t.getCategory(), userId)
//                        .orElse(createBudgetPosition(t.getCategory(), getBudgetByDateAndUserId(t.getBill().getBillDate(), userId)));

//                TODO tymczasowe
//                this.billPositionService.updatePositionInTransaction(t, position, userId);
//            }



        var budgets = getBudgetsByUserId(this.userService.getUserIdByEmail(email));
        var result = new ArrayList<BudgetDTO>();

        budgets.forEach(b ->
        {
            var incomes = new ArrayList<BudgetPosition>();
            var expenses = new ArrayList<BudgetPosition>();

            for (BudgetPosition p : b.getPositions())
            {
                switch (p.getCategory().getType())
                {
                    case INCOME -> incomes.add(p);
                    case EXPENSE -> expenses.add(p);
                }
            }

            var dto = b.toDto();

            dto.setPlannedIncomes(incomes.stream().mapToDouble(BudgetPosition::getPlannedAmount).sum());
            dto.setPlannedExpenses(expenses.stream().mapToDouble(BudgetPosition::getPlannedAmount).sum());
            dto.setPlannedSum(dto.getPlannedIncomes() - dto.getPlannedExpenses());

            dto.setActualIncomes(sumBillPositionsInBudgetPositionsList(incomes));
            dto.setActualExpenses(sumBillPositionsInBudgetPositionsList(expenses));
            dto.setActualSum(dto.getActualIncomes() - dto.getActualExpenses());
        });

        return result;
    }

    public BudgetPosition createBudgetPosition(Category category, Budget budget)
    {
        return this.positionRepo.save(new BudgetPosition(category, budget));
    }

    public double sumBillPositionsInBudgetPositionsList(List<BudgetPosition> positions)
    {
        var sum = 0;

        for (BudgetPosition p : positions)
            sum += p.getTransactions().stream().mapToDouble(BillPosition::getAmount).sum();

        return sum;
    }

    public List<BudgetDTO> getBudgetsByUserEmailAsDto(String email)
    {
        return mapToDtoAndSetAmountsForBudgetsList(getBudgetsByUserId(this.userService.getUserIdByEmail(email)));
    }

    public List<BudgetDTO> mapToDtoAndSetAmountsForBudgetsList(List<Budget> budgets)
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

    public Budget getBudgetByNumberAndUserId(String number, long userId)
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
