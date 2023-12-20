package com.moneyApp.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlCategoryRepository extends CategoryRepository, JpaRepository<CategorySnapshot, Long>
{

}
