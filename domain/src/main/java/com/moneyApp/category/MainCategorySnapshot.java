package com.moneyApp.category;

import com.moneyApp.vo.UserSource;

import java.util.Set;

class MainCategorySnapshot
{
    private Long id;
    private String name;
    private Set<SubCategorySnapshot> subCategories;
    private UserSource user;

    public MainCategorySnapshot()
    {
    }

    MainCategorySnapshot(
            final Long id
            , final String name
            , final Set<SubCategorySnapshot> subCategories
            , final UserSource user)
    {
        this.id = id;
        this.name = name;
        this.subCategories = subCategories;
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

    public Set<SubCategorySnapshot> getSubCategories()
    {
        return this.subCategories;
    }

    public UserSource getUser()
    {
        return this.user;
    }
}