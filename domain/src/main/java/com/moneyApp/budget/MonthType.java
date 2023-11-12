package com.moneyApp.budget;

public enum MonthType
{
    LAST_MONTH("Poprzedni miesiąc"),
    ADEQUATE_MONTH("Adekwatny miesiąc");

    private final String name;

    MonthType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
