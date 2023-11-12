package com.moneyApp.payment.dto;

import com.moneyApp.payment.PaymentDate;
import com.moneyApp.payment.PaymentFrequency;
import com.moneyApp.payment.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

public class PaymentDTO
{
    private LocalDate date;
    private LocalDate startDate;
    private PaymentFrequency frequencyType;
    private int frequency;
    private String description;
    private double amount;
    private PaymentStatus isPaid;
    private List<PaymentDate> dates;

    public PaymentDTO()
    {
    }

    public PaymentDTO(LocalDate date, String description, double amount, boolean isPaid)
    {
        this.date = date;
        this.description = description;
        this.amount = amount;

        if (isPaid)
            this.isPaid = PaymentStatus.PAID;
        else
            this.isPaid = PaymentStatus.UNPAID;
    }

    public PaymentDTO(LocalDate startDate, PaymentFrequency frequencyType, int frequency, String description, double amount)
    {
        this.startDate = startDate;
        this.frequencyType = frequencyType;
        this.frequency = frequency;
        this.description = description;
        this.amount = amount;
    }

    public PaymentDTO(LocalDate startDate, PaymentFrequency frequencyType, int frequency, String description, double amount, List<PaymentDate> dates)
    {
        this.startDate = startDate;
        this.frequencyType = frequencyType;
        this.frequency = frequency;
        this.description = description;
        this.amount = amount;
        this.dates = dates;
    }

    public LocalDate getDate()
    {
        return this.date;
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

    public PaymentStatus getIsPaid()
    {
        return this.isPaid;
    }

    public List<PaymentDate> getDates()
    {
        return this.dates;
    }
}
