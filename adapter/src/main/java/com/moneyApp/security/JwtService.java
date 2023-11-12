package com.moneyApp.security;

import com.moneyApp.user.User;
import com.moneyApp.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService
{
    private final String SECRET = "294A404E635266556A576E5A7234753778214125442A472D4B6150645367566B";

    private final UserService userService;

    public JwtService(final UserService userService)
    {
        this.userService = userService;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

//    TODO tymczasowe
//    public String generateToken(String userName)
    public JwtResponse generateToken(String userName)
    {
        var claims = new HashMap<String, Object>();
//        TODO tymczasowe
//        return createToken(claims, userName);
        return new JwtResponse(createToken(claims, userName));
    }

    private String createToken(Map<String, Object> claims, String subject)
    {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateToken(String token, User userDetails)
    {
        final var username = extractUsername(token);
        return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    public String getToken(HttpServletRequest request)
    {
        var authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer "))
            return authHeader.substring(7);
        else
            throw new IllegalArgumentException("Invalid token!");
    }

    public String getUserEmail(HttpServletRequest request)
    {
        return extractUsername(getToken(request));
    }

    public Long getUserIdFromToken(HttpServletRequest request)
    {
        return this.userService.getUserIdByEmail(extractUsername(getToken(request)));
    }
}
