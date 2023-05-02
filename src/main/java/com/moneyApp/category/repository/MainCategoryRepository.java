package com.moneyApp.category.repository;

import com.moneyApp.category.MainCategory;

import java.util.Optional;

public interface MainCategoryRepository
{
    Optional<MainCategory> findByNameAndUserId(String name, long userId);
    Optional<MainCategory> findByIdAndUserId(long id, long userId);

    MainCategory save(MainCategory entity);
}
