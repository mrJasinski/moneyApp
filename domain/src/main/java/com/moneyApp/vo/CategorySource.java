package com.moneyApp.vo;

public class CategorySource
{
    private Long id;
    private String name;

    protected CategorySource()
    {
    }

    public CategorySource(final Long id)
    {
        this.id = id;
    }

    CategorySource(final Long id, final String name)
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