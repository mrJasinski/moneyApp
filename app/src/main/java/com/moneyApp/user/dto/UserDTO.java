package com.moneyApp.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moneyApp.user.UserRole;

public class UserDTO
{
    private long id;
    private String email;   // also username
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String name;    // name used for communication (eg "Good morning Wiesław!")
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
        this(email, name);
        this.password = password;
    }

    public UserDTO(String email, String password, String name, UserRole role)
    {
        this(email, password, name);
        this.role = role;
    }

    public long getId()
    {
        return this.id;
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
}
