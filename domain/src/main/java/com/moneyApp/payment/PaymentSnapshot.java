package com.moneyApp.payment;

import com.moneyApp.vo.UserSource;

import java.time.LocalDate;
import java.util.HashSet;
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
    private Set<PaymentPositionSnapshot> positions = new HashSet<>();

    //    persistence constructor
    protected PaymentSnapshot()
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
        this.frequency = checkFrequency(frequency);
        this.description = description;
        this.amount = amount;
        this.user = user;
        addPositions(positions);
    }

    int checkFrequency(int frequency)
    {
        if (this.frequencyType.equals(PaymentFrequency.ONCE))
            return this.frequency = 1;

        return frequency;
    }

    void addPositions(Set<PaymentPositionSnapshot> positions)
    {
        if (this.positions == null)
            this.positions = new HashSet<>();

        this.positions.addAll(positions);
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
