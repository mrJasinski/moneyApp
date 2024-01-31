package com.moneyApp.bill;

import com.moneyApp.account.AccountService;
import com.moneyApp.bill.dto.*;
import com.moneyApp.category.CategoryService;
import com.moneyApp.payee.PayeeService;
import com.moneyApp.utils.Utils;
import com.moneyApp.vo.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.moneyApp.category.CategoryType.EXPENSE;

@Service
public class BillService
{
    private final BillRepository billRepo;
    private final BillQueryRepository billQueryRepo;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final PayeeService payeeService;


    BillService(
            final BillRepository billRepo
            , final BillQueryRepository billQueryRepo
            , final CategoryService categoryService
            , final PayeeService payeeService
            , final AccountService accountService)
    {
        this.billRepo = billRepo;
        this.billQueryRepo = billQueryRepo;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.payeeService = payeeService;
    }

    BillDTO createBillByUserIdAsDto(BillDTO toSave, Long userId)
    {
        return toDto(saveBill(toSave, userId));
    }

    BillDTO toDto(BillSnapshot snap)
    {
        var gainers = this.payeeService.getPayeesByIdsAsDto(snap.getGainerIds());

        return BillDTO.builder()
                .withId(snap.getId())
                .withNumber(snap.getNumber())
                .withDate(snap.getBillDate())
                .withPayeeName(this.payeeService.getPayeeNameById(snap.getPayeeId()))
                .withAccountName(this.accountService.getAccountNameById(snap.getAccountId()))
                .withDescription(snap.getDescription())
                .withPositions(snap.getPositions()
                        .stream()
                        .map(bp -> BillPositionDTO.builder()
                                .withId(bp.getId())
                                .withCategory(this.categoryService.getCategoryByIdAsDto(bp.getCategoryId()))
                                .withGainerName(gainers.stream().filter(g -> bp.getGainerId().equals(g.getId())).toList().get(0).getName())
                                .withAmount(bp.getAmount())
                                .withDescription(bp.getDescription())
                                .build()
                        ).collect(Collectors.toList()))
                .build();
    }

//    TODO konkretna nazwa
    BillSnapshot prepareBillToSave(BillDTO toSave, Long userId)
    {
        var payee = this.payeeService.getPayeeSourceByNameAndUserId(toSave.getPayeeName(), userId);
        var account = this.accountService.getAccountSourceByNameAndUserId(toSave.getAccountName(), userId);

        var gainerNames = toSave.getGainerNames();

        var gainers = this.payeeService.getPayeesByNamesAndUserIdAsDto(gainerNames, userId);

        var toSaveDate = toSave.getDate();

        long billId;
        String billNumber;
        BudgetSource budget;
        var oldBillSum = 0d;

        var monthYearMatches = false;

        try
        {
            var bill = getBillByNumberAndUserId(toSave.getNumber(), userId);

            var billDate = bill.getBillDate();
            monthYearMatches = checkIfMonthYearMatch(billDate, toSaveDate);

            billId = bill.getId();
            billNumber = monthYearMatches ? bill.getNumber() : setBillsCountAsBillNumber(toSaveDate, userId);
            budget = monthYearMatches ? bill.getBudget() : null;

            oldBillSum = sumBillPositionsAmounts(bill);
        }
        catch (IllegalArgumentException ex)
        {
            billId = 0L;
            billNumber = setBillsCountAsBillNumber(toSaveDate, userId);
            budget = null;
        }
//TODO jak to obejść?
        final boolean finalMonthYearMatches = monthYearMatches;

        var result = new BillSnapshot(
                billId
                , toSaveDate
                , billNumber
                , payee
                , account
                , budget
                , toSave.getDescription()
                , toSave.getPositions()
                .stream()
                .map(dto ->
                {
                    var index = toSave.getPositionIndex(dto);
                    //                                    TODO czy tu się też nie oprzeć o obiekt jak w przypadku category?
                    var gainerId = gainers.stream().filter(g -> dto.getGainerName().equals(g.getName())).toList().get(0).getId();

                    return prepareBillPosition(dto, finalMonthYearMatches, index, gainerId);
                })
                .collect(Collectors.toSet())
                , new UserSource(userId));

        var billSum = sumBillPositionsAmounts(result);
        updateAccountBalanceByBillSum(billSum, oldBillSum, account.getId());

        return result;
    }

    BillSnapshot saveBill(BillDTO toSave, Long userId)
    {
        return this.billRepo.save(prepareBillToSave(toSave, userId));
    }

    BillPositionSnapshot prepareBillPosition(BillPositionDTO dto, boolean monthYearMatches, int index, long gainerId)
    {
//        set to null in two cases
//        when it's newly created object and has no budget position assigned
//        when during update bill date was changed (only month or/and year are important) and bill will be assigned to another budget
        var budgetPosition = dto.getBudgetPosition();
        if (!monthYearMatches)
            budgetPosition = null;

        return new BillPositionSnapshot(
                dto.getId() == null ? 0L : dto.getId()
                , dto.getNumber() == null ? index + 1 : dto.getNumber()
                , dto.getAmount()
                , new CategorySource(dto.getCategoryId())
                , new PayeeSource(gainerId)
                , dto.getDescription()
                , budgetPosition);
    }

    String setBillsCountAsBillNumber(LocalDate toSaveDate, long userId)
    {
        //        +1 so lowest number (for first bill in month) is 1
        var count = getBillCountByMonthYearAndUserId(toSaveDate, userId) + 1;

        return String.valueOf(count);
    }

