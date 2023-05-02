package com.moneyApp.category;

public enum CategoryType
{
    INCOME("Przychód"),
    EXPENSE("Wydatek");

    private String name;

    CategoryType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
