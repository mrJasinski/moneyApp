package com.moneyApp.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moneyApp.user.User;
import com.moneyApp.user.UserRole;

public class UserDTO
{
    private String email;   // również nazwa użytkownika
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String name;    // imię wykorzystywane do komunikatów wewnątrz apki
    private UserRole role;

    public UserDTO()
    {
    }

    public UserDTO(String email, String name)
    {
        this.email = email;
        this.name = name;
    }

    public UserDTO(String email, String password, String name)
    {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserDTO(String email, String password, String name, UserRole role)
    {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public User toUser()
    {
        return new User(this.email, this.password, this.role, this.name);
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getName()
    {
        return this.name;
    }

    public UserRole getRole()
    {
        return this.role;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
