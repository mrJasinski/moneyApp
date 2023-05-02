package com.moneyApp.category.repository;

import com.moneyApp.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SqlCategoryRepository extends CategoryRepository, JpaRepository<Category, Long>
{
    @Override
    @Query(value = "FROM Category c WHERE c.mainCategory.name = :mainCategoryName AND c.subCategory.name = :subCategoryName " +
            "AND c.user.id = :userId")
    Optional<Category> findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName,
                                                                                 String subCategoryName, Long userId);
}
