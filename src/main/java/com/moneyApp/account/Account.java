package com.moneyApp.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.bill.Bill;
import com.moneyApp.user.User;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "accounts")
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;    // nazwa konta
    private String description;
    private Double actualBalance;   // bieżące saldo konta
    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private Set<Bill> bills;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Account()
    {
    }

    public Account(String name, String description, Double actualBalance, User user)
    {
        this.name = name;
        this.description = description;
        this.actualBalance = actualBalance;
        this.user = user;
    }

    public AccountDTO toDto()
    {
        return new AccountDTO(this.name, this.description, this.actualBalance, this.bills);
    }

    public AccountDTO toSimpleDto()
    {
        return new AccountDTO(this.name, this.actualBalance);
    }

    public Long getId()
    {
        return this.id;
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

    public Set<Bill> getBills()
    {
        return this.bills;
    }

    public User getUser()
    {
        return this.user;
    }


}
