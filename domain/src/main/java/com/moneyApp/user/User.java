package com.moneyApp.user;

class User
{
    static User restore(UserSnapshot snapshot)
    {
        return new User(snapshot.getId(), snapshot.getEmail(), snapshot.getPassword(), snapshot.getRole(), snapshot.getName());
    }

    private final Long id;
    private final String email;   // also used as username
    private final String password;
    private final UserRole role;  // access level (eg user, admin, etc)
    private final String name;    // name used for communication (eg "Good morning Wiesław!")

    User(final Long id, final String email, final String password, final UserRole role, final String name)
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    UserSnapshot getSnapshot()
    {
        return new UserSnapshot(
                this.id
                , this.email
                , this.password
                , this.role
                , this.name);
    }
}
