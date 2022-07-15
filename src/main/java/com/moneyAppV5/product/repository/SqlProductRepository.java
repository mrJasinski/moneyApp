package com.moneyAppV5.product.repository;

import com.moneyAppV5.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlProductRepository extends ProductRepository, JpaRepository<Product, Integer>
{
}
