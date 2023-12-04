package com.moneyApp.category;

import com.moneyApp.category.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlSubCategoryRepository extends SubCategoryRepository, JpaRepository<SubCategorySnapshot, Long>
{
}
