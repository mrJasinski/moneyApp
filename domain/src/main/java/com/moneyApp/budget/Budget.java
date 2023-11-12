package com.moneyApp.budget;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import com.moneyApp.category.Category;
import com.moneyApp.user.User;
import com.moneyApp.vo.SimpleBillPosition;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "budgets")
public class Budget
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate monthYear;    //  day always set to 1 because is ignored
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany
    private Set<Budget.Position> positions;

//    persistence constructor
    public Budget()
    {
    }

    public Budget(LocalDate monthYear, String description)
    {
        this.monthYear = monthYear;
        this.description = description;
    }

    public Budget(LocalDate monthYear, String description, User user)
    {
        this(monthYear, description);
        this.user = user;
    }

    public BudgetDTO toDto()
    {
        return new BudgetDTO(this.monthYear, this.description);
    }

    public Long getId()
    {
        return this.id;
    }

    public LocalDate getMonthYear()
    {
        return this.monthYear;
    }

    public String getDescription()
    {
        return this.description;
    }

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Set<Budget.Position> getPositions()
    {
        return this.positions;
    }

    @Entity
    @Table(name = "budget_positions")
    static class Position
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
        private Set<SimpleBillPosition> billPositions;

        //    persistence constructor
        protected Position()
        {
        }

        Position(Category category, Budget budget)
        {
            this.category = category;
            this.budget = budget;
            this.plannedAmount = 0d;
            this.billPositions = new HashSet<SimpleBillPosition>();
        }

        public BudgetPositionDTO toDto()
        {
            return new BudgetPositionDTO(this.category.toDto(), this.plannedAmount, this.description);
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

        public Set<SimpleBillPosition> getTransactions()
        {
            return this.billPositions;
        }
    }
}
