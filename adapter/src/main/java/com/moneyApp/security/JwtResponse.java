package com.moneyApp.security;

public class JwtResponse
{
    private String token;
//TODO tymczasowo
//    public JwtResponse()
//    {
//    }

    public JwtResponse(String token)
    {
        this.token = token;
    }

    public String getToken()
    {
        return this.token;
    }
}
