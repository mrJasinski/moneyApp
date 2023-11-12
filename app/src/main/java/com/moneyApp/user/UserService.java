package com.moneyApp.user;

import com.moneyApp.mail.service.MailService;
import com.moneyApp.user.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService
{
    private final UserRepository userRepo;
    private final UserQueryRepository userQueryRepo;
    private final PasswordEncoder encoder;
    private final MailService mailService;

    UserService(
            final UserRepository userRepo
            , final UserQueryRepository userQueryRepo
            , final PasswordEncoder encoder
            , final MailService mailService)
    {
        this.userRepo = userRepo;
        this.userQueryRepo = userQueryRepo;
        this.encoder = encoder;
        this.mailService = mailService;
    }

    public User getUserByEmail(String email)
    {
        return this.userQueryRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with given email not found!"));
    }

    public UserDTO getUserByEmailAsDto(String email)
    {
        return getUserByEmail(email).toDto();
    }

    public User createUser(UserDTO toSave)
    {
        if (!validateEmail(toSave.getEmail()))
            throw new IllegalArgumentException("Provided email address is invalid!");

        if (this.userQueryRepo.existsByEmail(toSave.getEmail()))
            throw new IllegalArgumentException("User with given email already exists!");

        if (!validatePassword(toSave.getPassword()))
            throw new IllegalArgumentException("Provided password is invalid!");

        toSave.setPassword(hashPassword(toSave.getPassword()));

        var user = this.userRepo.save(toSave.toUser());

        this.mailService.sendAfterRegistrationMail(user);

        return user;
    }

    boolean validateEmail(String email)
    {
//        validation if email has form xxx@xxx.xx

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
        return this.userQueryRepo.findIdByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User id with given email not found!"));
    }

    public User getUserById(long userId)
    {
        return this.userQueryRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with given id not found!"));
    }

    String getUserNameById(Long userId)
    {
        return this.userQueryRepo.findNameById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User name for given user id not found!"));
    }

    public void deleteUserById(long userId)
    {
        if (!this.userQueryRepo.existsById(userId))
            throw new IllegalArgumentException("User with given id not found!");

        this.userRepo.deleteById(userId);
    }
}
