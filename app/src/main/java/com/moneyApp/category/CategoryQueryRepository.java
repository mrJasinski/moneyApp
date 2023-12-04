package com.moneyApp.category;

import java.util.List;
import java.util.Optional;

interface CategoryQueryRepository
{
    List<Category> findByUserId(Long userId);

    List<Category> findByTypeAndUserId(CategoryType type, long userId);

    Optional<Category> findById(long categoryId);
    Optional<Category> findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName
                                                                                , String subCategoryName
                                                                                , Long userId);

    boolean existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(Long mainCategoryId
                                                                    , Long subCategoryId
                                                                    , CategoryType type
                                                                    , Long userId);

    Optional<Long> findIdByMainCategoryIdAndSubCategoryIdAndUserId(long mainId, long subId, long userId);

    CategoryType getTypeById(long categoryId);


}
