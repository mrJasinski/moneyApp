package com.moneyApp.category.dto;

import com.moneyApp.category.CategoryType;

import java.text.Normalizer;

public class CategoryDTO
{
    private Long id;
    private String name;    // "mainName : subName"
    private String mainCategory;
    private String subCategory;
    private CategoryType type;
    private String description;

    public CategoryDTO()
    {
    }

    public CategoryDTO(final Long id, final String name)
    {
        this.id = id;
        this.name = name;
    }

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
        this.name = addName(mainCategory, subCategory);
        this.description = description;
    }

    String addName(String mainCategory, String subCategory)
    {
        return String.format("%s : %s", mainCategory, subCategory);
    }

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getUrlName()
    {
        //edited name to be used in URL (without whitespaces and letters like ż, ą etc)
        var urlName = this.name.replace(" : ", "-");

        urlName = urlName.replace(" ", "-");

        urlName = Normalizer.normalize(urlName, Normalizer.Form.NFKD);

        return urlName.replaceAll("\\p{M}", "");
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
