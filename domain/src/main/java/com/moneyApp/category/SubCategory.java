package com.moneyApp.category;

import com.moneyApp.vo.UserSource;

class SubCategory
{
    static SubCategory restore(SubCategorySnapshot snapshot)
    {
        return new SubCategory(
                snapshot.getId()
                , snapshot.getName()
                , MainCategory.restore(snapshot.getMainCategory())
                , snapshot.getUser()
        );
    }

    private final Long id;
    private final String name;
    private final MainCategory mainCategory;
    private final UserSource user;

    private SubCategory(
            final Long id
            , final String name
            , final MainCategory mainCategory
            , final UserSource user)
    {
        this.id = id;
        this.name = name;
        this.mainCategory = mainCategory;
        this.user = user;
    }

    SubCategorySnapshot getSnapshot()
    {
        return new SubCategorySnapshot(
                this.id
                , this.name
                , this.mainCategory.getSnapshot()
                , this.user
        );
    }
}

