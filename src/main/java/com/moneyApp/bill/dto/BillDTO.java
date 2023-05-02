package com.moneyApp.bill.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moneyApp.transaction.Transaction;
import com.moneyApp.transaction.dto.TransactionDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BillDTO
{
    private String number;  // numerKolejny_miesiącRok
    private LocalDate date;
    private String payeeName;
    private String accountName;
    private String description;
    private Double billSum; //suma transakcji w danym rachunku
    List<TransactionDTO> transactions;

    public BillDTO()
    {
    }

    public BillDTO(LocalDate date, String payeeName, String accountName, String description, List<TransactionDTO> transactions)
    {
        this.date = date;
        this.payeeName = payeeName;
        this.accountName = accountName;
        this.description = description;
        this.transactions = transactions;
    }

    public BillDTO(Long number, LocalDate date, String payeeName, String description, Set<Transaction> transactions)
    {
        this.date = date;
        this.number = String.format("%d_%s%s", number, this.date.getMonth().getValue(), getDate().getYear());
        this.payeeName = payeeName;
        this.description = description;
        this.billSum = transactions.stream().mapToDouble(Transaction::getAmount).sum();
        this.transactions = transactions.stream().map(Transaction::toDto).collect(Collectors.toList());
    }

    public String getNumber()
    {
        return this.number;
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

    public String getDescription()
    {
        return this.description;
    }

    public Double getBillSum()
    {
        return this.billSum;
    }

    public List<TransactionDTO> getTransactions()
    {
        return this.transactions;
    }
}
