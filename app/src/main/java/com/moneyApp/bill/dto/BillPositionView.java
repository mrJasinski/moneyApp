package com.moneyApp.bill.dto;

import com.moneyApp.vo.CategorySource;
import com.moneyApp.vo.PayeeSource;

public interface BillPositionView
{
    long getNumber();
    Double getAmount();
    CategorySource getCategory();
    PayeeSource getGainer();
    String getDescription();
}