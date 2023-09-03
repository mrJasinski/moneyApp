package com.moneyApp.bill.dto;

import com.moneyApp.bill.BillPosition;

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
    List<BillPositionDTO> transactions;

    public BillDTO()
    {
    }

    public BillDTO(LocalDate date, String payeeName, String accountName, String description, List<BillPositionDTO> transactions)
    {
        this.date = date;
        this.payeeName = payeeName;
        this.accountName = accountName;
        this.description = description;
        this.transactions = transactions;
    }

    public BillDTO(Long number, LocalDate date, String payeeName, String description, Set<BillPosition> billPositions)
    {
        this.date = date;
        this.number = String.format("%d_%s%s", number, this.date.getMonth().getValue(), getDate().getYear());
        this.payeeName = payeeName;
        this.description = description;
        this.billSum = billPositions.stream().mapToDouble(BillPosition::getAmount).sum();
        this.transactions = billPositions.stream().map(BillPosition::toSimpleDto).collect(Collectors.toList());
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

    public List<BillPositionDTO> getTransactions()
    {
        return this.transactions;
    }
}
