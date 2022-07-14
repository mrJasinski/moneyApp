package com.moneyAppV5.account;

import com.moneyAppV5.account.dto.AccountDTO;
import com.moneyAppV5.bill.Bill;
import com.moneyAppV5.bill.dto.BillDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "accounts")
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private LocalDate deadline;
    private double target;
    @OneToMany(mappedBy = "account")
    private Set<Bill> bills;
    private double initBalance;
    @OneToMany(mappedBy = "account")
    private Set<MonthBalance> monthBalances;
    private double actualBalance;
    private Integer hash;

//    TODO historia konta wprowadzana z ręki? tj stany kont z każdego pierwszego miesiąca wylcizane na podstawie transakcji oraz z ręki?

    public Account()
    {
    }

    public Account(String name, double actualBalance)
    {
        this.name = name;
        this.actualBalance = actualBalance;
        this.initBalance = actualBalance;
        this.bills = new HashSet<>();
    }

    public Account(String name, String description, double actualBalance) {
        this.name = name;
        this.description = description;
        this.actualBalance = actualBalance;
        this.initBalance = actualBalance;
        this.bills = new HashSet<>();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.name, this.initBalance);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LocalDate getDeadline()
    {
        return deadline;
    }

    public void setDeadline(LocalDate deadline)
    {
        this.deadline = deadline;
    }

    public double getTarget()
    {
        return target;
    }

    public void setTarget(double target)
    {
        this.target = target;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public void setBills(Set<Bill> bills) {
        this.bills = bills;
    }

    public double getInitBalance()
    {
        return initBalance;
    }

    public void setInitBalance(double initBalance)
    {
        this.initBalance = initBalance;
    }

    public Set<MonthBalance> getMonthBalances()
    {
        return monthBalances;
    }

    public void setMonthBalances(Set<MonthBalance> monthBalances)
    {
        this.monthBalances = monthBalances;
    }

    public double getActualBalance()
    {
        return actualBalance;
    }

    public void setActualBalance(double actualBalance)
    {
        this.actualBalance = actualBalance;
    }

    public Integer getHash() {
        return hash;
    }

    public void setHash(Integer hash) {
        this.hash = hash;
    }

    public void updateFrom(final Account toUpdate)
    {
        this.name = toUpdate.getName();
        this.description = toUpdate.getDescription();
        this.deadline = toUpdate.getDeadline();
        this.target = toUpdate.getTarget();
        this.bills = toUpdate.getBills();
        this.initBalance = toUpdate.getInitBalance();
        this.monthBalances = toUpdate.getMonthBalances();
        this.actualBalance = toUpdate.getActualBalance();
    }

    public AccountDTO toDto() {

        return new AccountDTO.AccountDtoBuilder()
                .buildName(this.name)
                .buildDescription(this.description)
                .buildBills(this.bills.stream().map(BillDTO::new).collect(Collectors.toList()))
                .buildActualBalance(this.actualBalance)
                .buildHash(this.hash)
                .build();
    }
}
