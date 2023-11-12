package com.moneyApp.category;

import com.moneyApp.category.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlSubCategoryRepository extends SubCategoryRepository, JpaRepository<SubCategory, Long>
{
}
