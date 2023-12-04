package com.moneyApp.budget;

import com.moneyApp.bill.BillService;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import com.moneyApp.category.CategoryService;
import com.moneyApp.user.UserService;
import com.moneyApp.vo.BudgetSource;
import com.moneyApp.vo.UserSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService
{
    private final BudgetRepository budgetRepo;
    private final BudgetQueryRepository budgetQueryRepo;
    private final BillService billService;
    private final UserService userService;
    private final CategoryService categoryService;

    BudgetService(BudgetRepository budgetRepo
            , BudgetQueryRepository budgetQueryRepo
            , BillService billService
            , UserService userService
            , CategoryService categoryService)
    {
        this.budgetRepo = budgetRepo;
        this.budgetQueryRepo = budgetQueryRepo;
        this.billService = billService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    BudgetDTO getBudgetByNumberAndUserEmailAsDto(String number, Long userId)
    {
        var budget = getBudgetByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId);

        return new BudgetDTO(budget.getMonthYear(), budget.getDescription(), budget.getPositions()
                                                                                    .stream()
                                                                                    .map(p -> new BudgetPositionDTO(
                                                                                            p.getCategory().getName()
                                                                                            , p.getPlannedAmount()
                                                                                            , this.billService.getBillPositionsSumByBudgetPositionId(p.getId())
                                                                                            , p.getDescription()
                                                                                            ))
                                                                                    .collect(Collectors.toList()));
    }

    BudgetSnapshot getBudgetByMonthYearAndUserId(LocalDate monthYear, Long userId)
    {
        return this.budgetQueryRepo.findByMonthYearAndUserId(monthYear, userId)
                .orElseThrow(() -> new IllegalArgumentException("Budget for given monthYear and user id not found!"));
    }



//    BudgetPlanDTO getBudgetPlannedAsCopyOfMonth(LocalDate date, String email)
//    {
//        var userId = this.userService.getUserIdByEmail(email);
//
//        if (!this.budgetQueryRepo.existsByMonthYearAndUserId(date, userId))
//            throw new IllegalArgumentException("Budget for given data does not exists!");
//
//        return new BudgetPlanDTO(getBudgetByDateAndUserId(date, userId));
//    }
//
//    BudgetPlanDTO getBudgetPlannedAsCopyByMethod(Integer number, CalculationType type, String email)
//    {
//        var userId = this.userService.getUserIdByEmail(email);
//
//        if (getAmountOfBudgetsUserId(userId) < number)
//            throw new IllegalArgumentException("Not enough budgets in database!");
//
//        var budgets = getLatestBudgetsByAmount(number, userId);
//
//        var values = new HashMap<String, List<Double>>();
//
//        budgets.forEach(b ->
//        {
//            b.getPositions().forEach(bp ->
//            {
//                var categoryName = bp.getCategory().getCategoryName();
//
//                if (!values.containsKey(categoryName))
//                    values.put(categoryName, new ArrayList<>());
//
//                values.get(categoryName).add(bp.getPlannedAmount());
//            });
//        });
//
//        var result = new HashMap<String, Double>();
//
//        switch (type)
//        {
//            case AVERAGE ->
//                values.forEach((k, v) ->
//                {
//                    var sum = 0d;
//
//                   sum = v.stream().mapToDouble(Double::doubleValue).sum();
//
//                    sum = sum / v.size();
//
//                    result.put(k, sum);
//                });
//
//            case MEDIAN ->
//                values.forEach((k, v) ->
//                {
//                    var arr = v.stream().mapToDouble(Double::doubleValue).toArray();
//                   Arrays.sort(arr);
//
//                   var med = 0d;
//
//                   if (arr.length % 2 != 0)
//                       med = arr[arr.length / 2];
//
//                   if (arr.length % 2 == 0)
//                       med = (arr[arr.length / 2] + arr[arr.length / 2 - 1]) / 2;
//
//                   result.put(k, med);
//                });
//            }
//
//        var positions = new ArrayList<BudgetPositionDTO>();
//
//        result.forEach((k, v) -> positions.add(new BudgetPositionDTO(k, v)));
//
//        return new BudgetPlanDTO(positions);
//    }
//
//    Integer getAmountOfBudgetsUserId(Long userId)
//    {
//        return this.budgetQueryRepo.findAmountOfBudgetsByUserId(userId);
//    }
//
//    List<Budget> getLatestBudgetsByAmount(Integer number, Long userId)
//    {
//        return this.budgetQueryRepo.findLatestBudgetsByAmountAndUserId(number, userId);
//    }
//
//    Budget getBudgetByMonthYearAndUserId(LocalDate date, Long userId)
//    {
//        return this.budgetQueryRepo.findByMonthYearAndUserId(date, userId)
//                .orElseThrow(() -> new IllegalArgumentException("No budget for given month and year!"));
//    }
//
//    void getBudgetDtoAmounts(BudgetDTO dto, Long userId)
//    {
//        var budgetId = getBudgetIdByNumberAndUserId(dto.getNumber(), userId);
//
//        dto.setIncomes(getBudgetPositionsByBudgetIdAndCategoryTypeAsDto(budgetId, INCOME));
//        dto.setExpenses(getBudgetPositionsByBudgetIdAndCategoryTypeAsDto(budgetId, EXPENSE));
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
//
//    Long getBudgetIdByNumberAndUserId(String number, Long userId)
//    {
//        return this.budgetQueryRepo.findIdByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId)
//                .orElseThrow(() -> new IllegalArgumentException("No budget for given number!"));
//    }
//
    LocalDate getBudgetMonthYearByNumber(String number)
    {
//        check if budget number has correct format (6 digits)
        if (!(number.length() == 6 || !number.matches("\\d+")))
            throw new IllegalArgumentException("Wrong budget number!");

        var month = Integer.parseInt(number.substring(0, 2));
        var year = Integer.parseInt(number.substring(2, 6));

        return LocalDate.of(year, month, 1);
    }
//
////    List<Budget.Position> getBudgetPositionsByBudgetIdAndCategoryType(long budgetId, CategoryType categoryType)
////    {
////        return this.budgetQueryRepo.findByBudgetIdAndCategoryType(budgetId, categoryType);
////    }
//
////    List<BudgetPositionDTO> getBudgetPositionsByBudgetIdAndCategoryTypeAsDto(long budgetId, CategoryType categoryType)
//    {
////        var userId = getUserIdByBudgetId(budgetId);
////
////        var positions = getBudgetPositionsByBudgetIdAndCategoryType(budgetId, categoryType);
//
////        var categories = this.categoryService.getCategoriesByTypeAndUserId(categoryType, userId);
//
////        var posCategories = positions.stream().map(Budget.Position::getCategory).toList();
////TODO czy da się to skrócić do podnie jak w getBudgetForList
////        createBudgetPositionsByCategories(categories, posCategories, positions, budgetId);
//
////        TODO tymczasowe
////        var transactions = this.billPositionService.getTransactionsByMonthYearAndUserId(getBudgetMonthYearById(budgetId), userId);
////        assignBillPositionsToBudgetPositions(transactions, positions, userId);
//
////        return convertBudgetPositionsToDto(positions);
////    }
//
//    List<BudgetPositionDTO> convertBudgetPositionsToDto(List<Budget.Position> positions)
//    {
//        var dto = new ArrayList<BudgetPositionDTO>();
//
//        positions.forEach(p ->
//        {
////            var pos = p.toDto();
////
////            pos.setActualAmount(p.getTransactions().stream().mapToDouble(SimpleBillPosition::getAmount).sum());
////            pos.setAmountSum(pos.getPlannedAmount() - pos.getActualAmount());
//
////            dto.add(pos);
//        });
//
//        return dto;
//    }
//
////    void assignBillPositionsToBudgetPositions(List<SimpleBillPosition> billPositions, List<Budget.Position> budgetPositions, long userId)
////    {
////        billPositions.forEach(billPos ->
////        {
//////            if (billPos.getPosition() == null)
//////                budgetPositions.forEach(p ->
//////                {
//////                    if ((p.getCategory()).equals(billPos.getCategory()))
//////                    {
////////                        TODO tymczasowe
////////                        this.billPositionService.updatePositionInTransaction(t, p, userId);
//////                        p.getTransactions().add(billPos);
//////                    }
//////                });
////        });
////    }
//
////    void createBudgetPositionsByCategories(List<Category> categories, List<Category> posCategories,
////                                                  List<Budget.Position> positions, long budgetId)
////    {
////        categories.removeIf(posCategories::contains);
////
////        if (categories.size() != 0)
////        {
////            var budget = getBudgetById(budgetId);
////
////            categories.forEach(c ->
////            {
////                var p = new Budget.Position(c, budget);
////                positions.add(p);
////                this.budgetRepo.save(p);
////            });
////        }
////    }
//
////    LocalDate getBudgetMonthYearById(long budgetId)
////    {
////        return this.budgetQueryRepo.findMonthYearByBudgetId(budgetId)
////                .orElseThrow(() -> new IllegalArgumentException("No month and year for given budget id!"));
////    }
////
////    Budget getBudgetById(long budgetId)
////    {
////        return this.budgetQueryRepo.findById(budgetId)
////                .orElseThrow(() -> new IllegalArgumentException("No budget found for given id!"));
////    }
////
////    long getUserIdByBudgetId(long budgetId)
////    {
////        return this.budgetQueryRepo.findUserIdByBudgetId(budgetId)
////                .orElseThrow(() -> new IllegalArgumentException("No user id for given budget id!"));
////    }
//
//
//
//    Budget createBudgetByUserEmail(BudgetDTO toSave, MonthType monthType, CalculationType calculationType, Integer monthsCount, String email)
//    {
////        check if date has correct format (YYYY-MM-1)
//        if (toSave.getMonthYear().getDayOfMonth() != 1)
//            toSave.setMonthYear(LocalDate.of(toSave.getMonthYear().getYear(), toSave.getMonthYear().getMonth().getValue(), 1));
//
////        check if budget for given month already exists in database for user
////        if (this.budgetQueryRepo.existsByMonthYearAndUserId(toSave.getMonthYear(), this.userService.getUserIdByEmail(email)))
////            throw new IllegalArgumentException("Budget for given month and year already exists!");
//
////        sprawdzenie kopiowania z miesiąca/mediany/ilość mscy
//
//        if (monthType != null)
//        {
////            last lub adequate
//
//            switch (monthType)
//            {
//                case LAST_MONTH ->
//                {
////                    TODO
////                    check if exists any previous budget
////                    if exists copy planned
////                    if not throw exception
//                }
//                case ADEQUATE_MONTH ->
//                {
////                    TODO
////                    check if exists budget for adequate month (eg now is created budget for 11/2023 so check is for 11/2022)
////                    if exists copy planned
////                    if not throw exception
//                }
//            }
//        }
//
//        var budget = toSave.toBudget();
//
//        budget.setUser(this.userService.getUserByEmail(email));
//
//        return this.budgetRepo.save(budget);
//    }
//
////    List<Budget> getAllBudgets()
////    {
////        return this.budgetQueryRepo.findAll();
////    }
//
////    List<BudgetDTO> getAllBudgetsAsDto()
////    {
////        return mapToDtoAndSetAmountsForBudgetsList((getAllBudgets()));
////    }
//
////    List<Budget> getBudgetsByUserId(Long userId)
////    {
////        return this.budgetQueryRepo.findByUserId(userId);
////    }
//
//    List<BudgetDTO> getBudgetForList(String email, Long userId)
//    {
//
////        przed wyciągnięciem listy sprawdzenie czy powinny zostać przypisane jakieś pozycje transakcje itd
////sprawdzenie czy są transakcje z danego miesiąca bez przypisanej pozycji
////                TODO tymczasowe
////        var transactions = this.billPositionService.getTransactionsWithoutBudgetPositionsByUserId(userId);
//
////        if (transactions.size() != 0)
////            for (BillPosition t : transactions)
////            {
////                var position = this.positionRepo.findBudgetPositionByDateAndCategoryAndUserId(t.getBill().getBillDate(), t.getCategory(), userId)
////                        .orElse(createBudgetPosition(t.getCategory(), getBudgetByDateAndUserId(t.getBill().getBillDate(), userId)));
//
////                TODO tymczasowe
////                this.billPositionService.updatePositionInTransaction(t, position, userId);
////            }
//
//
//
////        var budgets = getBudgetsByUserId(this.userService.getUserIdByEmail(email));
////        var result = new ArrayList<BudgetDTO>();
////
////        budgets.forEach(b ->
////        {
////            var incomes = new ArrayList<Budget.Position>();
////            var expenses = new ArrayList<Budget.Position>();
////
////            for (Budget.Position p : b.getPositions())
////            {
////                switch (p.getCategory().getType())
////                {
////                    case INCOME -> incomes.add(p);
////                    case EXPENSE -> expenses.add(p);
////                }
////            }
//
////            var dto = b.toDto();
////
////            dto.setPlannedIncomes(incomes.stream().mapToDouble(Budget.Position::getPlannedAmount).sum());
////            dto.setPlannedExpenses(expenses.stream().mapToDouble(Budget.Position::getPlannedAmount).sum());
////            dto.setPlannedSum(dto.getPlannedIncomes() - dto.getPlannedExpenses());
////
////            dto.setActualIncomes(sumBillPositionsInBudgetPositionsList(incomes));
////            dto.setActualExpenses(sumBillPositionsInBudgetPositionsList(expenses));
////            dto.setActualSum(dto.getActualIncomes() - dto.getActualExpenses());
////        });
////
////        return result;
////    }
//
////    Budget.Position createBudgetPosition(Category category, Budget budget)
////    {
////        return this.budgetRepo.save(new Budget.Position(category, budget));
////    }
//
////    double sumBillPositionsInBudgetPositionsList(List<Budget.Position> positions)
////    {
////        var sum = 0;
////
////        for (Budget.Position p : positions)
////            sum += p.getTransactions().stream().mapToDouble(SimpleBillPosition::getAmount).sum();
////
////        return sum;
////    }
//
    List<BudgetDTO> getBudgetsByUserIdAsDto(Long userId)
    {
        return this.budgetQueryRepo.findAllByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
//
////    public List<BudgetDTO> mapToDtoAndSetAmountsForBudgetsList(List<Budget> budgets)
////    {
////        var result = budgets
////                .stream()
////                .map(Budget::toDto)
////                .toList();
////
//////  userId is retrieved from budgets list first position from db because list is already for given user
////
////        result.forEach(d -> getBudgetDtoAmounts(d, budgets.get(0).getUser().getId()));
////
////        return result;
////    }
//
////    Budget getBudgetByNumberAndUserId(String number, long userId)
////    {
////        return this.budgetQueryRepo.findByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId)
////                .orElseThrow(() -> new IllegalArgumentException("No budget for given month and year!"));
////    }
//
////    BudgetDTO getBudgetByNumberAndUserEmailAsDto(String number, long userId)
////    {
////        var dto = getBudgetByNumberAndUserId(number, userId).toDto();
////
////        getBudgetDtoAmounts(dto, userId);
////
////        return dto;
////    }
//
    BudgetSnapshot getBudgetByDateAndUserId(LocalDate date, Long userId)
    {
//        TODO względnie czy nie zostawić pustego jeśli nie ma dostępnego budżetu?
        return this.budgetQueryRepo.findByMonthYearAndUserId(LocalDate.of(date.getYear(), date.getMonth().getValue(), 1), userId)
                .orElseThrow(() -> new IllegalArgumentException("No budget found for given date!"));
    }

    public BudgetDTO getBudgetByDateAndUserIdAsDto(LocalDate date, Long userId)
    {
        if (!this.budgetQueryRepo.existsByMonthYearAndUserId(date, userId))
            throw new IllegalArgumentException("Budget for given date not found!");

        var sums = getIncomesAndExpensesSumsByMonthYearAndUserId(date, userId);

        return new BudgetDTO(date, sums[0], sums[1], sums[2], sums[3]);
    }

    private double[] getIncomesAndExpensesSumsByMonthYearAndUserId(final LocalDate date, final Long userId)
    {
        var result = new double[4];

        result[0] = this.budgetQueryRepo.findPlannedIncomesAmountByMonthYearAndUserId(date, userId).orElse(0d);
        result[1] = this.budgetQueryRepo.findActualIncomesAmountByMonthYearAndUserId(date, userId).orElse(0d);
        result[2] = this.budgetQueryRepo.findPlannedExpensesAmountByMonthYearAndUserId(date, userId).orElse(0d);
        result[3] = this.budgetQueryRepo.findActualExpensesAmountByMonthYearAndUserId(date, userId).orElse(0d);

        return result;
    }

    public BudgetSource getBudgetSourceByDateAndUserId(final LocalDate monthYear, final Long userId)
    {
        return new BudgetSource(this.budgetQueryRepo.findIdByMonthYearAndUserId(monthYear, userId)
                .orElseThrow(() -> new IllegalArgumentException("Budget for give date not found!")));
    }

    BudgetDTO createBudgetByUserIdAsDto(final BudgetDTO toSave, final Long userId)
    {
//        check if date has correct format (YYYY-MM-1)
        if (toSave.getMonthYear().getDayOfMonth() != 1)
            toSave.setMonthYear(LocalDate.of(toSave.getMonthYear().getYear(), toSave.getMonthYear().getMonth().getValue(), 1));

//        check if budget for given month already exists in database for user
        if (this.budgetQueryRepo.existsByMonthYearAndUserId(toSave.getMonthYear(), userId))
           throw new IllegalArgumentException("Budget for given month and year already exists!");

        return toDto(this.budgetRepo.save(new BudgetSnapshot(
                null
                , toSave.getMonthYear()
                , toSave.getDescription()
                , new UserSource(userId)
                , new HashSet<>())));
    }

    BudgetDTO toDto(BudgetSnapshot snap)
    {
        return new BudgetDTO(
                snap.getMonthYear()
                , snap.getDescription());
    }

    void deleteBudgetByNumberAndUserId(final String number, final Long userId)
    {
        this.budgetRepo.deleteByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId);
    }
}