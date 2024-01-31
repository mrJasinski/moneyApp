package com.moneyApp.user;

import com.moneyApp.budget.BudgetService;
import com.moneyApp.mail.service.MailService;
import com.moneyApp.payment.PaymentService;
import com.moneyApp.user.dto.DashboardDTO;
import com.moneyApp.user.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class UserService
{
    private final UserRepository userRepo;
    private final UserQueryRepository userQueryRepo;
    private final PasswordEncoder encoder;
    private final MailService mailService;
    private final BudgetService budgetService;
    private final PaymentService paymentService;

    UserService(
            final UserRepository userRepo
            , final UserQueryRepository userQueryRepo
            , final PasswordEncoder encoder
            , final MailService mailService
            , final BudgetService budgetService
            , final PaymentService paymentService)
    {
        this.userRepo = userRepo;
        this.userQueryRepo = userQueryRepo;
        this.encoder = encoder;
        this.mailService = mailService;
        this.budgetService = budgetService;
        this.paymentService = paymentService;
    }



    User getUserByEmail(String email)
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

    DashboardDTO getUserDashboardByUserIdAsDto(long userId)
    {
        var userName = getUserNameById(userId);

        var budget = this.budgetService.getBudgetByDateAndUserIdAsDto(LocalDate.now(), userId);

        var payments = this.paymentService.getUnpaidPaymentsTillDateByUserIdAsDto(LocalDate.now().plusWeeks(2), userId);

        return new DashboardDTO(userName, budget, payments);
    }

    public UserDTO createUser(UserDTO toSave)
    {
        if (!validateEmail(toSave.getEmail()))
            throw new IllegalArgumentException("Provided email address is invalid!");

        if (this.userQueryRepo.existsByEmail(toSave.getEmail()))
            throw new IllegalArgumentException("User with given email already exists!");

        if (!validatePassword(toSave.getPassword()))
            throw new IllegalArgumentException("Provided password is invalid!");

        var hashedPassword = hashPassword(toSave.getPassword());

        var user = this.userRepo.save(new User(toSave.getId(), toSave.getEmail(), hashedPassword, toSave.getRole(), toSave.getName()));

        var dto = toDto(user);

        this.mailService.sendAfterRegistrationMail(dto);

        return dto;
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

    UserSnapshot getUserById(long userId)
    {
        return this.userQueryRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with given id not found!"));
    }

    String getUserNameById(Long userId)
    {
        return this.userQueryRepo.findNameById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User name for given user id not found!"));
    }

    void deleteUserById(long userId)
    {
        if (!this.userQueryRepo.existsById(userId))
            throw new IllegalArgumentException("User with given id not found!");

        this.userRepo.deleteById(userId);
    }
}
