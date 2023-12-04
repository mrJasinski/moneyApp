package com.moneyApp.payment;

import com.moneyApp.vo.UserSource;

import java.time.LocalDate;
import java.util.Set;

class PaymentSnapshot
{
    private Long id;
    private LocalDate startDate;
    private PaymentFrequency frequencyType;
    private int frequency;
    private String description;
    private double amount;
    private UserSource user;
    private Set<PaymentPositionSnapshot> positions;

    //    persistence constructor
    public PaymentSnapshot()
    {
    }

    PaymentSnapshot(
            final Long id
            , final LocalDate startDate
            , final PaymentFrequency frequencyType
            , final int frequency
            , final String description
            , final double amount
            , final UserSource user
            , final Set<PaymentPositionSnapshot> positions)
    {
        this.id = id;
        this.startDate = startDate;
        this.frequencyType = frequencyType;
        this.frequency = frequency;
        this.description = description;
        this.amount = amount;
        this.user = user;
        this.positions = positions;
    }

    public Long getId()
    {
        return this.id;
    }

    public LocalDate getStartDate()
    {
        return this.startDate;
    }

    public PaymentFrequency getFrequencyType()
    {
        return this.frequencyType;
    }

    public int getFrequency()
    {
        return this.frequency;
    }

    public String getDescription()
    {
        return this.description;
    }

    public double getAmount()
    {
        return this.amount;
    }

    public UserSource getUser()
    {
        return this.user;
    }

    public Set<PaymentPositionSnapshot> getPositions()
    {
        return this.positions;
    }
}
