package com.moneyApp.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface SqlCategoryQueryRepository extends CategoryQueryRepository, JpaRepository<CategorySnapshot, Long>
{
    @Override
    @Query(value = "FROM CategorySnapshot c WHERE c.mainCategory.name = :mainCategoryName AND c.subCategory.name = :subCategoryName " +
            "AND c.user.id = :userId")
    Optional<Category> findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName,
                                                                                 String subCategoryName, Long userId);
}
