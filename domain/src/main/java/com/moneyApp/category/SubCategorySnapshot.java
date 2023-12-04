package com.moneyApp.category;

import com.moneyApp.vo.UserSource;

class SubCategorySnapshot
{
    private Long id;
    private String name;
    private MainCategorySnapshot mainCategory;
    private UserSource user;

    public SubCategorySnapshot()
    {
    }

    SubCategorySnapshot(
            final Long id
            , final String name
            , final MainCategorySnapshot mainCategory
            , final UserSource user)
    {
        this.id = id;
        this.name = name;
        this.mainCategory = mainCategory;
        this.user = user;
    }

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public MainCategorySnapshot getMainCategory()
    {
        return this.mainCategory;
    }

    public UserSource getUser()
    {
        return this.user;
    }
}