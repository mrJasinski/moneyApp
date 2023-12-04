package com.moneyApp.payee.dto;

import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.payee.PayeeRole;

import java.util.HashSet;
import java.util.Set;

public class PayeeDTO
{
    private String name;
    private PayeeRole role;
    private Set<BillDTO> bills = new HashSet<>();
    private Set<BillPositionDTO> billPositions = new HashSet<>();


    public PayeeDTO()
    {
    }

    public PayeeDTO(String name, PayeeRole role)
    {
        this.name = name;
        this.role = role;
    }

    PayeeDTO(final String name, final PayeeRole role, final Set<BillDTO> bills, final Set<BillPositionDTO> billPositions)
    {
        this.name = name;
        this.role = role;
        this.bills = bills;
        this.billPositions = billPositions;
    }

    public String getName()
    {
        return this.name;
    }

    public PayeeRole getRole()
    {
        return this.role;
    }

    public Set<BillDTO> getBills()
    {
        return this.bills;
    }

    public Set<BillPositionDTO> getBillPositions()
    {
        return this.billPositions;
    }
}
