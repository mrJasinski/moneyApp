package com.moneyApp.category;

import com.moneyApp.vo.UserSource;

class CategorySnapshot
{
    private Long id;
    private MainCategorySnapshot mainCategory;
    private SubCategorySnapshot subCategory;
    private CategoryType type;
    private String description;
    private UserSource user;

    //    persistence constructor
    public CategorySnapshot()
    {
    }

    CategorySnapshot(
            final Long id
            , final MainCategorySnapshot mainCategory
            , final SubCategorySnapshot subCategory
            , final CategoryType type
            , final String description
            , final UserSource user)
    {
        this.id = id;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.type = type;
        this.description = description;
        this.user = user;
    }

    public Long getId()
    {
        return this.id;
    }

    public MainCategorySnapshot getMainCategory()
    {
        return this.mainCategory;
    }

    public SubCategorySnapshot getSubCategory()
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

    public UserSource getUser()
    {
        return this.user;
    }
}
