package com.moneyAppV5.budget.service;

import com.moneyAppV5.budget.dto.*;
import com.moneyAppV5.budget.Budget;
import com.moneyAppV5.budget.BudgetPosition;
import com.moneyAppV5.budget.repository.BudgetPositionRepository;
import com.moneyAppV5.budget.repository.BudgetRepository;
import com.moneyAppV5.category.Category;
import com.moneyAppV5.category.Type;
import com.moneyAppV5.category.dto.CategoryDTO;
import com.moneyAppV5.category.service.CategoryService;
import com.moneyAppV5.transaction.Transaction;
import com.moneyAppV5.transaction.dto.TransactionDTO;
import com.moneyAppV5.transaction.service.TransactionService;
import com.moneyAppV5.utils.UtilService;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BudgetService
{
    private final BudgetRepository repository;
    private final BudgetPositionRepository positionsRepository;
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final UtilService utilService;

    BudgetService(BudgetRepository repository, BudgetPositionRepository positionsRepository, TransactionService transactionService,
                  CategoryService categoryService, UtilService utilService)
    {
        this.repository = repository;
        this.positionsRepository = positionsRepository;
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.utilService = utilService;
    }

    List<BudgetPosition> readBudgetPositionsByBudgetIdAndType(int id, Type type)
    {
        return this.positionsRepository.findPositionsByBudgetIdAndType(id, type.name());
    }

    BudgetPositionDTO readBudgetPositionAsDto(BudgetPosition bp)
    {
        var actual = sumTransactionsByPositionId(bp.getId());

        return new BudgetPositionDTO.BudgetPositionDtoBuilder()
                .buildCategory(bp.getCategory().toDto())
                .buildPlannedAmount(bp.getPlannedAmount())
                .buildDescription(bp.getDescription())
                .buildHash(bp.getHash())
                .buildActualAmount(actual)
                .buildBalance()
                .buildTransactionsDto(this.transactionService.readTransactionsByBudgetPositionIdAsDto(bp.getId()))
                .build();
    }

    public BudgetPositionDTO readBudgetPositionViewAsDto(BudgetPosition bp)
    {
//        var position = new BudgetPositionDTO();
//
//        position.setHash(bp.getHash());
//        position.setPlannedAmount(bp.getPlannedAmount());
//        position.setActualAmount();
//        position.setUsage((int) ((position.getActualAmount() / position.getPlannedAmount()) * 100));
//        position.setBudgetDto(new BudgetDTO(bp.getBudget()));
//        position.setCategory(new CategoryDTO(bp.getCategory()));
//        position.setDailyView(sumDailyTransactionsByPositionIdAndMonth(bp.getId(), Month.of(bp.getBudget().getMonth()).length(Year.isLeap(bp.getBudget().getMonth()))));
//        position.setTransactionsDto(this.transactionService.readTransactionsByBudgetPositionIdAsDto(bp.getId()));

//        TODO
//        stats

        return new BudgetPositionDTO.BudgetPositionDtoBuilder()
                .buildHash(bp.getHash())
                .buildPlannedAmount(bp.getPlannedAmount())
                .buildActualAmount(this.transactionService.sumTransactionsByPositionId(bp.getId()))
                .buildUsage()
                .buildBudgetDto(new BudgetDTO(bp.getBudget()))
                .buildCategory(new CategoryDTO(bp.getCategory()))
                .buildDailyView(sumDailyTransactionsByPositionIdAndMonth(bp.getId(), Month.of(bp.getBudget().getMonth()).length(Year.isLeap(bp.getBudget().getMonth()))))
                .buildTransactionsDto(this.transactionService.readTransactionsByBudgetPositionIdAsDto(bp.getId()))
                .build();
    }

    public HashMap<String, Double> sumDailyTransactionsByPositionIdAndMonth(int positionId, int monthLength)
    {
        var map = new HashMap<String, Double>();

        for (String day : monthDaysList(monthLength))
            map.put(day, this.transactionService.sumTransactionsByDayAdnPositionId(day, positionId));

        return map;
    }

    public List<String> monthDaysList(int monthLength)
    {
        var days = new ArrayList<String>();

        for (int i = 1; i <= monthLength; i++)
            days.add(String.valueOf(i));

        return days;
    }

    private List<BudgetPositionDTO> readBudgetPositionsByBudgetIdAndTypeAsDto(int budgetId, Type type)
    {
        return readBudgetPositionsByBudgetIdAndType(budgetId, type).stream().map(BudgetPositionDTO::new).collect(Collectors.toList());
    }

    public BudgetDTO readBudgetAsDto(Budget b)
    {
//        var budget = new BudgetDTO(b);

//        this.month = budget.getMonth();
//        this.year = budget.getYear();
//        setMonthName(budget.getMonth());
//        setName(budget.getMonth(), budget.getYear());
//        this.description = budget.getDescription();
//        this.hash = budget.getHash();

//        budget.setIncomesDto();
//        budget.setExpensesDto();
//        budget.setTransactionsDto();
//
//        budget.setPlannedIncomes();
//        budget.setActualIncomes();
//
//        budget.setPlannedExpenses();
//        budget.setActualExpenses();

//        budget.setBalancePlanned(budget.getPlannedIncomes() - budget.getPlannedExpenses());
//        budget.setBalanceActual(budget.getActualIncomes() - budget.getActualExpenses());

//        budget.setBalanceIncomes(budget.getPlannedIncomes() - budget.getActualIncomes());
//        budget.setBalanceExpenses(budget.getPlannedExpenses() - budget.getActualExpenses());
//
//        return budget;
        var incomes = readBudgetPositionsByBudgetIdAndTypeAsDto(b.getId(), Type.INCOME);
        var expenses = readBudgetPositionsByBudgetIdAndTypeAsDto(b.getId(), Type.EXPENSE);

        return buildBudgetDtoByBudgetData(b)
                .buildIncomesDto(incomes)
                .buildExpensesDto(expenses)
                .buildTransactionsDto(readTransactionsByBudgetIdAsDto(b.getId()))
                .buildPlannedIncomes(sumPlannedByList(incomes))
                .buildActualIncomes(sumActualByList(incomes))
                .buildPlannedExpenses(sumPlannedByList(expenses))
                .buildActualExpenses(sumActualByList(expenses))
                .buildBalancePlanned(sumPlannedByList(incomes) - sumPlannedByList(expenses))
                .buildBalanceActual(sumActualByList(incomes) - sumActualByList(expenses))
                .buildBalanceIncomes(sumPlannedByList(incomes) - sumActualByList(incomes))
                .buildBalanceExpenses(sumPlannedByList(expenses) - sumActualByList(expenses))
                .build();
    }

    Budget readBudgetByMonthAndYear(int month, int year)
    {
        var empty = new Budget.BudgetBuilder().buildMonth(month).buildYear(year).build();

        return this.repository.findByMonthAndYear(month, year).orElse(empty);
    }

    public BudgetDTO readBudgetOnlyWithActualByAccountIdAndMonthAsDto(int[] date, int accountId)
    {
        var b = readBudgetByMonthAndYear(date[0], date[1]);

        return buildBudgetDtoByBudgetData(b)
                .buildActualIncomes(this.transactionService.sumTransactionsByBudgetIdAndAccountIdAndType(b.getId(), accountId, Type.INCOME))
                .buildActualExpenses(this.transactionService.sumTransactionsByBudgetIdAndAccountIdAndType(b.getId(), accountId, Type.EXPENSE))
                .buildBalanceActual(this.transactionService.sumTransactionsByBudgetIdAndAccountIdAndType(b.getId(), accountId, Type.INCOME)
                        - this.transactionService.sumTransactionsByBudgetIdAndAccountIdAndType(b.getId(), accountId, Type.EXPENSE))
                .build();
    }

    private double sumPlannedByList(List<BudgetPositionDTO> list)
    {
        double sum = 0;

        for (BudgetPositionDTO bp : list)
            sum += bp.getPlannedAmount();

        return sum;
    }

    private double sumActualByList(List<BudgetPositionDTO> list)
    {
        double sum = 0;

        for (BudgetPositionDTO bp : list)
            sum += bp.getActualAmount();

        return sum;
    }

    private double sumPlannedByBudgetIdAndType(int budgetId, Type type)
    {
        return this.positionsRepository.sumPlannedByBudgetIdAndType(budgetId, type.name()).orElse(0.0);
    }

    public Budget readBudgetById(int id)
    {
        return this.repository.findById(id).orElseThrow();
    }

    public Budget createBudget(BudgetDTO current)
    {
        return this.repository.save(current.toBudget());
    }

    public boolean existsByMonthAndYear(int month, int year)
    {
        return this.repository.existsByMonthAndYear(month, year);
    }

    public void createPositionsListByBudget(Budget budget)
    {
        for (Category cat : this.categoryService.readAllCategories())
            this.positionsRepository.save(new BudgetPosition(cat, budget));
    }

    public BudgetsCountsDTO readBudgetsWithMaxMinTransactionCountsByListAsDto(List<Transaction> transactions)
    {
        var map = new HashMap<Integer, Integer>();

        for (Transaction t : transactions)
        {
            var k = t.getBill().getBudget().getHash();
            var v = map.get(k);

            if (!map.containsKey(k))
                map.put(k, 1);
            else
                map.replace(k, v + 1);
        }

        BudgetsCountsDTO data;

        if  (map.size() > 0)
        {
            var maxKey = Collections.max(map.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
            var minKey = Collections.min(map.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();

            data = new BudgetsCountsDTO(new BudgetDTO(readBudgetByHash(maxKey)), map.get(maxKey), new BudgetDTO(readBudgetByHash(minKey)), map.get(minKey));
        }
        else
            data = new BudgetsCountsDTO();

        return data;
    }

    public BudgetsSumsDTO readBudgetsWithMaxMinTransactionSumsByListAsDto(List<Transaction> transactions)
    {
        var map = new HashMap<Integer, Double>();

        for (Transaction t : transactions)
        {
            var k = t.getBill().getBudget().getHash();
            var v = t.getAmount();

            if (!map.containsKey(k))
                map.put(k, v);
            else
                map.replace(k, v + map.get(k));
        }

        BudgetsSumsDTO data;

        if  (map.size() > 0)
        {
            var maxKey = Collections.max(map.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
            var minKey = Collections.min(map.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();

            data = new BudgetsSumsDTO(new BudgetDTO(readBudgetByHash(maxKey)), map.get(maxKey), new BudgetDTO(readBudgetByHash(minKey)), map.get(minKey));
        }
        else
            data = new BudgetsSumsDTO();

        return data;
    }

    public List<BudgetPosition> readPositionsByBudgetId(int id)
    {
        return this.positionsRepository.findPositionsByBudgetId(id);
    }

    double sumTransactionsByPositionId(int id)
    {
        return this.transactionService.sumTransactionsByPositionId(id);
    }

    public List<BudgetDTO> readAllBudgetsDto()
    {
          return readAllBudgets().stream().map(BudgetDTO::new).collect(Collectors.toList());
    }

    private List<Budget> readAllBudgets()
    {
        return this.repository.findAll();
    }

    public Budget readBudgetByHash(Integer hash)
    {
        return this.repository.findByHash(hash);
    }

    public BudgetPosition readPositionByHash(Integer hash) {
        return this.positionsRepository.findByHash(hash).orElseThrow();
    }

    public List<BudgetPositionDTO> readPositionsDtoByBudgetAndType(Budget budget, Type type)
    {
        List<BudgetPositionDTO> dtos = new ArrayList<>();

        for (BudgetPosition bp : this.positionsRepository.findPositionsByBudgetIdAndType(budget.getId(), type.name()))
        {
            var p = new BudgetPositionDTO(bp);
            p.setActualAmount(sumTransactionsByPositionId(bp.getId()));

            dtos.add(p);
        }

        return dtos;
    }

    public BudgetPosition readPositionByBudgetHashAndCategory(Integer hash, Category category)
    {
        return this.positionsRepository.findByHashAndCategoryId(hash, category.getId());
    }

    public List<BudgetPosition> readPositionsByBudgetHashAndCategories(int hash, List<Integer> categoriesIds)
    {
        var positions = new ArrayList<BudgetPosition>();

        for (int id : categoriesIds)
            positions.add(this.positionsRepository.findByHashAndCategoryId(hash, id));

        return positions;
    }
//TODO obługa null?
    public Integer readNewestBudgetHash()
    {
        return this.repository.findNewestBudgetHash().orElse(null);
    }

    public double sumTransactionsByPositionAndMonth(BudgetPosition position, int month, int year) {
        return this.transactionService.sumTransactionsByPositionAndMonth(position, month, year);
    }

    public double sumTransactionsByPositionAndYear(BudgetPosition position, int year) {
        return this.transactionService.sumTransactionsByPositionAndYear(position, year);
    }

    public List<TransactionDTO> readTransactionsDtoByBudget(Budget budget)
    {
        return this.transactionService.readTransactionsDtoByBudget(budget);
    }

    public void checkPositionsByBudget(Budget budget)
    {
//        var positions = new HashMap<Category, BudgetPosition>();
////TODO obczaić wykoanie przez stream i Collector (todo-app?)
//        for (BudgetPosition p : readPositionsByBudgetId(budget.getId()))
//            positions.put(p.getCategory(), p);

        var positions = readPositionsByBudgetId(budget.getId()).stream()
                .collect(Collectors.toMap(BudgetPosition::getCategory, Function.identity()));

        for (Category cat : this.categoryService.readAllCategories())
            if  (!positions.containsKey(cat))
            {
                var pos = this.positionsRepository.save(new BudgetPosition(cat, budget));
                positions.put(cat, pos);
            }

        var transactions = this.transactionService.readTransactionsByBudgetId(budget.getId());

        for (Transaction t : transactions)
            if  (positions.containsKey(t.getCategory()))
                this.transactionService.updateBudgetDataInTransaction(t.getId(), positions.get(t.getCategory()));
    }

    public BudgetDTO readBudgetPlanAsDto(Budget b)
    {
        return buildBudgetDtoByBudgetData(b)
                .buildIncomesDto(readBudgetPositionsByBudgetIdAndTypeAsDto(b.getId(), Type.INCOME))
                .buildExpensesDto(readBudgetPositionsByBudgetIdAndTypeAsDto(b.getId(), Type.EXPENSE))
                .build();
    }

    private BudgetDTO.BudgetDtoBuilder buildBudgetDtoByBudgetData(Budget b)
    {
        return new BudgetDTO.BudgetDtoBuilder()
                .buildHash(b.getHash())
                .buildMonth(b.getMonth())
                .buildYear(b.getYear())
                .buildName()
                .buildMonthName()
                .buildDescription(b.getDescription());
    }

    private List<TransactionDTO> readTransactionsByBudgetIdAsDto(int budgetId)
    {
        return this.transactionService.readTransactionsByBudgetId(budgetId).stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    public BudgetDTO readBudgetByMonthAndYearAsDto(int month, int year)
    {
        var date = this.utilService.checkMonthValue(month, year);

        return new BudgetDTO(readBudgetByMonthAndYear(date[0], date[1]));
    }

    public void updatePlannedAmountInPositions(BudgetPositionsWrapperDTO current)
    {
        var list = new ArrayList<BudgetPositionDTO>();

        list.addAll(current.getIncomesList());
        list.addAll(current.getExpensesList());

        for (BudgetPositionDTO bp : list)
        {
            if (this.positionsRepository.existsByHash(bp.getHash()))
                this.positionsRepository.setPlannedAmountByPositionHash(bp.getPlannedAmount(), bp.getHash());
        }
    }

    public List<BudgetPositionDTO> readPositionsByBudgetIdAsDto(Integer budgetId)
    {
        return readPositionsByBudgetId(budgetId).stream().map(BudgetPositionDTO::new).collect(Collectors.toList());
    }

    public BudgetPositionsWrapperDTO readPositionsWrapperAsDto(int hash)
    {
        var positions = new BudgetPositionsWrapperDTO();

        positions.setIncomesList(readPositionsByBudgetHashAndTypeAsDto(hash, Type.INCOME));
        positions.setExpensesList(readPositionsByBudgetHashAndTypeAsDto(hash, Type.EXPENSE));

        return positions;
    }

    private List<BudgetPositionDTO> readPositionsByBudgetHashAndTypeAsDto(Integer hash, Type type)
    {
        return readPositionsByBudgetIdAndType(readBudgetIdByBudgetHash(hash), type).stream().map(BudgetPositionDTO::new).collect(Collectors.toList());
    }

    private List<BudgetPosition> readPositionsByBudgetIdAndType(int budgetId, Type type)
    {
        return this.positionsRepository.findPositionsByBudgetIdAndType(budgetId, type.name());
    }

    private List<BudgetPositionDTO> readPositionsByBudgetHashAsDto(Integer hash)
    {
        var budgetId = readBudgetIdByBudgetHash(hash);

        return readPositionsByBudgetId(budgetId).stream().map(BudgetPositionDTO::new).collect(Collectors.toList());
    }

    private int readBudgetIdByBudgetHash(Integer hash)
    {
        return this.repository.readBudgetIdByBudgetHash(hash).orElseThrow();
    }

    public ActualBudgetsWrapperDTO readActualBudgetsWrapper(int month, int year)
    {
        var wrapper = new ActualBudgetsWrapperDTO();

        wrapper.setActual(readBudgetByMonthAndYearAsDto(month, year));
        wrapper.setActualMinusOne(readBudgetByMonthAndYearAsDto(month - 1, year));
        wrapper.setActualMinusTwo(readBudgetByMonthAndYearAsDto(month - 2, year));
//        TODO jeszcze roczny i ogólny?

        return wrapper;
    }

    public YearBudgetDTO readYearBudgetByYearAsDto(int year)
    {
        var budget = new YearBudgetDTO();

        budget.setYear(year);

        var months = new ArrayList<BudgetDTO>();

        for (int i = 1; i <= 12; i++)
            months.add(readBudgetByMonthAndYearAsDto(i, year));

        budget.setMonths(months);

        return budget;
    }

    private List<BudgetDTO> readBudgetsByYearAsDto(int year)
    {
        return this.repository.findBudgetsByYear(year).stream().map(BudgetDTO::new).collect(Collectors.toList());
    }


}
