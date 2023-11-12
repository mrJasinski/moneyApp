package com.moneyApp.account;

import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.user.User;
import jakarta.persistence.*;

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
//    @JsonIgnore
//    @OneToMany(mappedBy = "account")
//    private Set<BillSourceId> bills;
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
//                .withBills(this.bills.stream().map(Bill::toDto).collect(Collectors.toList()))
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

//    public Set<Bill> getBills()
//    {
//        return this.bills;
//    }

    public User getUser()
    {
        return this.user;
    }

    void setName(final String name)
    {
        this.name = name;
    }

    void setDescription(final String description)
    {
        this.description = description;
    }

    void setActualBalance(final Double actualBalance)
    {
        this.actualBalance = actualBalance;
    }
}