    boolean checkIfMonthYearMatch(LocalDate billDate, LocalDate toSaveDate)
    {
        return billDate.getYear() == toSaveDate.getYear() && billDate.getMonthValue() == toSaveDate.getMonthValue();
    }

    BillDTO updateBillByNumberAndUserAsDto(final BillDTO toUpdate, final Long userId)
    {
        return toDto(saveBill(toUpdate, userId));
    }

    Double sumBillPositionsAmounts(BillSnapshot bill)
    {
        var sum = bill.getBillSum();

        var type = this.categoryService.getCategoryTypeById(bill.getExemplaryCategoryId());

        if (EXPENSE.equals(type))
            sum = -sum;

        return sum;
    }

    void updateAccountBalanceByBillSum(double billSum, double oldBillSum, long accountId)
    {
        var sum = billSum - oldBillSum;

        if (sum != 0)
            this.accountService.updateAccountBalanceByAccountId(sum, accountId);
    }

    void deleteBillByNumberAndUserId(final String number, final Long userId)
    {
        this.billRepo.deleteByNumberAndUserId(number, userId);
    }

    List<BillSnapshot> getBillsByUserId(Long userId)
    {
        return this.billQueryRepo.findByUserId(userId);
    }

    List<BillDTO> getBillsByUserIdAsDto(Long userId)
    {
        return getBillsByUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    int getBillCountByMonthYearAndUserId(final LocalDate monthYear, final Long userId)
    {
        var startDate = Utils.getMonthYearStartDate(monthYear);
        var endDate = Utils.getMonthYearEndDate(monthYear);

        return this.billQueryRepo.findBillCountBetweenDatesAndUserId(startDate, endDate, userId);
    }

    BillDTO getBillByNumberAndUserIdAsDto(final String number, final Long userId)
    {
        return toDto(getBillByNumberAndUserId(number, userId));
    }

    boolean existsByNumberAndUserId(final String number, final Long userId)
    {
        return this.billQueryRepo.existsByNumberAndUserId(number, userId);
    }

    BillSnapshot getBillByNumberAndUserId(final String number, final Long userId)
    {
        return this.billQueryRepo.findByNumberAndUserId(number, userId)
                .orElseThrow(() -> new IllegalArgumentException("Bill with given number not found!"));
    }

    public Set<BillWithSumsDTO> getBudgetPositionsIdsWithSumsByBillPositionIds(final List<Long> billPosIds)
    {
        return this.billQueryRepo.findBudgetPositionsIdsWithSumsByBillPositionIds(billPosIds);
    }

    BillPositionDTO toDto(BillPositionSnapshot snap)
    {
        return BillPositionDTO.builder()
                .withId(snap.getId())
                .withCategory(this.categoryService.getCategoryByIdAsDto(snap.getCategoryId()))
                .withAmount(snap.getAmount())
                .withGainerName(this.payeeService.getPayeeNameById(snap.getGainerId()))
                .withDescription(snap.getDescription())
                .build();
    }

    public void updateBudgetPositionInBillPositionById(final Long budgetPositionId, final List<Long> billPositionIds)
    {
        this.billRepo.updateBudgetPositionInBillPositionByIds(budgetPositionId, Set.copyOf(billPositionIds));
    }

    Set<Long> getBillPositionIdsByBudgetPositionId(final long budPosId)
    {
        return this.billQueryRepo.findBillPositionIdsByBudgetPositionId(budPosId);
    }

    public Set<BillPositionSource> getBillPositionSourcesByBudgetPositionId(final Long budPosId)
    {
        return getBillPositionIdsByBudgetPositionId(budPosId)
                .stream()
                .map(BillPositionSource::new)
                .collect(Collectors.toSet());
    }

    List<BillSnapshot> getBillsByMonthYearAndUserId(final LocalDate monthYear, final long userId)
    {
        var startDate = Utils.getMonthYearStartDate(monthYear);
        var endDate = Utils.getMonthYearEndDate(monthYear);

        return this.billQueryRepo.findByDatesAndUserId(startDate, endDate, userId);
    }

    List<BillPositionSnapshot> getBillPositionsWithoutBudgetPositionByMonthYearAndUserId(final LocalDate monthYear, final Long userId)
    {
        var bills = getBillsByMonthYearAndUserId(monthYear, userId);
        var positions = new ArrayList<BillPositionSnapshot>();

        for (BillSnapshot b : bills)
            positions.addAll(b.filterPositionsWithoutBudgetPositions());

        return positions;
    }

    public List<BillPositionDTO> getBillPositionsWithoutBudgetPositionByMonthYearAndUserIdAsDto(final LocalDate monthYear, final Long userId)
    {
        return getBillPositionsWithoutBudgetPositionByMonthYearAndUserId(monthYear, userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    List<BillSnapshot> filterBillsWithoutBudgetAssigned(final LocalDate monthYear, final Long userId)
    {
        return getBillsByMonthYearAndUserId(monthYear, userId)
            .stream()
            .filter(b -> b.getBudget() == null)
            .toList();
    }

    public void updateBudgetInBillsByMonthYearAndUserId(final LocalDate monthYear, final long budgetId, final Long userId)
    {
        var billIds = filterBillsWithoutBudgetAssigned(monthYear, userId)
                .stream()
                .map(BillSnapshot::getId)
                .toList();

        this.billRepo.updateBudgetInBills(budgetId, billIds);
    }
}