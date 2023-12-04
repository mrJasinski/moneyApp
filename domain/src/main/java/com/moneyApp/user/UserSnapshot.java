package com.moneyApp.user;

class UserSnapshot
{
    private Long id;
    private String email;   // also used as username
    private String password;
    private UserRole role;  // access level (eg user, admin, etc)
    private String name;    // name used for communication (eg "Good morning Wiesław!")

    //    persistence constructor
    public UserSnapshot()
    {
    }

    UserSnapshot(final Long id, final String email, final String password, final UserRole role, final String name)
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
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
