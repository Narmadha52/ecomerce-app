package com.ecommerce.backend.security.jwt;

import com.ecommerce.backend.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter responsible for processing JWTs in the request headers.
 * It ensures that the token is valid and sets the user's authentication context
 * for subsequent security checks.
 */
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * The core logic that executes once per request.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Get JWT from the request header
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // 2. Extract username from token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 3. Load user details
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 4. Create authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                // 5. Set authentication details (remote address, session id, etc.)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Set the Authentication in SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header (Bearer scheme).
     *
     * @param request The incoming HTTP request.
     * @return The JWT string, or null if not present or incorrectly formatted.
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // Remove "Bearer " prefix (7 characters)
            return headerAuth.substring(7);
        }

        return null;
    }
}
