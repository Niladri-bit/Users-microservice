package com.assignment.user.service.UserService.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
            .csrf().disable() 
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
            .and()
            .authorizeRequests()
                .requestMatchers("/users/register", "/users/login","/validateToken").permitAll()
                .requestMatchers("/users/{userId}/roles").hasAuthority("ADMIN") 
                .requestMatchers("/users").hasAnyAuthority("ADMIN","MODERATOR") 
                .requestMatchers(HttpMethod.DELETE, "/users/{userId}").hasAuthority("ADMIN")
                .requestMatchers("/users/{userId}").hasAnyAuthority("ADMIN","MODERATOR") 
                .anyRequest().authenticated() 
            .and()
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); 

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
