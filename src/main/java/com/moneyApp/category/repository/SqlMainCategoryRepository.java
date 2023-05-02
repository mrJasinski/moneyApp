package com.moneyApp.category.repository;

import com.moneyApp.category.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlMainCategoryRepository extends MainCategoryRepository, JpaRepository<MainCategory, Long>
{
}
