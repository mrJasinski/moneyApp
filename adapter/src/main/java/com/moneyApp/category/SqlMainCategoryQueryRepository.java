package com.moneyApp.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlMainCategoryQueryRepository extends MainCategoryQueryRepository, JpaRepository<MainCategorySnapshot, Long>
{
}
