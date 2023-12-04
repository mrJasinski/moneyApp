package com.moneyApp.category;

import com.moneyApp.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface SqlCategoryRepository extends CategoryRepository, JpaRepository<CategorySnapshot, Long>
{

}
