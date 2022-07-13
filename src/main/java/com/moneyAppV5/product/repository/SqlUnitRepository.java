package com.moneyAppV5.product.repository;

import com.moneyAppV5.product.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlUnitRepository extends UnitRepository, JpaRepository<Unit, Integer>
{
}
