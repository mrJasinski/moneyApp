package com.moneyApp.payee.dto;

import com.moneyApp.payee.PayeeRole;

public class PayeeDTO
{
    private String name;
    private PayeeRole role;

    public PayeeDTO()
    {
    }

    public PayeeDTO(String name, PayeeRole role)
    {
        this.name = name;
        this.role = role;
    }

    public String getName()
    {
        return this.name;
    }

    public PayeeRole getRole()
    {
        return this.role;
    }
}
