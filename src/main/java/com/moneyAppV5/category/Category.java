package com.moneyAppV5.category;

import com.moneyAppV5.budget.BudgetPosition;
import com.moneyAppV5.category.dto.CategoryDTO;
import com.moneyAppV5.transaction.Transaction;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String category;
    @ManyToOne
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    @OneToMany(mappedBy = "category")
    private Set<Transaction> transactions;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String description;
    @OneToMany(mappedBy = "category")
    private Set<BudgetPosition> budgetPositions;
    private Integer hash;

     public Category()
    {
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.mainCategory, this.subCategory, this.type);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCategory(String main, String sub)
    {
        this.category = String.format("%s : %s", main, sub);
    }

    public MainCategory getMainCategory()
    {
        return mainCategory;
    }

    public void setMainCategory(MainCategory mainCategory)
    {
        this.mainCategory = mainCategory;
    }

    public SubCategory getSubCategory()
    {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory)
    {
        this.subCategory = subCategory;
    }

    public Set<Transaction> getTransactions()
    {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions)
    {
        this.transactions = transactions;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Set<BudgetPosition> getBudgetPositions()
    {
        return budgetPositions;
    }

    public void setBudgetPositions(Set<BudgetPosition> budgetPositions)
    {
        this.budgetPositions = budgetPositions;
    }

    public Integer getHash() {
        return hash;
    }

    public void setHash(Integer hash) {
        this.hash = hash;
    }

    public String toDisplay()
    {
        return String.format("%s : %s", this.mainCategory.getMainCategory(), this.subCategory.getSubCategory());
    }

    public CategoryDTO toDto()
    {
        var dto = new CategoryDTO();

        dto.setMainCategory(this.mainCategory);
        dto.setSubCategory(this.subCategory);
        dto.setCategory(toDisplay());
        dto.setType(this.type);
        dto.setHash(this.hash);

        return dto;
    }
}
