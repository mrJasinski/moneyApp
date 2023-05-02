package com.moneyApp.category.repository;

import com.moneyApp.category.SubCategory;

import java.util.Optional;

public interface SubCategoryRepository
{
    Optional<SubCategory> findByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId);

    SubCategory save(SubCategory entity);
}
