package com.ecommerce.backend.security;

import com.ecommerce.backend.security.jwt.AuthEntryPointJwt;
import com.ecommerce.backend.security.jwt.AuthTokenFilter;
import com.ecommerce.backend.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main configuration class for Spring Security.
 * Sets up JWT-based authentication, stateless session management,
 * and defines access rules for various API endpoints.
 */
@Configuration
@EnableMethodSecurity // Enables @PreAuthorize, @PostAuthorize annotations
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * Defines the JWT authentication token filter.
     * This filter is responsible for processing the JWT in the request header.
     * @return An instance of the AuthTokenFilter.
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Configures the authentication provider, setting the user details service
     * and the password encoder to be used for authentication logic.
     * @return The configured DaoAuthenticationProvider.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Exposes the AuthenticationManager bean, which is used in AuthService
     * to perform the actual login authentication.
     * @param authConfig Configuration object provided by Spring.
     * @return The AuthenticationManager instance.
     * @throws Exception if configuration fails.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Defines the password encoder using BCrypt algorithm.
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the security filter chain to configure HTTP security settings.
     *
     * @param http The HttpSecurity object to configure.
     * @return The built SecurityFilterChain.
     * @throws Exception if configuration fails.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF as we are stateless
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // Set unauthorized handler
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set session policy to stateless (JWT requirement)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints for authentication (Login/Registration)
                        .requestMatchers("/api/auth/**").permitAll()
                        // Public endpoints for test/open access (e.g., viewing public products)
                        .requestMatchers("/api/test/**").permitAll()
                        // Secure all other API requests - requires authentication
                        .requestMatchers("/api/**").authenticated()
                        // Secure all other requests
                        .anyRequest().authenticated()
                );

        // Add the authentication provider for the authentication logic
        http.authenticationProvider(authenticationProvider());

        // Add the custom JWT filter before Spring's standard authentication filter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
