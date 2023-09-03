package com.moneyApp.bill;

import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.category.Category;
import com.moneyApp.payee.Payee;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public
class BillPosition
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long number;  //TODO numer kolejny w danym rachunku
    private Double amount;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "gainer_id")
    private Payee gainer;
    private String description;
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;
    @ManyToOne
    @JoinColumn(name = "position_id")
    private BudgetPosition position;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    persistence constructor
    public BillPosition()
    {
    }

    public BillPosition(long number, Double amount, Category category, Payee gainer, String description, Bill bill, User user)
    {
        this.number = number;
        this.amount = amount;
        this.category = category;
        this.gainer = gainer;
        this.description = description;
        this.bill = bill;
        this.user = user;
    }

    public BillPositionDTO toSimpleDto()
    {
        return new BillPositionDTO(this.amount, this.category.getCategoryName(), this.gainer.getName(), this.description);
    }

    public BillPositionDTO toDto()
    {
        return new BillPositionDTO(this.bill.getBillDate(), this.bill.getPayee().getName(), this.bill.getAccount().getName(), this.amount, this.category.getCategoryName(),
                this.gainer.getName(), this.description);
    }

    public Long getId()
    {
        return this.id;
    }

    public long getNumber()
    {
        return this.number;
    }

    public Double getAmount()
    {
        return this.amount;
    }

    public Category getCategory()
    {
        return this.category;
    }

    public Payee getGainer()
    {
        return this.gainer;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Bill getBill()
    {
        return this.bill;
    }

    public BudgetPosition getPosition()
    {
        return this.position;
    }

    public void setPosition(BudgetPosition position)
    {
        this.position = position;
    }

    public User getUser()
    {
        return this.user;
    }
}
