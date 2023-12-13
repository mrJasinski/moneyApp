package com.moneyApp.bill.dto;

import com.moneyApp.category.CategoryType;
import com.moneyApp.vo.BillPositionSource;

import java.time.LocalDate;

public class BillPositionDTO
{
    static public Builder builder()
    {
        return new Builder();
    }

    private final Long id;
    private final Double amount;
    private final String categoryName;
    private final CategoryType type;
    private final String gainerName;
    private final String description;
    private final LocalDate date;
    private final String payeeName;
    private final String accountName;

    BillPositionDTO(
            Long id
            , LocalDate date
            , String payeeName
            , String accountName
            , Double amount
            , String categoryName
            , CategoryType type
            , String gainerName
            , String description)
    {
        this.id = id;
        this.amount = amount;
        this.categoryName = categoryName;
        this.type = type;
        this.gainerName = gainerName;
        this.description = description;
        this.date = date;
        this.payeeName = payeeName;
        this.accountName = accountName;
    }

    public BillPositionSource toSource()
    {
        return new BillPositionSource(this.id);
    }

    public Long getId()
    {
        return this.id;
    }

    public Double getAmount()
    {
        return this.amount;
    }

    public String getCategoryName()
    {
        return this.categoryName;
    }

    public CategoryType getType()
    {
        return this.type;
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

    public static class Builder
    {
        private Long id;
        private Double amount;
        private String categoryName;
        private CategoryType type;
        private String gainerName;
        private String description;
        private LocalDate date;
        private String payeeName;
        private String accountName;

        public Builder withId(Long id)
        {
            this.id = id;
            return this;
        }

        public Builder withAmount(Double amount)
        {
            this.amount = amount;
            return this;
        }

        public Builder withCategoryName(String categoryName)
        {
            this.categoryName = categoryName;
            return this;
        }

        public Builder withType(CategoryType type)
        {
            this.type = type;
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

        public BillPositionDTO build()
        {
            return new BillPositionDTO(
                    this.id
                    , this.date
                    , this.payeeName
                    , this.accountName
                    , this.amount
                    , this.categoryName
                    , this.type
                    , this.gainerName
                    , this.description);
        }
    }
}
