package com.moneyApp.transaction.dto;

import com.moneyApp.transaction.Transaction;

public class TransactionDTO
{
    private Double amount;
    private String categoryName;
    private String gainerName;
    private String description;

    public TransactionDTO()
    {
    }

    public TransactionDTO(Double amount, String categoryName, String gainerName, String description)
    {
        this.amount = amount;
        this.categoryName = categoryName;
        this.gainerName = gainerName;
        this.description = description;
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
}
