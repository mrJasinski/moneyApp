package com.moneyApp.category;

import com.moneyApp.category.dto.CategoryWithIdAndNameDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface CategoryQueryRepository
{
    List<CategorySnapshot> findByUserId(Long userId);
    List<CategorySnapshot> findCategoriesByIds(List<Long> catIds);

    List<CategoryWithIdAndNameDTO> findCategoriesIdsAndNamesByNamesAndUserId(List<String> catNames, Long userId);

    Optional<CategorySnapshot> findById(long categoryId);

    Optional<CategoryType> findTypeById(Long categoryId);

    boolean existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(Long mainCategoryId
                                                                    , Long subCategoryId
                                                                    , CategoryType type
                                                                    , Long userId);

    Optional<CategorySnapshot> findByNameAndUserId(String nameFromUrlName, Long userId);

    boolean existsByNameAndUserId(String catName, long userId);

    Set<Long> findCategoriesIdsByType(CategoryType categoryType);
}
