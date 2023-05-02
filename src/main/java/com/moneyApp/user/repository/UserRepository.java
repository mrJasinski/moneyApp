package com.moneyApp.user.repository;

import com.moneyApp.user.User;

import java.util.Optional;

public interface UserRepository
{
    Optional<User> findByEmail(String email);
    Optional<User> findById(long userId);

    User save(User entity);

    boolean existsById(Long id);
    Boolean existsByEmail(String email);

    Optional<Long> findIdByEmail(String email);

    Optional<String> findNameById(Long userId);

    void deleteById(Long userId);
}
