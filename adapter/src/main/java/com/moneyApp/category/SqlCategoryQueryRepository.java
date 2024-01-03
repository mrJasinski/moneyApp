package com.moneyApp.category;

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
    @Query(value = "SELECT c.id AS id, c.name AS name " +
                   "FROM CategorySnapshot c " +
                   "WHERE c.name IN :catNames AND c.user.id = :userId")
    List<CategoryWithIdAndNameDTO> findCategoriesIdsAndNamesByNamesAndUserId(List<String> catNames, Long userId);

    @Override
    @Query(value = "SELECT c.type " +
                   "FROM CategorySnapshot c " +
                   "WHERE c.id = :categoryId")
    Optional<CategoryType> findTypeById(Long categoryId);

    @Override
    @Query(value = "FROM CategorySnapshot c " +
                   "WHERE c.id IN :catIds")
    List<CategorySnapshot> findCategoriesByIds(List<Long> catIds);
}
