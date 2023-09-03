package com.moneyApp.account.dto;

import com.moneyApp.account.Account;
import com.moneyApp.bill.Bill;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO
{
    private String name;    // nazwa konta
    private String description;
    private Double actualBalance;   // bieżące saldo konta
    List<BillDTO> bills;
    List<BillPositionDTO> transactions;

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

    public AccountDTO(Account account)
    {
        this.name = account.getName();
        this.description = account.getDescription();
        this.actualBalance = account.getActualBalance();
        this.bills = account.getBills()
                .stream()
                .map(Bill::toDto)
                .collect(Collectors.toList());
    }

    public AccountDTO(String name, String description, Double actualBalance, Set<Bill> bills)
    {
        this.name = name;
        this.description = description;
        this.actualBalance = actualBalance;
        this.bills = bills
                .stream()
                .map(Bill::toDto)
                .collect(Collectors.toList());
    }

    public AccountDTO(String name, String description, Double actualBalance, List<BillPositionDTO> transactions)
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

    public List<BillPositionDTO> getTransactions()
    {
        return this.transactions;
    }
}
