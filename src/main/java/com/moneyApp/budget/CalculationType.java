package com.moneyApp.budget;

public enum CalculationType
{
    AVERAGE("Średnia arytmetyczna"),
    MEDIAN("Mediana");

    private final String name;

    CalculationType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
