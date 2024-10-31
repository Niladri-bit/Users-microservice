package com.assignment.user.service.UserService.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.assignment.user.service.UserService.utils.JwtUtil;

import java.io.IOException;

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

        String username = null;
        String jwt = null;
        
//        String path = request.getRequestURI();
//        if (path.equals("/users/register") || path.equals("/users/login")) {
//            chain.doFilter(request, response); // Continue the filter chain
//            return; // Exit the filter method
//        }

        // Check if the Authorization header is present and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extract the token
            try {
                username = jwtUtil.extractUsername(jwt); // Extract the username from the token
            } catch (Exception e) {
                logger.warn("Unable to extract username from token: " + e.getMessage());
            }
        } else {
            logger.warn("Authorization header is missing or malformed");
        }

        // If the username is present and no authentication is set in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (userDetails == null) {
                logger.warn("UserDetails not found for username: " + username);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                return;
            }

            // Validate the token and set the authentication in the context if valid
            if (jwtUtil.validateToken(jwt, username)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.info("Authenticated user: " + username + " with roles: " + userDetails.getAuthorities());
                logger.info("Authenticated user: " + username);
            } else {
                logger.warn("Invalid JWT token for user: " + username);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }

        chain.doFilter(request, response); // Continue the filter chain
    }

}
