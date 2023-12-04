package com.moneyApp.bill;

import com.moneyApp.account.AccountService;
import com.moneyApp.category.CategoryService;
import com.moneyApp.payee.PayeeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
class BillQueryService
{
    private final BillQueryRepository billQueryRepo;
    private final PayeeService payeeService;
    private final AccountService accountService;
    private final CategoryService categoryService;

    BillQueryService(
            final BillQueryRepository billQueryRepo
            , final PayeeService payeeService
            , final AccountService accountService
            , final CategoryService categoryService)
    {
        this.billQueryRepo = billQueryRepo;
        this.payeeService = payeeService;
        this.accountService = accountService;
        this.categoryService = categoryService;
    }


}
