package com.assignment.user.service.UserService.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.assignment.user.service.UserService.utils.JwtUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String internalHeader = request.getHeader("X-INTERNAL");

        String jwt = null;
        final String[] usernameHolder = new String[1]; // Use an array to hold the username

        // Check if the X-INTERNAL header is present
        if (internalHeader != null) {
            usernameHolder[0] = internalHeader; // Use the value of X-INTERNAL as the username
        }

        // If the Authorization header is present and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extract the token
            try {
                if (usernameHolder[0] == null) {
                    usernameHolder[0] = jwtUtil.extractUsername(jwt); // Extract the username from the token
                }
            } catch (Exception e) {
                logger.warn("Unable to extract username from token: " + e.getMessage());
            }
        }

        // If the username is present and no authentication is set in the context
        if (usernameHolder[0] != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;

            // If the username comes from X-INTERNAL, create a user with SERVICE_COMMUNICATOR role
            if (internalHeader != null) {
                userDetails = new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_SERVICE_COMMUNICATOR"));
                    }

                    @Override
                    public String getPassword() {
                        return null; // No password needed for internal users
                    }

                    @Override
                    public String getUsername() {
                        return usernameHolder[0]; // Username comes from the X-INTERNAL header
                    }

                    @Override
                    public boolean isAccountNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return true;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                };
            } else {
                userDetails = userDetailsService.loadUserByUsername(usernameHolder[0]);
            }

            if (userDetails == null) {
                logger.warn("UserDetails not found for username: " + usernameHolder[0]);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                return;
            }

            // Only validate the token if it is not from internal service
            if (jwt != null && internalHeader == null && !jwtUtil.validateToken(jwt, usernameHolder[0])) {
                logger.warn("Invalid JWT token for user: " + usernameHolder[0]);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }

            // Set the authentication in the context
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            logger.info("Authenticated user: " + usernameHolder[0] + " with roles: " + userDetails.getAuthorities());
        }

        // Proceed with the filter chain
        chain.doFilter(request, response);
    }
}
