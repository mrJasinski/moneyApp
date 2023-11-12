package com.moneyApp.user;

import java.util.Optional;

public interface UserRepository
{
    User save(User entity);

    void deleteById(Long userId);
}
