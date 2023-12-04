package com.moneyApp.security.config;

import com.moneyApp.user.UserRole;
import com.moneyApp.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MoneyAppAuthenticationManager implements AuthenticationManager
{
    private final UserService service;
    private final PasswordEncoder encoder;

    public MoneyAppAuthenticationManager(UserService service, PasswordEncoder encoder)
    {
        this.service = service;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        var username = authentication.getName();
        var pwd = authentication.getCredentials().toString();
//        TODO test
        System.out.println("username auth mgr");
        System.out.println(username);


        var user = this.service.getUserByEmailAsDto(username);

        if (!this.encoder.matches(pwd, user.getPassword()))
            throw new BadCredentialsException("Invalid password!");

        return new UsernamePasswordAuthenticationToken(username, pwd, getGrantedAuthorities(user.getRole()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(UserRole role)
    {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));

        return grantedAuthorities;
    }
}
