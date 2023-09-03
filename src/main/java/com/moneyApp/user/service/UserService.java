package com.moneyApp.user.service;

import com.moneyApp.user.User;
import com.moneyApp.user.dto.DashboardDTO;
import com.moneyApp.user.dto.UserDTO;
import com.moneyApp.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService
{
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    UserService(UserRepository userRepo, PasswordEncoder encoder)
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
        if (!validateEmail(toSave.getEmail()))
            throw new IllegalArgumentException("Invalid email address provided!");

        if (this.userRepo.existsByEmail(toSave.getEmail()))
            throw new IllegalArgumentException("User with given email already exists!");

        if (!validatePassword(toSave.getPassword()))
            throw new IllegalArgumentException("Provided password is invalid!");

        toSave.setPassword(hashPassword(toSave.getPassword()));

        return this.userRepo.save(toSave.toUser());
    }

    boolean validateEmail(String email)
    {
        var regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        return email.matches(regex);
    }

    boolean validatePassword(String password)
    {
//        password requirements
//        min 8 chars
//        max 24 chars
//        min 1 lowercase letter
//        min 1 capital letter
//        min 1 digit
//        min 1 special char (#?!@$%^&*-)
//        no whitespaces

        var regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[#?!@$%^&*-])(?=\\S+$).{8,24}$";

        return password.matches(regex);
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
        if (!this.userRepo.existsById(userId))
            throw new IllegalArgumentException("User with given id not found!");

        this.userRepo.deleteById(userId);
    }
}
