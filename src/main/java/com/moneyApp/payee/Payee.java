package com.moneyApp.payee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moneyApp.bill.Bill;
import com.moneyApp.payee.dto.PayeeDTO;
import com.moneyApp.transaction.Transaction;
import com.moneyApp.user.User;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "payees")
public class Payee
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private PayeeRole role;
    @JsonIgnore
    @OneToMany(mappedBy = "payee")
    private Set<Bill> bills;
    @JsonIgnore
    @OneToMany(mappedBy = "gainer")
    private Set<Transaction> transactions;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Payee()
    {
    }

    public Payee(String name, PayeeRole role, User user)
    {
        this.name = name;
        this.role = role;
        this.user = user;
    }

    public PayeeDTO toDto()
    {
        return new PayeeDTO(this.name, this.role);
    }

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public PayeeRole getRole()
    {
        return this.role;
    }

    public Set<Bill> getBills()
    {
        return this.bills;
    }

    public Set<Transaction> getTransactions()
    {
        return this.transactions;
    }

    public User getUser()
    {
        return this.user;
    }
}
