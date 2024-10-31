package com.assignment.user.service.UserService.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF protection for testing (not recommended for production)
            .authorizeRequests()
                .anyRequest().permitAll() // Permit all requests without authentication
            .and()
            .formLogin().disable() // Disable form login
            .logout().disable(); // Disable logout functionality

        return http.build();
    }
}
