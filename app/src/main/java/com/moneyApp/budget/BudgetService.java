package com.moneyApp.budget;

import com.moneyApp.bill.BillService;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import com.moneyApp.category.CategoryService;
import com.moneyApp.category.dto.CategoryWithIdAndNameAndTypeDTO;
import com.moneyApp.vo.BillPositionSource;
import com.moneyApp.vo.CategorySource;
import com.moneyApp.vo.UserSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

        var catNames = budget.getPositions().stream().map(BudgetPositionSnapshot::getCategory).map(CategorySource::getId).toList();

        var cats = this.categoryService.getCategoriesIdsByUserId(userId).stream().filter(c -> !catNames.contains(c)).toList();

        var categories = this.categoryService.getCategoriesByIdsAsDto(cats);

        var billPositions = this.billService.getBillPositionsByMonthYearAndUserIdAsDto(budget.getMonthYear(), userId);

        var positions = new ArrayList<BudgetPositionDTO>();

//        generowanie pozycji budżetu na podstawie kategorii
        for (CategoryWithIdAndNameAndTypeDTO c : categories)
        {
            var amount = billPositions.stream().filter(bp -> bp.getCategoryName().equals(c.getName())).mapToDouble(BillPositionDTO::getAmount).sum();

            positions.add(new BudgetPositionDTO(c.getName(), c.getType(), 0d, amount, ""));
        }

        var posSnaps = new HashSet<BudgetPositionSnapshot>();

        positions.forEach(p -> posSnaps.add(saveBudgetPositionInDb(new BudgetPositionSnapshot(
                null
                , new CategorySource(categories.stream().filter(c -> c.getName().equals(p.getCategoryName())).toList().get(0).getId())
                , p.getPlannedAmount()
                , p.getDescription()
                , budget
                , billPositions
                .stream()
                .filter(bp -> bp.getCategoryName().equals(p.getCategoryName()))
                .map(BillPositionDTO::getId)
                .map(BillPositionSource::new)
                .collect(Collectors.toSet())
        ))));

        if (!posSnaps.isEmpty())
            budget.addPositions(posSnaps);

        var catList = this.categoryService.getCategoriesIdsAndNamesByUserIdAsDto(userId);

        budget.getPositions().forEach(p ->
        {
            var dto = new BudgetPositionDTO(
                    catList.stream().filter(c -> c.getId().equals(p.getCategory().getId())).toList().get(0).getName()
                    , p.getPlannedAmount());

            this.billService.updateBudgetPositionInBillPositionById(p.getId(),
                    billPositions
                            .stream()
                            .filter(bp -> bp.getCategoryName().equals(dto.getCategoryName()))
                            .map(BillPositionDTO::getId).toList());
        });

        return budget;
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

    BudgetDTO createBudgetByUserIdAsDto(final BudgetDTO toSave, final Long userId)
    {
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

    void deleteBudgetByNumberAndUserId(final String number, final Long userId)
    {
        this.budgetRepo.deleteByMonthYearAndUserId(getBudgetMonthYearByNumber(number), userId);
    }
}