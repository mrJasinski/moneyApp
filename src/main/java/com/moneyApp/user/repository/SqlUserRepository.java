package com.moneyApp.user.repository;

import com.moneyApp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SqlUserRepository extends UserRepository, JpaRepository<User, Long>
{
    @Override
    @Query(value = "SELECT u.id FROM User u WHERE u.email = :email")
    Optional<Long> findIdByEmail(String email);

    @Override
    @Query(value = "SELECT u.name FROM User u WHERE u.id = :userId")
    Optional<String> findNameById(Long userId);
}
