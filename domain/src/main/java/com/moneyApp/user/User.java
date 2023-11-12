package com.moneyApp.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moneyApp.budget.Budget;
import com.moneyApp.user.dto.UserDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;   // also used as username
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;  // access level (eg user, admin, etc)
    private String name;    // name used for communication (eg "Good morning Wiesław!")

//    persistence constructor
    protected User()
    {
    }

    public User(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, UserRole role, String name)
    {
        this(email, password);
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
