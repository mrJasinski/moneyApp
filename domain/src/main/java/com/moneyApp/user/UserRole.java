package com.moneyApp.user;

public enum UserRole
{
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;

    UserRole(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
