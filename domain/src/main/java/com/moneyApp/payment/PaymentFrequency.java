package com.moneyApp.payment;

public enum PaymentFrequency
{
    ONCE("Jednorazowa"),
    WEEKLY("Tygodniowa"),
    MONTHLY("Miesięcza"),
    QUARTERLY("Kwartalna"),
    YEARLY("Roczna");

    private final String name;

    PaymentFrequency(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
