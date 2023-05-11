package com.moneyApp.security.config;

import com.moneyApp.security.filter.CsrfCookieFilter;
import com.moneyApp.security.filter.JwtAuthFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig
{

    private final JwtAuthFilter authFilter;

    public SecurityConfig(JwtAuthFilter authFilter)
    {
        this.authFilter = authFilter;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception
    {
        var requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().configurationSource(new CorsConfigurationSource()
                {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(List.of("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                })
                .and()
                .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers( "/register",
                                "/budgets", "/categories/**", "/bills/**", "/accounts/**", "/payees", "/user/**", "/authenticate",
                                 "/payments/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
//                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(this.authFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
//                .requestMatchers("/budgets/**", "/user/**").hasRole("USER")
                .requestMatchers("/budgets/**", "/user/**", "/categories/**", "/bills/**", "/accounts/**", "/payees/**", "/payments/**",
                        "/myDashboard").authenticated()
//                TODO sendMail tylko dla testu - usunąć po!
                .requestMatchers("/register", "/authenticate", "/sendMail").permitAll()
                .and().formLogin()
                .and().httpBasic();


        return http.build();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder()
//    {
//        return new BCryptPasswordEncoder();
//    }

}
