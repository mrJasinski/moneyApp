package com.moneyApp.category;

import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    @Enumerated(EnumType.STRING)
    private CategoryType type;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    persistence constructor
    public Category()
    {
    }

    public Category(MainCategory mainCategory, SubCategory subCategory, CategoryType type, String description, User user)
    {
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.type = type;
        this.description = description;
        this.user = user;
    }

    public CategoryDTO toDto()
    {
        return new CategoryDTO(this.mainCategory.getName(), this.subCategory.getName(), this.type, this.description);
    }

    public Long getId()
    {
        return this.id;
    }

    public MainCategory getMainCategory()
    {
        return this.mainCategory;
    }

    public SubCategory getSubCategory()
    {
        return this.subCategory;
    }

    public String getCategoryName()
    {
        return String.format("%s : %s", this.mainCategory.getName(), this.subCategory.getName());
    }

    public CategoryType getType()
    {
        return this.type;
    }

    public String getDescription()
    {
        return this.description;
    }

    public User getUser()
    {
        return this.user;
    }
}
