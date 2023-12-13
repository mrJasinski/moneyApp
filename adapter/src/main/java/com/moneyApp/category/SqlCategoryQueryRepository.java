package com.moneyApp.category;

import com.moneyApp.category.dto.CategoryWithIdAndNameAndTypeDTO;
import com.moneyApp.category.dto.CategoryWithIdAndNameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface SqlCategoryQueryRepository extends CategoryQueryRepository, JpaRepository<CategorySnapshot, Long>
{
    @Override
    @Query(value = "FROM CategorySnapshot c WHERE c.mainCategory.name = :mainCategoryName AND c.subCategory.name = :subCategoryName " +
            "AND c.user.id = :userId")
    Optional<CategorySnapshot> findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName,
                                                                                 String subCategoryName, Long userId);

    @Override
    @Query(value = "SELECT c.id FROM CategorySnapshot c WHERE c.mainCategory.id = :mainId AND c.subCategory.id = :subId " +
            "AND c.user.id = :userId")
    Optional<Long> findIdByMainCategoryIdAndSubCategoryIdAndUserId(long mainId, long subId, long userId);

    @Override
    @Query(value = "SELECT c.type FROM CategorySnapshot c WHERE c.id = :categoryId")
    CategoryType getTypeById(long categoryId);

    @Override
    @Query(value = "SELECT c.id AS id, c.name AS name FROM CategorySnapshot c WHERE c.name IN :catNames AND c.user.id = :userId")
    List<CategoryWithIdAndNameDTO> findCategoriesIdsAndNamesByNamesAndUserId(List<String> catNames, Long userId);
    @Override
    @Query(value = "SELECT c.id AS id, c.name AS name FROM CategorySnapshot c WHERE c.user.id = :userId")
    List<CategoryWithIdAndNameDTO> findCategoriesIdsAndNamesByUserId(Long userId);

    @Override
    @Query(value = "SELECT c.id AS id, c.name AS name , c.type AS type FROM CategorySnapshot c WHERE c.id IN :catIds")
    List<CategoryWithIdAndNameAndTypeDTO> findCategoriesIdsAndNamesAndTypesByIds(List<Long> catIds);

    @Override
    @Query(value = "SELECT c.id FROM CategorySnapshot c WHERE c.user.id = :userId")
    List<Long> findIdsByUserId(Long userId);
}
