package com.moneyApp.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface SqlMainCategoryQueryRepository extends MainCategoryQueryRepository, JpaRepository<MainCategorySnapshot, Long>
{
    @Override
    @Query(value = "SELECT m.id " +
                   "FROM MainCategorySnapshot m " +
                   "WHERE m.name = :name AND m.user.id = :userId")
    Optional<Long> findIdByNameAndUserId(String name, long userId);
}
