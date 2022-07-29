package com.moneyAppV5.product.repository;

import com.moneyAppV5.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SqlProductRepository extends ProductRepository, JpaRepository<Product, Integer>
{


    @Override
    @Query(value = "select * from products inner join prices on prices.id inner join shops on shops.id where SHOPS.NAME = :shopName " +
            "and GENRE_ID = :genreId", nativeQuery = true)
    List<Product> findProductsByShopNameAndGenreId(String shopName, Integer genreId);

    //    TODO zapytanie

    @Override
    @Query(value = "", nativeQuery = true)
    Optional<Float> findPriceByProductIdAndShopName(Integer productId, String shopName);
}
