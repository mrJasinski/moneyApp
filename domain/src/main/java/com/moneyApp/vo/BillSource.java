package com.moneyApp.vo;

public class BillSource
{
    private  Long id;

    protected BillSource()
    {
    }

    public BillSource(final Long id)
    {
        this.id = id;
    }

    Long getId()
    {
        return this.id;
    }
}
