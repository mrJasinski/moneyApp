package com.moneyApp.budget;

import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "budgets")
public class Budget
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate monthYear;    //dzień ustawiany zawsze na 1 bo i tak jest ignorowany
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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
        this.monthYear = monthYear;
        this.description = description;
        this.user = user;
    }

    public BudgetDTO toDto()
    {
        return new BudgetDTO(monthYear, description);
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
}
