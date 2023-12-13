package com.moneyApp.category;

import java.util.Optional;

interface MainCategoryQueryRepository
{
    Optional<MainCategorySnapshot> findByNameAndUserId(String name, long userId);
    Optional<MainCategorySnapshot> findById(long id);

    Optional<Long> findIdByNameAndUserId(String name, long userId);
}
