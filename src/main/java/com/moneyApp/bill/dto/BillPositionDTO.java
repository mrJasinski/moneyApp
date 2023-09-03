package com.moneyApp.bill.dto;

import java.time.LocalDate;

public class BillPositionDTO
{
    private Double amount;
    private String categoryName;
    private String gainerName;
    private String description;
    private LocalDate date;
    private String payeeName;
    private String accountName;

    public BillPositionDTO()
    {
    }

    public BillPositionDTO(Double amount, String categoryName, String gainerName, String description)
    {
        this.amount = amount;
        this.categoryName = categoryName;
        this.gainerName = gainerName;
        this.description = description;
    }

    public BillPositionDTO(LocalDate date, String payeeName, String accountName, Double amount, String categoryName, String gainerName, String description)
    {
        this(amount, categoryName, gainerName, description);
        this.date = date;
        this.payeeName = payeeName;
        this.accountName = accountName;
    }

    public Double getAmount()
    {
        return this.amount;
    }

    public String getCategoryName()
    {
        return this.categoryName;
    }

    public String getGainerName()
    {
        return this.gainerName;
    }

    public String getDescription()
    {
        return this.description;
    }

    public LocalDate getDate()
    {
        return this.date;
    }

    public String getPayeeName()
    {
        return this.payeeName;
    }

    public String getAccountName()
    {
        return this.accountName;
    }
}
