package com.moneyApp.payment;

public enum PaymentStatus
{
    PAID("Zapłacone"),
    UNPAID("Niezapłacone");

    private final String name;

    PaymentStatus(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
