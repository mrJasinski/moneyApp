package com.moneyApp.category;

import com.moneyApp.vo.UserSource;

class Category
{
    static Category restore(CategorySnapshot snapshot)
    {
        return new Category(
                snapshot.getId()
                , MainCategory.restore(snapshot.getMainCategory())
                , SubCategory.restore(snapshot.getSubCategory())
                , snapshot.getName()
                , snapshot.getType()
                , snapshot.getDescription()
                , snapshot.getUser()
        );
    }

    private final Long id;
    private final MainCategory mainCategory;
    private final SubCategory subCategory;
    private final String name;
    private final CategoryType type;
    private final String description;
    private final UserSource user;

    private Category(
            final Long id
            , final MainCategory mainCategory
            , final SubCategory subCategory
            , final String name
            , final CategoryType type
            , final String description
            , final UserSource user)
    {
        this.id = id;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.name = name;
        this.type = type;
        this.description = description;
        this.user = user;
    }

    CategorySnapshot getSnapshot()
    {
        return new CategorySnapshot(
                this.id
                , this.mainCategory.getSnapshot()
                , this.subCategory.getSnapshot()
                , this.type
                , this.description
                , this.user
        );
    }
}
