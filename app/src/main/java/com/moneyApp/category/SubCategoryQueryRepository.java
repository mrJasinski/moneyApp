package com.moneyApp.category;

import java.util.List;
import java.util.Optional;

interface SubCategoryQueryRepository
{
    Optional<SubCategory> findByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId);

    List<SubCategory> findByMainCategoryNameAndUserId(String main, Long userId);
}
