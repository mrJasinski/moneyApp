package com.moneyApp.category;

import java.util.List;
import java.util.Optional;

interface CategoryQueryRepository
{
    List<Category> findByUserId(Long userId);

    List<Category> findByTypeAndUserId(CategoryType type, long userId);

    Optional<Category> findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName
                                                                                , String subCategoryName
                                                                                , Long userId);

    Boolean existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(Long mainCategoryId
                                                                    , Long subCategoryId
                                                                    , CategoryType type
                                                                    , Long userId);
}
