package com.moneyApp.bill;

import com.moneyApp.account.AccountService;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.budget.BudgetService;
import com.moneyApp.category.CategoryService;
import com.moneyApp.payee.PayeeService;
import com.moneyApp.vo.UserSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BillService
{
    private final AccountService accountService;
//    private final BudgetService budgetService;
    private final CategoryService categoryService;
    private final PayeeService payeeService;
    private final BillRepository billRepo;
    private final BillQueryRepository billQueryRepo;

    BillService(final CategoryService categoryService
            , final PayeeService payeeService
            , final AccountService accountService
//            , final BudgetService budgetService
            , final BillRepository billRepo
            , final BillQueryRepository billQueryRepo)
    {
        this.accountService = accountService;
//        this.budgetService = budgetService;
        this.categoryService = categoryService;
        this.payeeService = payeeService;
        this.billRepo = billRepo;
        this.billQueryRepo = billQueryRepo;
    }

    BillDTO createBillByUserId(BillDTO toSave, Long userId)
    {
        var user = new UserSource(userId);
        var payee = this.payeeService.getPayeeSourceByNameAndUserId(toSave.getPayeeName(), userId);
        var account = this.accountService.getAccountSourceByNameAndUserId(toSave.getAccountName(), userId);
//        var budget = this.budgetService.getBudgetSourceByDateAndUserId(toSave.getDate(), userId);

        var date = toSave.getDate();
        var startDate = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        var endDate = LocalDate.of(date.getYear(), date.getMonthValue(), date.lengthOfMonth());

        var number = this.billQueryRepo.findBillCountBetweenDatesAndUserId(startDate, endDate, userId) + 1;


        var bill = this.billRepo.save(Bill.restore(new BillSnapshot(
                0L
                , String.format("%s%s_%s", toSave.getDate().getYear(), toSave.getDate().getMonthValue(), number)
                , toSave.getDate()
                , payee
                , account
                , null
                , toSave.getDescription()
                , toSave.getPositions()
                    .stream()
                    .map(dto -> new BillPositionSnapshot(null
                        , toSave.getPositions().indexOf(dto) + 1
                        , dto.getAmount()
                        , this.categoryService.getCategorySourceByNameAndUserId(dto.getCategoryName(), userId)
                        , this.payeeService.getPayeeSourceByNameAndUserId(dto.getGainerName(), userId)
                        , dto.getDescription()
                        ,null))
                    .collect(Collectors.toSet())
                , user)));


        var sum = sumPositionsAmounts(bill.getSnapshot().getPositions());


        this.accountService.updateAccountBalanceByAccountId(sum, account.getId());

        return toDto(bill.getSnapshot());
    }

    BillDTO toDto(BillSnapshot snap)
    {
//TODO test
      return BillDTO.builder()
                .withNumber(snap.getNumber())
                .withDate(snap.getBillDate())
//                .withPayeeName(snap.getPayee().getName())
                .withPayeeName(this.payeeService.getPayeeNameById(snap.getPayee().getId()))
                .withAccountName(snap.getAccount().getName())
                .withDescription(snap.getDescription())
                .withPositions(snap.getPositions().stream().map(this::toDto).collect(Collectors.toList()))
                .build();
    }

    BillPositionDTO toDto(BillPositionSnapshot snap)
    {
        //TODO test
        return BillPositionDTO.builder()
                .withCategoryName(snap.getCategory().getName())
                .withAmount(snap.getAmount())
//                .withAmount(snap.getGainer().getName())
                .withGainerName(this.payeeService.getPayeeNameById(snap.getGainer().getId()))
                .withDescription(snap.getDescription())
                .build();
    }

    double sumPositionsAmounts(Set<BillPositionSnapshot> positions)
    {
        var sum = 0;

        for (BillPositionSnapshot bp : positions)
            switch (this.categoryService.getCategoryTypeByCategoryId(bp.getCategory().getId()))
            {
                case EXPENSE :  sum -= bp.getAmount();
                case INCOME : sum += bp.getAmount();
            }

        return sum;
    }

    BillDTO updateBillByNumberAndUserAsDto(final BillDTO toUpdate, final Long userId)
    {
        var bill = this.billQueryRepo.findByNumberAndUserId(toUpdate.getNumber(), userId);

        return null;
    }

    void deleteBillByNumberAndUserId(final String number, final Long userId)
    {
        this.billRepo.deleteByNumberAndUserId(number, userId);
    }
//
////    public void updatePositionInTransaction(Bill.Position billPosition, Long budgetPositionId, long userId)
////    {
////        billPosition.setBudgetPosition(new SimpleBudgetPosition(String.valueOf(budgetPositionId)));
////
////        this.billRepo.updatePositionIdInDb(billPosition.getId(), budgetPositionId, userId);
////    }

//    long getHighestBillNumberByMonthYearAndUserId(LocalDate date, long userId)
//    {
//        return this.billQueryRepo
//                .findHighestBillNumberByMonthYearAndUserId(date.getMonth().getValue(), date.getYear(), userId)
//                .orElse(0L);
//    }

    List<BillDTO> getBillsByUserIdAsDto(Long userId)
    {
        return this.billQueryRepo.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

//    long getHighestPositionNumberByBillId(long billId)
//    {
////        read from db the highest position number in given bill
////        if no number found then assign 0
//        return this.billQueryRepo
//                .findHighestNumberByBillId(billId)
//                .orElse(0L);
//    }

    Integer getBillCountByMonthYearAndUserId(final LocalDate date, final Long userId)
    {
        var startDate = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        var endDate = LocalDate.of(date.getYear(), date.getMonthValue(), date.lengthOfMonth());

        return this.billQueryRepo.findBillCountBetweenDatesAndUserId(startDate, endDate, userId);
    }
//TODO do takich dto to pewnie by dobrze siadły projekcje?
    BillDTO getBillByNumberAndUserIdAsDto(final String number, final Long userId)
    {
        var bill = this.billQueryRepo.findByNumberAndUserId(number, userId)
                .orElseThrow(() -> new IllegalArgumentException("Bill with given number and user id not found!"));

        return BillDTO.builder()
                .withNumber(bill.getNumber())
                .withDate(bill.getBillDate())
                .withPayeeName(this.payeeService.getPayeeNameById(bill.getPayee().getId()))
                .withAccountName(this.accountService.getAccountNameById(bill.getAccount().getId()))
                .withDescription(bill.getDescription())
//                .withPositions(getBillPositionsByBillIdAsDto(bill.getId()))
                .build();
    }


//    List<BillPositionDTO> getBillPositionsByBillIdAsDto(final Long billId)
//    {
//        var result = new ArrayList<BillPositionDTO>();
//
//       for (BillPositionView bp :  this.billQueryRepo.findPositionsByBillIdAsView(billId))
//       {
//           result.add(BillPositionDTO.builder()
//                   .withCategoryName(this.categoryService.getCategoryNameById(Long.parseLong(bp.getCategory().getId())))
//                   .withAmount(bp.getAmount())
//                   .withGainerName(this.payeeService.getPayeeNameById(Long.parseLong(bp.getGainer().getId())))
//                   .withDescription(bp.getDescription())
//                   .build());
//       }
//
//        return result;
//    }

//    private List<BillPositionSnapshot> getBillPositionsByBillId(final Long billId)
//    {
//        return this.billQueryRepo.findPositionsByBillId(billId);
//    }

    boolean existsByNumberAndUserId(final String number, final Long userId)
    {
        return this.billQueryRepo.existsByNumberAndUserId(number, userId);
    }

    BillSnapshot getBillByNumberAndUserId(final String number, final Long userId)
    {
        return this.billQueryRepo.findByNumberAndUserId(number, userId)
                .orElseThrow(() -> new IllegalArgumentException("Bill with given number and user not found!"));
    }

    public Double getBillPositionsSumByBudgetPositionId(final Long budgetPositionId)
    {
//        TODO czy przy braku pozycji z automatu sum wypluje zero? sprawdzić przy okazji testów
        return this.billQueryRepo.findBillPositionsSumByBudgetPositionId(budgetPositionId);
//                .orElse(0d);
    }
//
//    List<BillPositionDTO> getBillPositionsWithoutBudgetPositionByDateAndUserIdAsDto(LocalDate startDate, LocalDate endDate, Long userId)
//    {
//        return this.billQueryRepo
//                .findBillPositionsBetweenDatesWithoutBudgetPositionByUserId(startDate, endDate, userId);
//    }
//
//    List<BillPositionDTO> getBillPositionsWithoutBudgetPositionsByUserIdAsDto(Long userId)
//    {
//        return this.billQueryRepo
//                .findBillPositionsWithoutBudgetPositionByUserId(userId);
//    }
//
//    List<BillPositionDTO> getBillPositionsByDatesAndUserIdAsDto(LocalDate startDate, LocalDate endDate, Long userId)
//    {
//        return this.billQueryRepo
//                .findBillPositionsBetweenDatesAndUserId(startDate, endDate, userId);
//    }
//
//    List<BillPositionDTO> getBillPositionsByMonthYearAndUserIdAsDto(LocalDate monthYear, long userId)
//    {
//        //        monthYear as starting date because of format YYYY-MM-1
//
//        if (monthYear.getDayOfMonth() != 1)
//            monthYear = LocalDate.of(monthYear.getYear(), monthYear.getMonth().getValue(), 1);
//
//        return this.billQueryRepo
//                .findBillPositionsBetweenDatesAndUserId(monthYear
//                        , LocalDate.of(monthYear.getYear()
//                        , monthYear.getMonth().getValue()
//                        , monthYear.lengthOfMonth())
//                        , userId);
//    }
}