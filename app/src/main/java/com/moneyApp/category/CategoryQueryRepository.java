package com.moneyApp.category;

import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.category.dto.CategoryWithIdAndNameAndTypeDTO;
import com.moneyApp.category.dto.CategoryWithIdAndNameDTO;

import java.util.List;
import java.util.Optional;

interface CategoryQueryRepository
{
    List<CategorySnapshot> findByUserId(Long userId);

    List<CategorySnapshot> findByTypeAndUserId(CategoryType type, long userId);

    Optional<CategorySnapshot> findById(long categoryId);
    Optional<CategorySnapshot> findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName
                                                                                , String subCategoryName
                                                                                , Long userId);

    boolean existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(Long mainCategoryId
                                                                    , Long subCategoryId
                                                                    , CategoryType type
                                                                    , Long userId);

    Optional<Long> findIdByMainCategoryIdAndSubCategoryIdAndUserId(long mainId, long subId, long userId);

    CategoryType getTypeById(long categoryId);


    List<CategoryWithIdAndNameDTO> findCategoriesIdsAndNamesByNamesAndUserId(List<String> catNames, Long userId);

    List<CategoryWithIdAndNameAndTypeDTO> findCategoriesIdsAndNamesAndTypesByIds(List<Long> catIds);

    List<Long> findIdsByUserId(Long userId);

    List<CategoryWithIdAndNameDTO> findCategoriesIdsAndNamesByUserId(Long userId);
}
