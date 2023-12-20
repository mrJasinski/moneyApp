package com.moneyApp.budget;

import com.moneyApp.bill.BillService;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import com.moneyApp.category.CategoryService;
import com.moneyApp.category.CategoryType;
import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.category.dto.CategoryWithIdAndNameAndTypeDTO;
import com.moneyApp.vo.BillPositionSource;
import com.moneyApp.vo.CategorySource;
import com.moneyApp.vo.UserSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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


//        w zasadzie to istotne jest tylko aby utworzyć pozycje na podstawie billPositions z rachunków w danym miesiącu
//        - aby bez sensu nie tworzyć pustych pozycji dla samego ich tworzenia

//        czyli potrzebuję wyciągnąć billPositions bez przypisanego budgetPosition
//        jeśli istnieje budgetPos z daną kategorią to go dodać
//        jeśli nie to utworzyć budgetPosition i zapisać do bazy a przy okazji uaktualnić budgetPos w billPos


        var billPositionsDto = this.billService.getBillPositionsWithoutAssignedBudgetPositionByMonthYearAndUserIdAsDto(budget.getMonthYear(), userId);

        if (!billPositionsDto.isEmpty())
        {
            var categoriesIdFromBudgetPositions = budget.getPositions()
                                                    .stream()
                                                    .map(BudgetPositionSnapshot::getCategory)
                                                    .map(CategorySource::getId)
                                                    .toList();
//            generowanie nowych pozycji budzetu na podstawie kategorii z billPositions
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


                    budgetPosition.addBillPositionSource(new BillPositionSource(bp.getId()));
                }
                else
                    budget.addPosition(new BudgetPositionSnapshot(
                            null
                            , new CategorySource(billPositionCategoryId)
                            , 0d
                            , null
                            , budget
                            , Set.of(new BillPositionSource(bp.getId()))
                    ));
            }
        }
//TODO brak zapisu do bazy nowych pozycji budżetowych i zaktualizowanych billPos
//
//
//        var categoriesIdsFromPositions = budget.getPositions()
//                .stream()
//                .map(BudgetPositionSnapshot::getCategory)
//                .map(CategorySource::getId)
//                .toList();
//
//        var categories = this.categoryService.getCategoriesByIdsAndUserIdAsDto(categoriesIdsFromPositions, userId);
//
//        var billPositions = this.billService.getBillPositionsByMonthYearAndUserIdAsDto(budget.getMonthYear(), userId);
//
//        var positionsDto = new ArrayList<BudgetPositionDTO>();
//
////        generowanie pozycji budżetu na podstawie kategorii
//        for (CategoryWithIdAndNameAndTypeDTO c : categories)
//        {
//            var actualAmount = billPositions
//                    .stream()
//                    .filter(bp -> bp.getCategoryName().equals(c.getName()))
//                    .mapToDouble(BillPositionDTO::getAmount)
//                    .sum();
//
//            positionsDto.add(new BudgetPositionDTO(c.getName(), c.getType(), 0d, actualAmount, ""));
//        }
//
//        var posSnaps = new HashSet<BudgetPositionSnapshot>();
//
//        positionsDto.forEach(dto -> posSnaps.add(saveBudgetPositionInDb(new BudgetPositionSnapshot(
//                null
//                , new CategorySource(categories.stream().filter(c -> c.getName().equals(dto.getCategoryName()))
//                    .toList()
//                    .get(0).getId())
//                , dto.getPlannedAmount()
//                , dto.getDescription()
//                , budget
//                , billPositions
//                    .stream()
//                    .filter(bp -> bp.getCategoryName().equals(dto.getCategoryName()))
//                    .map(BillPositionDTO::getId)
//                        .map(BillPositionSource::new)
//                    .collect(Collectors.toSet())
//        ))));
//
//        if (!posSnaps.isEmpty())
//            budget.addPositions(posSnaps);
//
//        var catList = this.categoryService.getCategoriesIdsAndNamesByUserIdAsDto(userId);
//
//        budget.getPositions().forEach(p ->
//        {
//            var dto = new BudgetPositionDTO(
//                    catList
//                            .stream()
//                            .filter(c -> c.getId().equals(p.getCategory().getId()))
//                            .toList()
//                            .get(0).getName()
//                    , p.getPlannedAmount());
//
//            this.billService.updateBudgetPositionInBillPositionById(p.getId(),
//                    billPositions
//                            .stream()
//                            .filter(bp -> bp.getCategoryName().equals(dto.getCategoryName()))
//                            .map(BillPositionDTO::getId)
//                            .toList());
//        });

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
        var monthYear = convertDateToMonthYear(date);

        if (!this.budgetQueryRepo.existsByMonthYearAndUserId(monthYear, userId))
            throw new IllegalArgumentException("Budget for given monthYear not found!");

        var actualValues = this.billService.getXXXNoNameYetByMonthYearAndUserId(monthYear, userId);
//        TODO
//        wyjątek
//        też sprawdzenie czy nie trzeba utworzyć pozycji budżetu
        var budget = this.budgetQueryRepo.findByMonthYearAndUserId(monthYear, userId)
                .orElseThrow(() -> new IllegalArgumentException("Exception!"));

        var plannedIncomes = budget.getPositions()
                .stream()
                .filter(p -> this.categoryService.getCategoryTypeById(p.getCategory().getId()).equals(CategoryType.INCOME))
                .mapToDouble(BudgetPositionSnapshot::getPlannedAmount)
                .sum();

        var plannedExpenses = budget.getPositions()
                .stream()
                .filter(p -> this.categoryService.getCategoryTypeById(p.getCategory().getId()).equals(CategoryType.EXPENSE))
                .mapToDouble(BudgetPositionSnapshot::getPlannedAmount)
                .sum();

        return new BudgetDTO(
                date
                , plannedIncomes
                , actualValues.incomes()
                , plannedExpenses
                , actualValues.expenses());
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