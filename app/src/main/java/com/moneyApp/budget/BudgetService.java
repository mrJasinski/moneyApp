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
            var categoriesIdsFromBudgetPositions = budget.getPositions()
                                                    .stream()
                                                    .map(BudgetPositionSnapshot::getCategoryId)
                                                    .toList();

            var map = new HashMap<Long, List<Long>>();

            for (BillPositionDTO bp : billPositionsDto)
            {
                var billPositionCategoryId = bp.getCategoryId();

                if (categoriesIdsFromBudgetPositions.contains(billPositionCategoryId))
                {
                    updateBudgetPositionsMap(budget.getPositions(), bp, map);
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

            saveCreatedBudgetPositionsInDb(budget.getPositions());

            map.forEach(this.billService::updateBudgetPositionInBillPositionById);
        }

        return budget;
    }

    void updateBudgetPositionsMap(Set<BudgetPositionSnapshot> budgetPositions, BillPositionDTO  billPosition, Map<Long, List<Long>> map)
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
            map.put(budgetPositionId, new ArrayList<>(List.of(billPositionId)));
        else
            map.get(budgetPositionId).add(billPositionId);
    }

    BudgetDTO prepareBudgetAsDto(BudgetSnapshot budget)
    {
        return toDto(prepareBudget(budget));
    }

    void saveCreatedBudgetPositionsInDb(Set<BudgetPositionSnapshot> positions)
    {
        filterPositionsToBeSaved(positions)
                .forEach(this.budgetRepo::save);
    }

    List<BudgetPositionSnapshot> filterPositionsToBeSaved(Set<BudgetPositionSnapshot> positions)
    {
        return positions
                .stream()
                .filter(p -> p.getId() < 1)
                .toList();
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

    List<BudgetDTO> getBudgetsByUserIdAsDto(Long userId)
    {
        return this.budgetQueryRepo.findAllByUserId(userId)
                .stream()
                .map(this::prepareBudgetAsDto)
                .collect(Collectors.toList());
    }

    BudgetDTO toDto(BudgetSnapshot budget)
    {
        var catIds = budget.getPositions()
                .stream()
                .map(BudgetPositionSnapshot::getCategoryId)
                .toList();

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