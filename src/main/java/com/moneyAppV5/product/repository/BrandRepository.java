package com.moneyAppV5.product.repository;

import com.moneyAppV5.product.Brand;

import java.util.List;

public interface BrandRepository
{
    List<Brand> findAll();

    Brand save(Brand entity);
}
