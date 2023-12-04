package com.moneyApp.bill.dto;

import com.moneyApp.vo.AccountSource;
import com.moneyApp.vo.BudgetSource;
import com.moneyApp.vo.PayeeSource;
import com.moneyApp.vo.UserSource;

import java.time.LocalDate;

public interface BillView
{
    String getNumber();
    LocalDate getBillDate();
    PayeeSource getPayee();
    AccountSource getAccount();
    BudgetSource getBudget();
    String getDescription();

//    TODO chwilowo
//    Set<BillPositionSnapshot> getBillPositions();

    UserSource getUser();
}
