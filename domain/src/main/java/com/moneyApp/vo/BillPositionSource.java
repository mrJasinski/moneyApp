package com.moneyApp.vo;

public class BillPositionSource
{
    private Long id;

    private BillPositionSource()
    {
    }

    public BillPositionSource(final Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }
}
