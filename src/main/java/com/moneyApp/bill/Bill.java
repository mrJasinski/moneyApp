package com.moneyApp.bill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moneyApp.account.Account;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.budget.Budget;
import com.moneyApp.payee.Payee;
import com.moneyApp.transaction.Transaction;
import com.moneyApp.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "bills")
public class Bill
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long number;  // numer kolejny w danym miesiącu
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "payee_id")
    private Payee payee;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;
    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "bill")
    private Set<Transaction> transactions;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Bill()
    {
    }

    public Bill(Long number, LocalDate date, Payee payee, Account account, Budget budget, String description, User user)
    {
        this.number = number;
        this.date = date;
        this.payee = payee;
        this.account = account;
        this.budget = budget;
        this.description = description;
        this.user = user;
    }

    public BillDTO toDto()
    {
        return new BillDTO(this.number, this.date, this.payee.getName(), this.description, this.transactions);
    }

    public Long getId()
    {
        return this.id;
    }

    public Long getNumber()
    {
        return this.number;
    }

    public LocalDate getDate()
    {
        return this.date;
    }

    public Payee getPayee()
    {
        return this.payee;
    }

    public Account getAccount()
    {
        return this.account;
    }

    public Budget getBudget()
    {
        return this.budget;
    }

    public String getDescription()
    {
        return this.description;
    }

    public User getUser()
    {
        return this.user;
    }
}
