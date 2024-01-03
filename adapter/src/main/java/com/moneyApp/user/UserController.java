package com.moneyApp.user;

import com.moneyApp.mail.service.MailService;
import com.moneyApp.security.JwtRequest;
import com.moneyApp.security.JwtService;
import com.moneyApp.user.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UserController
{
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final MailService mailService;
    private final SimpleMailMessage message;

    public UserController(
            final UserService userService
            , final JwtService jwtService
            , final AuthenticationManager authManager
            , final MailService mailService
            , final SimpleMailMessage message)
    {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.mailService = mailService;
        this.message = message;
    }

    @PostMapping("/authenticate")
    ResponseEntity<?> generateToken(@RequestBody JwtRequest user)
    {
        var userDetails = this.authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (!userDetails.isAuthenticated())
            throw new UsernameNotFoundException("Invalid user credentials!");

        return ResponseEntity.ok(this.jwtService.generateToken(user.getEmail()));
    }

    @GetMapping("/myDashboard")
    ResponseEntity<?> getDashboard(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.userService.getUserDashboardByUserIdAsDto(this.jwtService.getUserIdFromToken(request)));
    }

    @GetMapping("/user")
    ResponseEntity<?> getUserDetailsAfterLogin(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.userService.getUserByEmailAsDto(this.jwtService.getUserEmail(request)));
    }

    @PostMapping("/register")
    ResponseEntity<?> registerUser(@RequestBody UserDTO toSave)
    {
        var user = this.userService.createUser(toSave);

//        event usunięty - czy powrócić?
//        this.eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));

        return ResponseEntity.created(URI.create("/" + user.getEmail())).body(user);
    }

//    TODO test
    @GetMapping("/sendMail")
    ResponseEntity<?> sendMailToUser()
    {
        this.mailService.sendSimpleMessage("abakan@ymail.com", "Test", this.message.getText());

        return ResponseEntity.ok("Sent!");
    }


}
