package com.moneyApp.vo;

public class BudgetPositionSource
{
    private Long id;

    protected BudgetPositionSource()
    {
    }

    public BudgetPositionSource(final Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }
}
