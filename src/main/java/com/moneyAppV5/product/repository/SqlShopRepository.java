package com.moneyAppV5.product.repository;

import com.moneyAppV5.product.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlShopRepository extends ShopRepository, JpaRepository<Shop, Integer>
{
}
