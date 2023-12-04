package com.moneyApp.user;

import com.moneyApp.mail.service.MailService;
import com.moneyApp.user.dto.UserDTO;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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

    public boolean isAuthenticated()
    {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || AnonymousAuthenticationToken.class.isAssignableFrom(auth.getClass()))
            return false;

        return auth.isAuthenticated();
    }

    public User getUserByEmail(String email)
    {
        var snap = this.userQueryRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with given email not found!"));

        return User.restore(snap);
    }

    public UserDTO getUserByEmailAsDto(String email)
    {
        return toDto(getUserByEmail(email));
    }

    UserDTO toDto(User user)
    {
        var snap = user.getSnapshot();

        return new UserDTO(
                snap.getEmail()
                , snap.getPassword()
                , snap.getName()
                , snap.getRole()
        );
    }

    public UserDTO createUser(UserDTO toSave)
    {
        if (!validateEmail(toSave.getEmail()))
            throw new IllegalArgumentException("Provided email address is invalid!");

        if (this.userQueryRepo.existsByEmail(toSave.getEmail()))
            throw new IllegalArgumentException("User with given email already exists!");

        if (!validatePassword(toSave.getPassword()))
            throw new IllegalArgumentException("Provided password is invalid!");

        toSave.setPassword(hashPassword(toSave.getPassword()));

        var user = this.userRepo.save(new User(toSave.getId(), toSave.getEmail(), toSave.getPassword(), toSave.getRole(), toSave.getName()));

        this.mailService.sendAfterRegistrationMail(toSave);

        return toSave;
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

    public UserSnapshot getUserById(long userId)
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
