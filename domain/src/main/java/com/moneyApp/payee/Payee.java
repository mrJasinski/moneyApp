package com.moneyApp.payee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moneyApp.payee.dto.PayeeDTO;
import com.moneyApp.user.User;
import com.moneyApp.vo.SimpleBill;
import com.moneyApp.vo.SimpleBillPosition;
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
    private Set<SimpleBill> bills;
    @JsonIgnore
    @OneToMany(mappedBy = "gainer")
    private Set<SimpleBillPosition> billPositions;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    persistence constructor
    protected Payee()
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

    public Set<SimpleBill> getBills()
    {
        return this.bills;
    }

    public Set<SimpleBillPosition> getTransactions()
    {
        return this.billPositions;
    }

    public User getUser()
    {
        return this.user;
    }
}
