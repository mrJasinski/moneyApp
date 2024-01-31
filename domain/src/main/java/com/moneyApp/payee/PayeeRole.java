package com.moneyApp.payee;

public enum PayeeRole
{
//    TODO ładne nazwy PL
    PAYEE("Kontrahent"),
    GAINER("Beneficjent"),
    BOTH("Oba");

    private final String name;

    PayeeRole(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
