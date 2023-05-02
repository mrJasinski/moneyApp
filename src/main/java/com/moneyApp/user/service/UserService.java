package com.moneyApp.user.service;

import com.moneyApp.user.User;
import com.moneyApp.user.dto.DashboardDTO;
import com.moneyApp.user.dto.UserDTO;
import com.moneyApp.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepo, PasswordEncoder encoder)
    {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public User getUserByEmail(String email)
    {
        return this.userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user with given email!"));
    }

    public UserDTO getUserByEmailAsDto(String email)
    {
        return getUserByEmail(email).toDto();
    }

    public User createUser(UserDTO toSave)
    {
        if (!this.userRepo.existsByEmail(toSave.getEmail()))
        {
            toSave.setPassword(hashPassword(toSave.getPassword()));

            return this.userRepo.save(toSave.toUser());
        }
        else
            throw new IllegalArgumentException("User with given email already exists!");
    }

    String hashPassword(String toHash)
    {
        return this.encoder.encode(toHash);
    }

    public Long getUserIdByEmail(String email)
    {
        return this.userRepo.findIdByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user with given email found!"));
    }

    public User getUserById(long userId)
    {
        return this.userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user with given id!"));
    }

    String getUserNameById(Long userId)
    {
        return this.userRepo.findNameById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No name found for given user id!"));
    }

    public void deleteUserById(long userId)
    {
        if (this.userRepo.existsById(userId))
            this.userRepo.deleteById(userId);
        else
            throw new IllegalArgumentException("User with given id not found!");
    }
}
