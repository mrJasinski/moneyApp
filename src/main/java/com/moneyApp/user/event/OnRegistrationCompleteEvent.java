package com.moneyApp.user.event;

import com.moneyApp.user.User;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent
{
    private final User user;

    public OnRegistrationCompleteEvent(User user)
    {
        super(user);
        this.user = user;
    }

    public User getUser()
    {
        return this.user;
    }
}
