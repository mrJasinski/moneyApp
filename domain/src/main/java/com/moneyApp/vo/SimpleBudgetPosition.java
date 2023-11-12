package com.moneyApp.vo;

public class SimpleBudgetPosition
{
    private String id;

    //    persistence constructor
    protected SimpleBudgetPosition()
    {

    }

    public SimpleBudgetPosition(final String id)
    {
        this.id = id;
    }

    String getId()
    {
        return this.id;
    }
}
