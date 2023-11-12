package com.moneyApp.category;

import com.moneyApp.category.Category;
import com.moneyApp.category.CategoryType;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository
{
    Category save(Category entity);
}
