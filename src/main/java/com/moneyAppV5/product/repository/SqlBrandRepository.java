package com.moneyAppV5.product.repository;

import com.moneyAppV5.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlBrandRepository extends BrandRepository, JpaRepository<Brand, Integer>
{

}
