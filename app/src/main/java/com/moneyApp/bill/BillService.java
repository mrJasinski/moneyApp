package com.moneyApp.bill;

import com.moneyApp.account.AccountService;
import com.moneyApp.bill.dto.*;
import com.moneyApp.category.CategoryService;
import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.payee.PayeeService;
import com.moneyApp.utils.Utils;
import com.moneyApp.vo.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillService
{
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final PayeeService payeeService;
    private final BillRepository billRepo;
    private final BillQueryRepository billQueryRepo;

    BillService(final CategoryService categoryService
            , final PayeeService payeeService
            , final AccountService accountService
            , final BillRepository billRepo
            , final BillQueryRepository billQueryRepo)
    {
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.payeeService = payeeService;
        this.billRepo = billRepo;
        this.billQueryRepo = billQueryRepo;
    }

    BillDTO createBillByUserId(BillDTO toSave, Long userId)
    {
        var payee = this.payeeService.getPayeeSourceByNameAndUserId(toSave.getPayeeName(), userId);
        var account = this.accountService.getAccountSourceByNameAndUserId(toSave.getAccountName(), userId);

//        +1 so lowest number (for first bill in month) is 1
        var number = getBillCountByMonthYearAndUserId(toSave.getDate(), userId) + 1;

        var gainerNames = toSave.getPositions()
                            .stream()
                            .map(BillPositionDTO::getGainerName)
                            .toList();

        var gainers = this.payeeService.getPayeesByNamesAndUserIdAsDto(gainerNames, userId);

        var bill = this.billRepo.save(new BillSnapshot(
                null
                , String.format("%s%s_%s", toSave.getDate().getYear(), toSave.getDate().getMonthValue(), number)
                , toSave.getDate()
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

        var dto = toDto(bill);

        this.accountService.updateAccountBalanceByAccountId(dto.getBillSum(), account.getId());

        return dto;
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

    BillDTO updateBillByNumberAndUserAsDto(final BillDTO toUpdate, final Long userId)
    {
        var bill = getBillByNumberAndUserId(toUpdate.getNumber(), userId);

        var gainerNames = toUpdate.getPositions()
                .stream()
                .map(BillPositionDTO::getGainerName)
                .toList();

        var gainers = this.payeeService.getPayeesByNamesAndUserIdAsDto(gainerNames, userId);

        var billNumber = toUpdate.getNumber();
        var budget = bill.getBudget();

        if (bill.getBillDate().getYear() != toUpdate.getDate().getYear() || bill.getBillDate().getMonthValue() != toUpdate.getDate().getMonthValue())
        {
            var number = getBillCountByMonthYearAndUserId(toUpdate.getDate(), userId) + 1;

            billNumber = String.format("%s%s_%s", toUpdate.getDate().getYear(), toUpdate.getDate().getMonthValue(), number);
            budget = null;
        }

        return toDto(this.billRepo.save(new BillSnapshot(
                bill.getId()
                , billNumber
                , toUpdate.getDate()
                , this.payeeService.getPayeeSourceByNameAndUserId(toUpdate.getPayeeName(), userId)
                , this.accountService.getAccountSourceByNameAndUserId(toUpdate.getAccountName(), userId)
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
        )));
    }

    void deleteBillByNumberAndUserId(final String number, final Long userId)
    {
        this.billRepo.deleteByNumberAndUserId(number, userId);
    }

    List<BillDTO> getBillsByUserIdAsDto(Long userId)
    {
        return this.billQueryRepo.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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

    public void updateBudgetPositionInBillPositionById(final Long budgetPositionId, final List<Long> billPositionIds)
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

    public List<BillPositionDTO> getBillPositionsWithoutBudgetPositionByMonthYearAndUserIdAsDto(final LocalDate monthYear, final Long userId)
    {
        var startDate = Utils.getMonthYearStartDate(monthYear);
        var endDate = Utils.getMonthYearEndDate(monthYear);

        var bills = this.billQueryRepo.findByDatesAndUserId(startDate, endDate, userId);
        var positions = new ArrayList<BillPositionDTO>();

        for (BillSnapshot b : bills)
            positions.addAll(b.getPositions()
                    .stream()
                    .filter(p -> p.getBudgetPosition() == null)
                    .map(this::toDto)
                    .toList());

        return positions;
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