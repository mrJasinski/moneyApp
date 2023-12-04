package com.moneyApp.category;

import com.moneyApp.vo.UserSource;

import java.util.Set;
import java.util.stream.Collectors;

class MainCategory
{
    static MainCategory restore(MainCategorySnapshot snapshot)
    {
        return new MainCategory(
                snapshot.getId()
                , snapshot.getName()
                , snapshot.getSubCategories().stream().map(SubCategory::restore).collect(Collectors.toSet())
                , snapshot.getUser()
        );
    }

    private final Long id;
    private final String name;
    private final Set<SubCategory> subCategories;
    private final UserSource user;

    private MainCategory(
            final Long id
            , final String name
            , final Set<SubCategory> subCategories
            , final UserSource user)
    {
        this.id = id;
        this.name = name;
        this.subCategories = subCategories;
        this.user = user;
    }

    MainCategorySnapshot getSnapshot()
    {
        return new MainCategorySnapshot(
                this.id
                , this.name
                , this.subCategories.stream().map(SubCategory::getSnapshot).collect(Collectors.toSet())
                , this.user
        );
    }
}
