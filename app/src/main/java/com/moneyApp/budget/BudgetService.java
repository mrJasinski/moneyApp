package com.moneyApp.budget;

import com.moneyApp.bill.BillService;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import com.moneyApp.category.CategoryService;
import com.moneyApp.category.CategoryType;
import com.moneyApp.vo.BillPositionSource;
import com.moneyApp.vo.CategorySource;
import com.moneyApp.vo.UserSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BudgetService
{
    private final BudgetRepository budgetRepo;
    private final BudgetQueryRepository budgetQueryRepo;
    private final BillService billService;
    private final CategoryService categoryService;

    BudgetService(BudgetRepository budgetRepo
            , BudgetQueryRepository budgetQueryRepo
            , BillService billService
            , CategoryService categoryService)
    {
        this.budgetRepo = budgetRepo;
        this.budgetQueryRepo = budgetQueryRepo;
        this.billService = billService;
        this.categoryService = categoryService;
    }

    BudgetDTO getBudgetByNumberAndUserIdAsDto(String number, Long userId)
    {
        return toDto(prepareBudget(getBudgetByNumberAndUserId(number, userId)));
    }

    BudgetSnapshot prepareBudget(final BudgetSnapshot budget)
    {
        var userId = budget.getUser().getId();
        var monthYear = budget.getMonthYear();

        this.billService.updateBudgetInBillsByMonthYearAndUserId(monthYear, budget.getId(), userId);

        var billPositionsDto = this.billService.getBillPositionsWithoutBudgetPositionByMonthYearAndUserIdAsDto(monthYear, userId);

        if (!billPositionsDto.isEmpty())
        {
            var categoriesIdFromBudgetPositions = budget.getPositions()
                                                    .stream()
                                                    .map(BudgetPositionSnapshot::getCategory)
                                                    .map(CategorySource::getId)
                                                    .toList();

            var map = new HashMap<Long, List<Long>>();

            for (BillPositionDTO bp : billPositionsDto)
            {
                var billPositionCategoryId = bp.getCategory().getId();

                if (categoriesIdFromBudgetPositions.contains(billPositionCategoryId))
                {
                    var budgetPosition = budget.getPositions()
                            .stream()
                            .filter(p -> p.getCategory().getId().equals(billPositionCategoryId)).
                            toList()
                            .get(0);

                    var billPositionId = bp.getId();

                    budgetPosition.addBillPositionSource(new BillPositionSource(billPositionId));

                    var budgetPositionId = budgetPosition.getId();

                    if (!map.containsKey(budgetPosition.getId()))
                        map.put(budgetPositionId, List.of(billPositionId));
                    else
                        map.get(budgetPositionId).add(billPositionId);
                }
                else
                    budget.addPosition(new BudgetPositionSnapshot(
                            0L
                            , new CategorySource(billPositionCategoryId)
                            , 0d
                            , ""
                            , budget
                            , Set.of(new BillPositionSource(bp.getId()))
                    ));
            }

            var positionsToSave = budget.getPositions()
                                    .stream()
                                    .filter(p -> p.getId() < 1)
                                    .toList();

//            TODO zapis listy do bazy - najlepiej inny niż iteracji i zapis pojedynczego obiektu
            positionsToSave.forEach(this::saveBudgetPositionInDb);

            map.forEach(this.billService::updateBudgetPositionInBillPositionById);
        }

        return budget;
    }

    void saveBudgetPositionsInDb(List<BudgetPositionSnapshot> toSave)
    {
        this.budgetRepo.save(toSave);
    }

    private BudgetPositionSnapshot saveBudgetPositionInDb(BudgetPositionSnapshot toSave)
    {
        return this.budgetRepo.save(toSave);
    }

    BudgetSnapshot getBudgetByNumberAndUserId(String number, Long userId)
    {
        var monthYear = getBudgetMonthYearByNumber(number);

        return this.budgetQueryRepo.findByMonthYearAndUserId(monthYear, userId)
                .orElseThrow(() -> new IllegalArgumentException("Budget for given monthYear and user id not found!"));
    }

    LocalDate getBudgetMonthYearByNumber(String number)
    {
//        check if budget number has correct format (6 digits)
        if (!(number.length() == 6 || !number.matches("\\d+")))
            throw new IllegalArgumentException("Wrong budget number!");

        var month = Integer.parseInt(number.substring(0, 2));
        var year = Integer.parseInt(number.substring(2, 6));

        return LocalDate.of(year, month, 1);
    }

    List<BudgetDTO> getBudgetsByUserIdAsDto(Long userId)
    {
        return this.budgetQueryRepo.findAllByUserId(userId)
                .stream()
                .map(this::prepareBudget)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    BudgetDTO toDto(BudgetSnapshot budget)
    {
        var catIds = budget.getPositions().stream().map(BudgetPositionSnapshot::getCategory).map(CategorySource::getId).toList();

        var categories = this.categoryService.getCategoriesByIdsAsDto(catIds);

        var billPosIds = new ArrayList<Long>();

        budget.getPositions().forEach(p -> p.addBillPositionSources(this.billService.getBillPositionSourcesByBudgetPositionId(p.getId())));

        for (BudgetPositionSnapshot bp : budget.getPositions())
                for (BillPositionSource b : bp.getBillPositions())
                    billPosIds.add(b.getId());

        var budPos = this.billService.getBudgetPositionsIdsWithSumsByBillPositionIds(billPosIds);

        return new BudgetDTO(
                budget.getMonthYear()
                , budget.getDescription()
                , budget.getPositions()
                    .stream()
                    .map(p -> new BudgetPositionDTO(
                            categories.stream().filter(c -> p.getCategory().getId().equals(c.getId())).toList().get(0).getName()
                            , categories.stream().filter(c -> p.getCategory().getId().equals(c.getId())).toList().get(0).getType()
                            , p.getPlannedAmount()
                            , budPos.stream().filter( b -> p.getId().equals(b.getBudgetPositionId())).toList().get(0).getSum()
                            , p.getDescription()
                    ))
                    .toList());
    }

    public BudgetDTO getBudgetByDateAndUserIdAsDto(LocalDate date, Long userId)
    {
        var monthYear = convertDateToMonthYear(date);

        var budget = this.budgetQueryRepo.findByMonthYearAndUserId(monthYear, userId)
                .orElseGet(BudgetSnapshot::new);

        if (budget.getId() == null)
            return new BudgetDTO();

//        if (!this.budgetQueryRepo.existsByMonthYearAndUserId(monthYear, userId))
//            throw new IllegalArgumentException("Budget for given monthYear not found!");

//        var actualValues = this.billService.getXXXNoNameYetByMonthYearAndUserId(monthYear, userId);
////        TODO
////        wyjątek
////        też sprawdzenie czy nie trzeba utworzyć pozycji budżetu
//        var budget = this.budgetQueryRepo.findByMonthYearAndUserId(monthYear, userId)
//                .orElseThrow(() -> new IllegalArgumentException("Exception!"));
//
//        var plannedIncomes = budget.getPositions()
//                .stream()
//                .filter(p -> this.categoryService.getCategoryTypeById(p.getCategory().getId()).equals(CategoryType.INCOME))
//                .mapToDouble(BudgetPositionSnapshot::getPlannedAmount)
//                .sum();
//
//        var plannedExpenses = budget.getPositions()
//                .stream()
//                .filter(p -> this.categoryService.getCategoryTypeById(p.getCategory().getId()).equals(CategoryType.EXPENSE))
//                .mapToDouble(BudgetPositionSnapshot::getPlannedAmount)
//                .sum();
//
//        return new BudgetDTO(
//                date
//                , plannedIncomes
//                , actualValues.incomes()
//                , plannedExpenses
//                , actualValues.expenses());

        return toDto(prepareBudget(budget));
    }

    private LocalDate convertDateToMonthYear(final LocalDate date)
    {
        if (date.getDayOfMonth() != 1)
            return LocalDate.of(date.getYear(), date.getMonthValue(), 1);

        return date;
    }

    BudgetDTO createBudgetByUserIdAsDto(final BudgetDTO toSave, final Long userId)
    {
//        check if budget for given month already exists in database for user
        if (this.budgetQueryRepo.existsByMonthYearAndUserId(toSave.getMonthYear(), userId))
           throw new IllegalArgumentException("Budget for given month and year already exists!");

        return toDto(this.budgetRepo.save(new BudgetSnapshot(
                0L
                , toSave.getMonthYear()
                , toSave.getDescription()
                , new UserSource(userId)
                , new HashSet<>())));
    }

    void deleteBudgetByNumberAndUserId(final String number, final Long userId)
    {
        this.budgetRepo.deleteByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId);
    }
}