package com.moneyAppV5.product.repository;

import com.moneyAppV5.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
{
    Product save(Product entity);

    List<Product> findProductsByShopNameAndGenreId(String shopName, Integer genreId);

    Optional<Float> findPriceByProductIdAndShopName(Integer productId, String shopName);

    List<Product> findAll();
}
