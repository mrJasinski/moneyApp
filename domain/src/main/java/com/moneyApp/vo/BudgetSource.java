package com.moneyApp.vo;

public class BudgetSource
{
    private Long id;

    protected BudgetSource()
    {
    }

    public BudgetSource(final Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }
}
