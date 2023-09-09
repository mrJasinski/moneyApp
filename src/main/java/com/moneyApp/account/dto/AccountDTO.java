package com.moneyApp.account.dto;

import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionDTO;

import java.util.List;

public class AccountDTO
{
    static public Builder builder()
    {
        return new Builder();
    }

    private String name;    // account name
    private String description;
    private Double actualBalance;   // actual account balance
    List<BillDTO> bills;
    List<BillPositionDTO> billPositions;

    AccountDTO(final String name, final String description, final Double actualBalance, final List<BillDTO> bills, final List<BillPositionDTO> billPositions)
    {
        this.name = name;
        this.description = description;
        this.actualBalance = actualBalance;
        this.bills = bills;
        this.billPositions = billPositions;
    }


//    public AccountDTO()
//    {
//    }
//
//    public AccountDTO(String name, Double actualBalance)
//    {
//        this.name = name;
//        this.actualBalance = actualBalance;
//    }
//
//    public AccountDTO(String name, String description, Double actualBalance)
//    {
//        this.name = name;
//        this.description = description;
//        this.actualBalance = actualBalance;
//    }
//
//    public AccountDTO(Account account)
//    {
//        this.name = account.getName();
//        this.description = account.getDescription();
//        this.actualBalance = account.getActualBalance();
//        this.bills = account.getBills()
//                .stream()
//                .map(Bill::toDto)
//                .collect(Collectors.toList());
//    }
//
//    public AccountDTO(String name, String description, Double actualBalance, Set<Bill> bills)
//    {
//        this.name = name;
//        this.description = description;
//        this.actualBalance = actualBalance;
//        this.bills = bills
//                .stream()
//                .map(Bill::toDto)
//                .collect(Collectors.toList());
//    }
//
//    public AccountDTO(String name, String description, Double actualBalance, List<BillPositionDTO> transactions)
//    {
//        this.name = name;
//        this.description = description;
//        this.actualBalance = actualBalance;
//        this.billPositions = transactions;
//    }

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

    public List<BillPositionDTO> getBillPositions()
    {
        return this.billPositions;
    }

    public static class Builder
    {
        private String name;    // account name
        private String description;
        private Double actualBalance;   // actual account balance
        List<BillDTO> bills;
        List<BillPositionDTO> billPositions;

        public Builder withName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description)
        {
            this.description = description;
            return this;
        }

        public Builder withActualBalance(double actualBalance)
        {
            this.actualBalance = actualBalance;
            return this;
        }

        public Builder withBills(List<BillDTO> bills)
        {
            this.bills = bills;
            return this;
        }

        public Builder withBillPositions(List<BillPositionDTO> billPositions)
        {
            this.billPositions = billPositions;
            return this;
        }

        public AccountDTO build()
        {
           return new AccountDTO(name, description, actualBalance, bills, billPositions);
        }

    }
}
