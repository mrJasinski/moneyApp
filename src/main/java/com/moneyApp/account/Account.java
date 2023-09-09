package com.moneyApp.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.bill.Bill;
import com.moneyApp.user.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "accounts")
class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;    // account name - no whitespaces allowed
    private String description;
    private Double actualBalance;   // actual account balance
    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private Set<Bill> bills;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    persistence constructor
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
        return AccountDTO.builder()
                .withName(this.name)
                .withDescription(this.description)
                .withActualBalance(this.actualBalance)
                .build();
    }

    public AccountDTO toDtoWithBills()
    {
        return AccountDTO.builder()
                .withName(this.name)
                .withDescription(this.description)
                .withActualBalance(this.actualBalance)
                .withBills(this.bills.stream().map(Bill::toDto).collect(Collectors.toList()))
                .build();
    }

    public AccountDTO toSimpleDto()
    {
        return AccountDTO.builder()
            .withName(this.name)
            .withActualBalance(this.actualBalance)
            .build();
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
