package com.moneyApp.category;

import java.util.Optional;

interface MainCategoryQueryRepository
{
    Optional<MainCategory> findByNameAndUserId(String name, long userId);
    Optional<MainCategory> findByIdAndUserId(long id, long userId);
}
