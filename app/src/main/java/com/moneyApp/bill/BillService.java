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

    BillDTO createBillByUserId(BillDTO toSave, Long userId)
    {
        var payee = this.payeeService.getPayeeSourceByNameAndUserId(toSave.getPayeeName(), userId);
        var account = this.accountService.getAccountSourceByNameAndUserId(toSave.getAccountName(), userId);

//        +1 so lowest number (for first bill in month) is 1
        var count = getBillCountByMonthYearAndUserId(toSave.getDate(), userId) + 1;

        var gainerNames = toSave.getPositions()
                            .stream()
                            .map(BillPositionDTO::getGainerName)
                            .toList();

        var gainers = this.payeeService.getPayeesByNamesAndUserIdAsDto(gainerNames, userId);

        var bill = this.billRepo.save(new BillSnapshot(
                null
                , toSave.getDate()
                , String.valueOf(count)
                , payee
                , account
                , null
                , toSave.getDescription()
                , toSave.getPositions()
                    .stream()
                    .map(dto -> new BillPositionSnapshot(
                            null
                        , toSave.getPositions().indexOf(dto) + 1
                        , dto.getAmount()
                        , new CategorySource(dto.getCategory().getId())
                        , new PayeeSource(gainers.stream().filter(p -> dto.getGainerName().equals(p.getName())).toList().get(0).getId())
                        , dto.getDescription()
                        ,null))
                    .collect(Collectors.toSet())
                , new UserSource(userId)));

//TODO aktualizacja stanu konta w dopiero gdy jest ono wywoływane? bo tak ot tez update powinien wywoływać i aktualizować o róznicę jeśli odszło do zmian
        var billSum = sumBillPositionsAmounts(bill);
        this.accountService.updateAccountBalanceByAccountId(billSum, account.getId());

        return toDto(bill);
    }

    BillDTO toDto(BillSnapshot snap)
    {
        var gainerIds = snap.getPositions()
                        .stream()
                        .map(BillPositionSnapshot::getGainerId)
                        .toList();

        var gainers = this.payeeService.getPayeesByIdsAsDto(gainerIds);

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
    BillDTO returnBillDTO(BillDTO toSave, Long userId)
    {
        var payee = this.payeeService.getPayeeSourceByNameAndUserId(toSave.getPayeeName(), userId);
        var account = this.accountService.getAccountSourceByNameAndUserId(toSave.getAccountName(), userId);

        var gainerNames = toSave.getPositions()
                            .stream()
                            .map(BillPositionDTO::getGainerName)
                            .toList();

        var gainers = this.payeeService.getPayeesByNamesAndUserIdAsDto(gainerNames, userId);

        var toSaveDate = toSave.getDate();

        long billId;
        int count;
        String billNumber;
        BudgetSource budget;
        var oldBillSum = 0d;

        var monthYearMatches = false;

        try
        {
            var bill = getBillByNumberAndUserId(toSave.getNumber(), userId);

            billId = bill.getId();
            billNumber = bill.getNumber();
            budget = bill.getBudget();
            var billDate = bill.getBillDate();

            oldBillSum = sumBillPositionsAmounts(bill);

            monthYearMatches = checkIfMonthYearMatch(billDate, toSaveDate);

            if (!monthYearMatches)
            {
                //        +1 so lowest number (for first bill in month) is 1
                count = getBillCountByMonthYearAndUserId(toSaveDate, userId) + 1;
                billNumber = String.valueOf(count);
                budget = null;
            }
        }
        catch (IllegalArgumentException ex)
        {
            billId = 0L;
            count = getBillCountByMonthYearAndUserId(toSaveDate, userId) + 1;
            billNumber = String.valueOf(count);
            budget = null;
        }

        var result = this.billRepo.save(new BillSnapshot(
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
//                            null jest w przypadku gdy zmienia się data -> patrz flaga oraz nowoutworzonego
                            BudgetPositionSource budgetPosition = null;
                            if (monthYearMatches)
                                budgetPosition = bill.getPositions().stream().toList().get(Math.toIntExact(dto.getId())).getBudgetPosition();

                            return new BillPositionSnapshot(
                                    dto.getId() == null ? 0L : dto.getId()
                                    , dto.getNumber() == null ? toSave.getPositions().indexOf(dto) + 1 : dto.getNumber()
                                    , dto.getAmount()
                                    , new CategorySource(dto.getCategory().getId())
//                                    TODO czy tu się też nie oprzeć o obiekt jak w przypadku category?
                                    , new PayeeSource(gainers.stream().filter(p -> dto.getGainerName().equals(p.getName())).toList().get(0).getId())
                                    , dto.getDescription()
                                    , budgetPosition
                            );
                        })
                .collect(Collectors.toSet())
                , new UserSource(userId)
        ));

        var billSum = sumBillPositionsAmounts(result);
        updateAccountBalanceByBillSum(billSum, oldBillSum, account.getId());

        return toDto(result);
    }

    boolean checkIfMonthYearMatch(LocalDate billDate, LocalDate toSaveDate)
    {
        return billDate.getYear() == toSaveDate.getYear() && billDate.getMonthValue() == toSaveDate.getMonthValue();
    }


    BillDTO updateBillByNumberAndUserAsDto(final BillDTO toUpdate, final Long userId)
    {
        var bill = getBillByNumberAndUserId(toUpdate.getNumber(), userId);

        var oldBillSum = sumBillPositionsAmounts(bill);

        var gainerNames = toUpdate.getPositions()
                .stream()
                .map(BillPositionDTO::getGainerName)
                .toList();

        var gainers = this.payeeService.getPayeesByNamesAndUserIdAsDto(gainerNames, userId);

        var billNumber = toUpdate.getNumber();
        var budget = bill.getBudget();

        if (bill.getBillDate().getYear() != toUpdate.getDate().getYear() || bill.getBillDate().getMonthValue() != toUpdate.getDate().getMonthValue())
        {
            var count = getBillCountByMonthYearAndUserId(toUpdate.getDate(), userId) + 1;

            billNumber = String.valueOf(count);
            budget = null;
        }

        var payee = this.payeeService.getPayeeSourceByNameAndUserId(toUpdate.getPayeeName(), userId);
        var account = this.accountService.getAccountSourceByNameAndUserId(toUpdate.getAccountName(), userId);

        var result = this.billRepo.save(new BillSnapshot(
                bill.getId()
                , toUpdate.getDate()
                , billNumber
                , payee
                , account
                , budget
                , toUpdate.getDescription()
                , toUpdate.getPositions()
                    .stream()
                    .map(dto ->
                    {
                        var budgetPosition = bill.getPositions().stream().toList().get(Math.toIntExact(dto.getId())).getBudgetPosition();

                        if (bill.getBillDate().getYear() != toUpdate.getDate().getYear() || bill.getBillDate().getMonthValue() != toUpdate.getDate().getMonthValue())
                            budgetPosition = null;

                       return new BillPositionSnapshot(
                                dto.getId()
                                , dto.getNumber()
                                , dto.getAmount()
                                , new CategorySource(dto.getCategory().getId())
                                , new PayeeSource(gainers.stream().filter(g -> dto.getGainerName().equals(g.getName())).toList().get(0).getId())
                                , dto.getDescription()
                                , budgetPosition);
                    })
                .collect(Collectors.toSet())
                , bill.getUser()
        ));

        var billSum = sumBillPositionsAmounts(bill);

        updateAccountBalanceByBillSum(billSum, oldBillSum, account.getId());

        return toDto(result);
    }

    Double sumBillPositionsAmounts(BillSnapshot bill)
    {
        var sum = bill.getPositions()
                .stream()
                .mapToDouble(BillPositionSnapshot::getAmount)
                .sum();

        var type = this.categoryService.getCategoryTypeById(bill.getPositions()
                                                                    .stream()
                                                                    .toList()
                                                                    .get(0).getCategory().getId());

        if (EXPENSE.equals(type))
            sum = -sum;

        return sum;
    }

    void updateAccountBalanceByBillSum(double billSum, double oldBillSum, long accountId)
    {
        if (oldBillSum != billSum)
        {
            var sum = billSum - oldBillSum;

            this.accountService.updateAccountBalanceByAccountId(sum, accountId);
        }
    }

    void deleteBillByNumberAndUserId(final String number, final Long userId)
    {
        this.billRepo.deleteByNumberAndUserId(number, userId);
    }

    List<BillSnapshot> getBillSByUserId(Long userId)
    {
        return this.billQueryRepo.findByUserId(userId);
    }

    List<BillDTO> getBillsByUserIdAsDto(Long userId)
    {
        return getBillSByUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    Integer getBillCountByMonthYearAndUserId(final LocalDate date, final Long userId)
    {
        var startDate = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        var endDate = LocalDate.of(date.getYear(), date.getMonthValue(), date.lengthOfMonth());

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

    public void updateBudgetPositionInBillPositionById(final Long budgetPositionId, final Set<Long> billPositionIds)
    {
        this.billRepo.updateBudgetPositionInBillPositionByIds(budgetPositionId, billPositionIds);
    }

    public Set<BillPositionSource> getBillPositionSourcesByBudgetPositionId(final Long budPosId)
    {
        return this.billQueryRepo.findBillPositionIdsByBudgetPositionId(budPosId)
                .stream()
                .map(BillPositionSource::new)
                .collect(Collectors.toSet());
    }

    List<BillPositionSnapshot> getBillPositionsWithoutBudgetPositionByMonthYearAndUserId(final LocalDate monthYear, final Long userId)
    {
        var startDate = Utils.getMonthYearStartDate(monthYear);
        var endDate = Utils.getMonthYearEndDate(monthYear);

        var bills = this.billQueryRepo.findByDatesAndUserId(startDate, endDate, userId);
        var positions = new ArrayList<BillPositionSnapshot>();

        for (BillSnapshot b : bills)
            positions.addAll(b.getPositions()
                    .stream()
                    .filter(p -> p.getBudgetPosition() == null)
                    .toList());

        return positions;
    }

    public List<BillPositionDTO> getBillPositionsWithoutBudgetPositionByMonthYearAndUserIdAsDto(final LocalDate monthYear, final Long userId)
    {
        return getBillPositionsWithoutBudgetPositionByMonthYearAndUserId(monthYear, userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public void updateBudgetInBillsByMonthYearAndUserId(final LocalDate monthYear, final long budgetId, final Long userId)
    {
        var startDate = Utils.getMonthYearStartDate(monthYear);
        var endDate = Utils.getMonthYearEndDate(monthYear);

        var billIds = this.billQueryRepo.findByDatesAndUserId(startDate, endDate, userId)
                .stream()
                .filter(b -> b.getBudget() == null)
                .map(BillSnapshot::getId)
                .toList();

        this.billRepo.updateBudgetInBills(budgetId, billIds);
    }
}