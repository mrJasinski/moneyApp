package com.moneyApp.bill;

import com.moneyApp.account.AccountService;
import com.moneyApp.bill.dto.*;
import com.moneyApp.category.CategoryService;
import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.payee.PayeeService;
import com.moneyApp.utils.Utils;
import com.moneyApp.vo.BillPositionSource;
import com.moneyApp.vo.CategorySource;
import com.moneyApp.vo.PayeeSource;
import com.moneyApp.vo.UserSource;
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

        var catNames = toSave.getPositions().stream().map(BillPositionDTO::getCategoryName).toList();

        var categories = this.categoryService.getCategoriesByNamesAndUserIdAsDto(catNames, userId);

        var payeeNames = toSave.getPositions().stream().map(BillPositionDTO::getGainerName).toList();

        var payees = this.payeeService.getPayeesByNamesAndUserIdAsDto(payeeNames, userId);

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
                        , new CategorySource(categories.stream().filter(c -> dto.getCategoryName().equals(c.getName())).toList().get(0).getId())
                        , new PayeeSource(payees.stream().filter(p -> dto.getGainerName().equals(p.getName())).toList().get(0).getId())
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
        var catIds = snap.getPositions().stream().map(BillPositionSnapshot::getCategory).map(CategorySource::getId).toList();

        var categories = this.categoryService.getCategoriesByIdsAsDto(catIds);

        var payeeIds = snap.getPositions().stream().map(BillPositionSnapshot::getGainer).map(PayeeSource::getId).toList();

        var payees = this.payeeService.getPayeesByIdsAsDto(payeeIds);

      return BillDTO.builder()
                .withNumber(snap.getNumber())
                .withDate(snap.getBillDate())
                .withPayeeName(this.payeeService.getPayeeNameById(snap.getPayee().getId()))
                .withAccountName(this.accountService.getAccountNameById(snap.getAccount().getId()))
                .withDescription(snap.getDescription())
                .withPositions(snap.getPositions()
                        .stream()
                        .map(bp -> BillPositionDTO.builder()
                                .withCategoryName(categories.stream().filter(c -> bp.getCategory().getId().equals(c.getId())).toList().get(0).getName())
                                .withGainerName(payees.stream().filter(p -> bp.getGainer().getId().equals(p.getId())).toList().get(0).getName())
                                .withAmount(bp.getAmount())
                                .withDescription(bp.getDescription())
                                .build()
                        ).collect(Collectors.toList()))
                .build();
    }

    BillDTO updateBillByNumberAndUserAsDto(final BillDTO toUpdate, final Long userId)
    {
        var bill = this.billQueryRepo.findByNumberAndUserId(toUpdate.getNumber(), userId);
//TODO
        return null;
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
//TODO do takich dto to pewnie by dobrze siadły projekcje?
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
                .orElseThrow(() -> new IllegalArgumentException("Bill with given number and user not found!"));
    }

    public Set<BillWithSumsDTO> getBudgetPositionsIdsWithSumsByBillPositionIds(final List<Long> billPosIds)
    {
        return this.billQueryRepo.findBudgetPositionsIdsWithSumsByBillPositionIds(billPosIds);
    }

    public List<BillPositionDTO> getBillPositionsByMonthYearAndUserIdAsDto(final LocalDate monthYear, final Long userId)
    {
        //                monthYear format YYYY-MM-1 so it can be used as first day of month
        var startDate = LocalDate.of(monthYear.getYear(), monthYear.getMonthValue(), monthYear.getDayOfMonth());
        var endDate = LocalDate.of(monthYear.getYear(), monthYear.getMonthValue(), monthYear.lengthOfMonth());

        var bills = this.billQueryRepo.findByDatesAndUserId(startDate, endDate, userId);
        var positions = new ArrayList<BillPositionDTO>();

        for (BillSnapshot b : bills)
            positions.addAll(b.getPositions().stream().map(this::toDto).toList());

        return positions;
    }

    BillPositionDTO toDto(BillPositionSnapshot snap)
    {
        //TODO test
        return BillPositionDTO.builder()
                .withId(snap.getId())
                .withCategory(this.categoryService.getCategoryByIdAsDto(snap.getCategory().getId()))
                .withAmount(snap.getAmount())
                .withGainerName(this.payeeService.getPayeeNameById(snap.getGainer().getId()))
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

    public BillActualAmounts getXXXNoNameYetByMonthYearAndUserId(final LocalDate monthYear, final Long userId)
    {
        var bills = this.billQueryRepo.getBillsBetweenDatesAndByUserId(Utils.getMonthYearStartDate(monthYear), Utils.getMonthYearEndDate(monthYear), userId);

        var incomes = 0d;

        var expenses = 0d;

        for (BillSnapshot b : bills)
        {
            for (BillPositionSnapshot p : b.getPositions())
            {
                var catType = this.categoryService.getCategoryTypeById(p.getCategory().getId());

                switch (catType)
                {
                    case INCOME -> incomes += p.getAmount();
                    case EXPENSE -> expenses += p.getAmount();
                }
            }
        }

        return new BillActualAmounts(incomes, expenses);
    }

    public List<BillPositionDTO> getBillPositionsWithoutAssignedBudgetPositionByMonthYearAndUserIdAsDto(final LocalDate monthYear, final Long userId)
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
}