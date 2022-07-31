package com.moneyAppV5.category.repository;

import com.moneyAppV5.category.MainCategory;

import java.util.List;
import java.util.Optional;

public interface MainCategoryRepository
{
    List<MainCategory> findAll();
    List<MainCategory> findByType(String type);

    Optional<MainCategory> findById(Integer id);
    Optional<MainCategory> findByMainCategory(String mainCategory);
    Optional<MainCategory> findByHash(Integer hash);

    boolean existsById(int id);
    boolean existsByMainCategory(String mainCategory);

    MainCategory save(MainCategory entity);




//    List<MainCategory> findAllWithType();
//    List<MainCategory> findAllIncomes();
//    List<MainCategory> findAllWithType(String type);
//    List<MainCategory> findAllExpenses();


}
