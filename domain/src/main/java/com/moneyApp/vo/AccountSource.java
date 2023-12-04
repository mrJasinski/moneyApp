package com.moneyApp.vo;


public class AccountSource
{
    private Long id;
    private String name;

    protected AccountSource()
    {
    }

    public AccountSource(final Long id)
    {
        this.id = id;
    }

    AccountSource(final Long id, final String name)
    {
        this.id = id;
        this.name = name;
    }

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }
}