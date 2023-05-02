package com.moneyApp.account.dto;

import com.moneyApp.account.Account;
import com.moneyApp.bill.Bill;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.transaction.dto.TransactionDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO
{
    private String name;    // nazwa konta
    private String description;
    private Double actualBalance;   // bieżące saldo konta
    List<BillDTO> bills;
    List<TransactionDTO> transactions;

    public AccountDTO()
    {
    }

    public AccountDTO(String name, Double actualBalance)
    {
        this.name = name;
        this.actualBalance = actualBalance;
    }

    public AccountDTO(String name, String description, Double actualBalance)
    {
        this.name = name;
        this.description = description;
        this.actualBalance = actualBalance;
    }

    public AccountDTO(String name, String description, Double actualBalance, Set<Bill> bills)
    {
        this.name = name;
        this.description = description;
        this.actualBalance = actualBalance;
        this.bills = bills.stream().map(Bill::toDto).collect(Collectors.toList());
    }

    public AccountDTO(String name, String description, Double actualBalance, List<TransactionDTO> transactions)
    {
        this.name = name;
        this.description = description;
        this.actualBalance = actualBalance;
        this.transactions = transactions;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Double getActualBalance()
    {
        return this.actualBalance;
    }

    public List<BillDTO> getBills()
    {
        return this.bills;
    }

    public List<TransactionDTO> getTransactions()
    {
        return this.transactions;
    }
}
