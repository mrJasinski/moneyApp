package com.moneyApp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface SqlUserQueryRepository extends UserQueryRepository, JpaRepository<UserSnapshot, Long>
{
    @Override
    @Query(value = "SELECT u.id FROM UserSnapshot u WHERE u.email = :email")
    Optional<Long> findIdByEmail(String email);

    @Override
    @Query(value = "SELECT u.name FROM UserSnapshot u WHERE u.id = :userId")
    Optional<String> findNameById(Long userId);
}