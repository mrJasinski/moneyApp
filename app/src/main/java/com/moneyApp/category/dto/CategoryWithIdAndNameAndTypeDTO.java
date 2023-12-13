package com.moneyApp.category.dto;

import com.moneyApp.category.CategoryType;

public interface CategoryWithIdAndNameAndTypeDTO
{
     Long getId();

    String getName();

    CategoryType getType();
}
