package com.assignment.user.service.UserService.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.assignment.user.service.UserService.Filter.JwtRequestFilter;
import com.assignment.user.service.UserService.Services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF protection for testing (not recommended for production)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session
            .and()
            .authorizeRequests()
                .requestMatchers("/users/register", "/users/login").permitAll() // Allow register and login for all
                .requestMatchers("/users/**").hasAuthority("ADMIN") // Admin can access all user APIs
                .requestMatchers("/users/{userId}/roles").hasAuthority("ADMIN") // Only admin can assign roles
                .requestMatchers("/users").hasAnyAuthority("ADMIN", "MODERATOR") // Admin and moderator can access all users
                .requestMatchers("/users/{userId}").hasAnyAuthority("ADMIN", "MODERATOR") // Admin and moderator can access user by ID
                .anyRequest().authenticated() // All other requests require authentication
            .and()
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService(userDetailsServiceImpl)
            .passwordEncoder(passwordEncoder); // Use the passwordEncoder method directly
        return authenticationManagerBuilder.build();
    }

    
}
