package com.moneyApp.user;

import java.util.Optional;

interface UserQueryRepository
{
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);

    Boolean existsById(Long id);
    Boolean existsByEmail(String email);

    Optional<Long> findIdByEmail(String email);

    Optional<String> findNameById(Long userId);
}
