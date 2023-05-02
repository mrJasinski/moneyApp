package com.moneyApp.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moneyApp.budget.Budget;
import com.moneyApp.user.dto.UserDTO;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;   // również nazwa użytkownika
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;  // określa poziom dostępów
    private String name;    // imię wykorzystywane do komunikatów wewnątrz apki

    public User()
    {
    }

    public User(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, UserRole role, String name)
    {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
    }
// tylko na potrzeby testów
    public User(Long id, String email)
    {
        this.id = id;
        this.email = email;
    }

    public UserDTO toDto()
    {
        return new UserDTO(this.email, this.name);
    }

    public Long getId()
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

    public UserRole getRole()
    {
        return this.role;
    }

    public String getName()
    {
        return this.name;
    }
}
