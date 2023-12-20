package com.moneyApp.vo;

public class CategorySource
{
    private Long id;

    protected CategorySource()
    {
    }

    public CategorySource(final Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }

}