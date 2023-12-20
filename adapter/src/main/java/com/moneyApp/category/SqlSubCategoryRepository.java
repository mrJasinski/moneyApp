package com.moneyApp.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlSubCategoryRepository extends SubCategoryRepository, JpaRepository<SubCategorySnapshot, Long>
{
}
