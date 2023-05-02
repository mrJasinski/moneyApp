package com.moneyApp.budget;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import com.moneyApp.category.Category;
import com.moneyApp.transaction.Transaction;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "budget_positions")
public class BudgetPosition
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;
    private Double plannedAmount;
    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "position")
    private Set<Transaction> transactions;

    public BudgetPosition()
    {
    }

    public BudgetPosition(Category category, Budget budget)
    {
        this.category = category;
        this.budget = budget;
        this.plannedAmount = 0d;
        this.transactions = new HashSet<>();
    }

    public BudgetPositionDTO toDto()
    {
        return new BudgetPositionDTO(this.category, this.plannedAmount, this.description);
    }

    public Long getId()
    {
        return this.id;
    }

    public Category getCategory()
    {
        return this.category;
    }

    public Budget getBudget()
    {
        return this.budget;
    }

    public Double getPlannedAmount()
    {
        return this.plannedAmount;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Set<Transaction> getTransactions()
    {
        return this.transactions;
    }
}
