package com.moneyApp.vo;

public class SimpleAccount
{
    private String id;
    private String name;
    private String userId;

//    persistence constructor
    protected SimpleAccount()
    {

    }

    public SimpleAccount(
            final String id
            , final String name
            , final String userId)
    {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    String getUserId()
    {
        return this.userId;
    }
}
