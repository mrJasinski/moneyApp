package com.moneyApp.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlMainCategoryRepository extends MainCategoryRepository, JpaRepository<MainCategorySnapshot, Long>
{
}
