package com.moneyApp.payment.dto;

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
    private PaymentStatus status;
    private List<PaymentPositionDTO> positions;
//    TODO tylko dla testu - zmienić na coś rozsądnego choćby dlatego że nazwy mogą się dublować
    private String linkDesc;

    public PaymentDTO()
    {
    }

    public PaymentDTO(LocalDate date, String description, double amount, boolean isPaid)
    {
        this.date = date;
        this.description = description;
        this.amount = amount;

        if (isPaid)
            this.status = PaymentStatus.PAID;
        else
            this.status = PaymentStatus.UNPAID;
    }

    public PaymentDTO(LocalDate startDate, PaymentFrequency frequencyType, int frequency, String description, double amount)
    {
        this.startDate = startDate;
        this.frequencyType = frequencyType;
        this.frequency = frequency;
        this.description = description;
        this.amount = amount;
    }

    public PaymentDTO(
            final LocalDate startDate
            , final PaymentFrequency frequencyType
            , final int frequency
            , final String description
            , final double amount
            , final List<PaymentPositionDTO> positions)
    {
        this.startDate = startDate;
        this.frequencyType = frequencyType;
        this.frequency = frequency;
        this.description = description;
        this.amount = amount;
        this.positions = positions;
        this.linkDesc = this.description.replaceAll(" ", "-");
    }

    //    public PaymentDTO(LocalDate startDate, PaymentFrequency frequencyType, int frequency, String description, double amount, List<PaymentDate> dates)
//    {
//        this.startDate = startDate;
//        this.frequencyType = frequencyType;
//        this.frequency = frequency;
//        this.description = description;
//        this.amount = amount;
//        this.dates = dates;
//    }

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

    public PaymentStatus getStatus()
    {
        return this.status;
    }

    public List<PaymentPositionDTO> getPositions()
    {
        return this.positions;
    }

    public String getLinkDesc()
    {
        return this.linkDesc;
    }
}
