package com.moneyApp.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

//TODO test
@Entity
public class SimpleBillPosition
{
    @Id
    private String id;
    private double amount;

    //    persistence constructor
    protected SimpleBillPosition()
    {

    }

    public SimpleBillPosition(final String id)
    {
        this.id = id;
    }

    String getId()
    {
        return this.id;
    }

    public double getAmount()
    {
        return this.amount;
    }
}
