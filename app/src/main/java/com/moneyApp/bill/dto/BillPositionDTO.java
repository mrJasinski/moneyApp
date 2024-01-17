package com.moneyApp.bill.dto;

import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.vo.BillPositionSource;
import com.moneyApp.vo.BudgetPositionSource;

import java.time.LocalDate;

public class BillPositionDTO
{
    static public Builder builder()
    {
        return new Builder();
    }

    private final Long id;
    private final Long number;
    private final Double amount;
    private final String categoryName;
    private final CategoryDTO category;
    private final String gainerName;
    private final String description;
    private final LocalDate date;
    private final String payeeName;
    private final String accountName;
    private final BudgetPositionSource budgetPosition;

    BillPositionDTO(
            Long id
            , Long number
            , LocalDate date
            , String payeeName
            , String accountName
            , Double amount
            , CategoryDTO category
            , String categoryName
            , String gainerName
            , String description
            , BudgetPositionSource budgetPosition)
    {
        this.id = id;
        this.number = number;
        this.amount = amount;
        this.category = category;
        this.categoryName = categoryName;
        this.gainerName = gainerName;
        this.description = description;
        this.date = date;
        this.payeeName = payeeName;
        this.accountName = accountName;
        this.budgetPosition = budgetPosition;
    }

    public Long getId()
    {
        return this.id;
    }

    public Long getNumber()
    {
        return this.number;
    }

    public Double getAmount()
    {
        return this.amount;
    }

     public CategoryDTO getCategory()
    {
        return this.category;
    }

     public Long getCategoryId()
    {
        return this.category.getId();
    }

    public String getGainerName()
    {
        return this.gainerName;
    }

    public String getDescription()
    {
        return this.description;
    }

    public LocalDate getDate()
    {
        return this.date;
    }

    public String getPayeeName()
    {
        return this.payeeName;
    }

    public String getAccountName()
    {
        return this.accountName;
    }

    public BudgetPositionSource getBudgetPosition()
    {
        return this.budgetPosition;
    }

    public static class Builder
    {
        private Long id;
        private Long number;
        private Double amount;
        private CategoryDTO category;
        private String categoryName;
        private String gainerName;
        private String description;
        private LocalDate date;
        private String payeeName;
        private String accountName;
        private BudgetPositionSource budgetPosition;

        public Builder withId(Long id)
        {
            this.id = id;
            return this;
        }

        public Builder withNumber(Long number)
        {
            this.number = number;
            return this;
        }

        public Builder withAmount(Double amount)
        {
            this.amount = amount;
            return this;
        }

        public Builder withCategory(CategoryDTO category)
        {
            this.category = category;
            return this;
        }

        public Builder withCategoryName(String categoryName)
        {
            this.categoryName = categoryName;
            return this;
        }

        public Builder withGainerName(String gainerName)
        {
            this.gainerName = gainerName;
            return this;
        }

        public Builder withDescription(String description)
        {
            this.description = description;
            return this;
        }

        public Builder withDate(LocalDate date)
        {
            this.date = date;
            return this;
        }

        public Builder withPayeeName(String payeeName)
        {
            this.payeeName = payeeName;
            return this;
        }

        public Builder withAccountName(String accountName)
        {
            this.accountName = accountName;
            return this;
        }

        public Builder withBudgetPosition(BudgetPositionSource budgetPosition)
        {
            this.budgetPosition = budgetPosition;
            return this;
        }

        public BillPositionDTO build()
        {
            return new BillPositionDTO(
                    this.id
                    , this.number
                    , this.date
                    , this.payeeName
                    , this.accountName
                    , this.amount
                    , this.category
                    , this.categoryName
                    , this.gainerName
                    , this.description
                    , this.budgetPosition);
        }
    }
}
