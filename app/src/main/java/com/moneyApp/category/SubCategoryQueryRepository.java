package com.moneyApp.category;

import java.util.List;
import java.util.Optional;

interface SubCategoryQueryRepository
{
    Optional<SubCategorySnapshot> findByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId);

    List<SubCategorySnapshot> findByMainCategoryNameAndUserId(String main, Long userId);

    List<Long> findIdByNameAndUserId(String name, long userId);

    List<SubCategorySnapshot> findByNameAndUserId(String name, long userId);
}
