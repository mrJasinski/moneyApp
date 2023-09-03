package com.moneyApp.category.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moneyApp.category.Category;
import com.moneyApp.category.CategoryType;

public class CategoryDTO
{
    private String name;    // w formacie "main : sub"
//    @JsonIgnore
    private String mainCategory;
//    @JsonIgnore
    private String subCategory;
    private CategoryType type;
    private String description;

    public CategoryDTO(String category)
    {
        this.name = category;
    }

    public CategoryDTO(String mainCategory, String subCategory, CategoryType type)
    {
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.type = type;
    }

    public CategoryDTO(String mainCategory, String subCategory, CategoryType type, String description)
    {
        this(mainCategory, subCategory, type);
        this.name = setName(mainCategory, subCategory);
        this.description = description;
    }

    String setName(String mainCategory, String subCategory)
    {
        return String.format("%s : %s", mainCategory, subCategory);
    }

    public String getName()
    {
        return this.name;
    }

    public String getMainCategory()
    {
        return this.mainCategory;
    }

    public String getSubCategory()
    {
        return this.subCategory;
    }

    public CategoryType getType()
    {
        return this.type;
    }

    public String getDescription()
    {
        return this.description;
    }
}
