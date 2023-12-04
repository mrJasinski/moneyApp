package com.moneyApp.user;

import java.util.List;
import java.util.Optional;

interface UserQueryRepository
{
    Optional<UserSnapshot> findByEmail(String email);
    Optional<UserSnapshot> findById(Long userId);

    boolean existsById(Long id);
    Boolean existsByEmail(String email);

    Optional<Long> findIdByEmail(String email);

    Optional<String> findNameById(Long userId);

    List<UserSnapshot> findAll();
}
