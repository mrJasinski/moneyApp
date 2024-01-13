package com.moneyApp.budget;

import com.moneyApp.bill.BillService;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import com.moneyApp.category.CategoryService;
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
        return prepareBudgetAsDto(getBudgetByNumberAndUserId(number, userId));
    }

    BudgetSnapshot prepareBudget(final BudgetSnapshot budget)
    {
        var userId = budget.getUserId();
        var monthYear = budget.getMonthYear();

        this.billService.updateBudgetInBillsByMonthYearAndUserId(monthYear, budget.getId(), userId);

        var billPositionsDto = this.billService.getBillPositionsWithoutBudgetPositionByMonthYearAndUserIdAsDto(monthYear, userId);

        if (!billPositionsDto.isEmpty())
        {
            var categoriesIdsFromBudgetPositions = extractCategoriesIdsFromBudgetPositions(budget.getPositions());

            var map = new HashMap<Long, Set<Long>>();

            for (BillPositionDTO bp : billPositionsDto)
            {
                var billPositionCategoryId = bp.getCategoryId();

                if (!categoriesIdsFromBudgetPositions.contains(billPositionCategoryId))
                {
                    var budPos = new BudgetPositionSnapshot(
                            0L
                            , new CategorySource(billPositionCategoryId)
                            , 0d
                            , ""
                            , budget
                            , Set.of(new BillPositionSource(bp.getId())));

                    budget.addPosition(budPos);

                    this.budgetRepo.save(budPos);
                }

                updateBudgetPositionsMap(budget.getPositions(), bp, map);
            }

            map.forEach(this.billService::updateBudgetPositionInBillPositionById);
        }

        return budget;
    }

    void updateBudgetPositionsMap(Set<BudgetPositionSnapshot> budgetPositions, BillPositionDTO  billPosition, Map<Long, Set<Long>> map)
    {
        var budgetPosition = budgetPositions
                .stream()
                .filter(p -> p.getCategory().getId().equals(billPosition.getCategoryId()))
                .toList()
                .get(0);

        var billPositionId = billPosition.getId();

        budgetPosition.addBillPositionSource(new BillPositionSource(billPositionId));

        var budgetPositionId = budgetPosition.getId();

        if (!map.containsKey(budgetPositionId))
            map.put(budgetPositionId, Set.of(billPositionId));
        else
            map.get(budgetPositionId).add(billPositionId);
    }

    List<Long> extractCategoriesIdsFromBudgetPositions(Set<BudgetPositionSnapshot> positions)
    {
        return positions
                .stream()
                .map(BudgetPositionSnapshot::getCategoryId)
                .toList();
    }

    BudgetDTO prepareBudgetAsDto(BudgetSnapshot budget)
    {
        return toDto(prepareBudget(budget));
    }

    BudgetSnapshot getBudgetByNumberAndUserId(String number, Long userId)
    {
        var monthYear = getBudgetMonthYearByNumber(number);

        return getBudgetByMonthYearAndUserId(monthYear, userId);
    }

    BudgetSnapshot getBudgetByMonthYearAndUserId(LocalDate monthYear, Long userId)
    {
        return this.budgetQueryRepo.findByMonthYearAndUserId(monthYear, userId)
                .orElseThrow(() -> new IllegalArgumentException("Budget for given monthYear and user id not found!"));
    }

    LocalDate getBudgetMonthYearByNumber(String number)
    {
//        check if budget number has correct format
        if (!number.matches("\\d+"))
            throw new IllegalArgumentException("Budget number has to contain digits only!");

        if (!(number.length() == 6))
            throw new IllegalArgumentException("Budget number should be six chars long!");

        var month = Integer.parseInt(number.substring(0, 2));
        var year = Integer.parseInt(number.substring(2, 6));

        return LocalDate.of(year, month, 1);
    }

    List<BudgetSnapshot> getBudgetsByUserId(Long userId)
    {
        return this.budgetQueryRepo.findAllByUserId(userId);
    }

    List<BudgetDTO> getBudgetsByUserIdAsDto(Long userId)
    {
        return getBudgetsByUserId(userId)
                .stream()
                .map(this::prepareBudgetAsDto)
                .collect(Collectors.toList());
    }

    BudgetDTO toDto(BudgetSnapshot budget)
    {
        var catIds = extractCategoriesIdsFromBudgetPositions(budget.getPositions());

        var categories = this.categoryService.getCategoriesByIdsAsDto(catIds);

        var billPosIds = new ArrayList<Long>();

        budget.getPositions().forEach(p -> p.addBillPositionSources(this.billService.getBillPositionSourcesByBudgetPositionId(p.getId())));

        for (BudgetPositionSnapshot bp : budget.getPositions())
            billPosIds.addAll(bp.getBillPositions()
                    .stream()
                    .map(BillPositionSource::getId)
                    .toList());

        var budPos = this.billService.getBudgetPositionsIdsWithSumsByBillPositionIds(billPosIds);

        return new BudgetDTO(
                budget.getMonthYear()
                , budget.getDescription()
                , budget.getPositions()
                    .stream()
                    .map(p -> new BudgetPositionDTO(
                            categories.stream().filter(c -> p.getCategory().getId().equals(c.getId())).toList().get(0)
                            , p.getPlannedAmount()
                            , budPos.stream().filter( b -> p.getId().equals(b.getBudgetPositionId())).toList().get(0).getSum()
                            , p.getDescription()
                    ))
                    .toList());
    }

    public BudgetDTO getBudgetByDateAndUserIdAsDto(LocalDate date, Long userId)
    {
        var monthYear = convertDateToMonthYear(date);

        try
        {
            var budget = getBudgetByMonthYearAndUserId(monthYear, userId);

            return prepareBudgetAsDto(budget);
        }
        catch (IllegalArgumentException ex)
        {
            return new BudgetDTO();
        }
    }

    LocalDate convertDateToMonthYear(final LocalDate date)
    {
        if (date.getDayOfMonth() != 1)
            return LocalDate.of(date.getYear(), date.getMonthValue(), 1);

        return date;
    }

    BudgetSnapshot createBudgetByUserId(final BudgetDTO toSave, final Long userId)
    {
//        check if budget for given month already exists in database for user
        if (this.budgetQueryRepo.existsByMonthYearAndUserId(toSave.getMonthYear(), userId))
            throw new IllegalArgumentException("Budget for given month and year already exists!");

        return this.budgetRepo.save(new BudgetSnapshot(
                0L
                , toSave.getMonthYear()
                , toSave.getDescription()
                , new UserSource(userId)
                , new HashSet<>()));
    }

    BudgetDTO createBudgetByUserIdAsDto(final BudgetDTO toSave, final Long userId)
    {
        return toDto(createBudgetByUserId(toSave, userId));
    }

    void deleteBudgetByNumberAndUserId(final String number, final Long userId)
    {
        this.budgetRepo.deleteByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId);
    }
}