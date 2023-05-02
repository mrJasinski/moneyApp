package com.moneyApp.category.repository;

import com.moneyApp.category.Category;
import com.moneyApp.category.CategoryType;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository
{
    Category save(Category entity);

    List<Category> findByUserId(Long userId);

    List<Category> findByTypeAndUserId(CategoryType type, long userId);

    Optional<Category> findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName,
                                                                                 String subCategoryName, Long userId);

    Boolean existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(Long mainCategoryId, Long subCategoryId, CategoryType type,
                                                                   Long userId);
}
