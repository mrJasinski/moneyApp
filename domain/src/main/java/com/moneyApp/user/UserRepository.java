package com.moneyApp.user;

public interface UserRepository
{
    UserSnapshot save(UserSnapshot entity);

    void deleteById(Long userId);
}
