package com.moneyApp.vo;

public
class PayeeSource
{
    private Long id;

    protected PayeeSource()
    {
    }

    public PayeeSource(final Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }
}
