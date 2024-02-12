package com.moneyApp.bill;

import com.moneyApp.vo.BudgetPositionSource;
import com.moneyApp.vo.CategorySource;
import com.moneyApp.vo.PayeeSource;

class BillPositionSnapshot
{
    private Long id;
    private long number;  // subsequent number in given bill
    private Double amount;
    private CategorySource category;
    private PayeeSource gainer;
    private String description;
    private BudgetPositionSource budgetPosition;

//    persistence constructor
    public BillPositionSnapshot()
    {
    }

    BillPositionSnapshot(
            final Long id
            , final long number
            , final Double amount
            , final CategorySource category
            , final PayeeSource gainer
            , final String description
            , final BudgetPositionSource budgetPosition)
    {
        this.id = id;
        this.number = number;
        this.amount = amount;
        this.category = category;
        this.gainer = gainer;
        this.description = description;
        this.budgetPosition = budgetPosition;
    }

    Long getId()
    {
        return this.id;
    }

    long getNumber()
    {
        return this.number;
    }

    Double getAmount()
    {
        return this.amount;
    }

    CategorySource getCategory()
    {
        return this.category;
    }

    Long getCategoryId()
    {
        return this.category.getId();
    }

    PayeeSource getGainer()
    {
        return this.gainer;
    }

    Long getGainerId()
    {
        return this.gainer.getId();
    }

    String getDescription()
    {
        return this.description;
    }

    BudgetPositionSource getBudgetPosition()
    {
        return this.budgetPosition;
    }

    long getBudgetPositionId()
    {
        return this.budgetPosition.getId();
    }
}
