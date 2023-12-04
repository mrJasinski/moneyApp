package com.moneyApp.vo;

public class UserSource
{
    private Long id;

    protected UserSource()
    {
    }

    public UserSource(final Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }
}
