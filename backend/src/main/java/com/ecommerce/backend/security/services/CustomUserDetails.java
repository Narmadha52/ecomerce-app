package com.ecommerce.backend.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Custom implementation of Spring Security's UserDetails.
 * This class wraps the application's User model (com.ecommerce.backend.user.model.User)
 * to provide necessary user information to Spring Security for authentication and authorization.
 */
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String email;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String username, String email, String password,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Factory method to build a CustomUserDetails instance from a User entity.
     * This is where the User entity's roles are converted into Spring Security GrantedAuthorities.
     * @param user The application's User model entity.
     * @return A CustomUserDetails instance.
     */
    public static CustomUserDetails build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                // Convert each Role's name (e.g., ROLE_USER) into a SimpleGrantedAuthority
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    // --- Standard UserDetails Interface Implementations ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // Since this is typically for a backend API, we assume accounts are always valid
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

    // Important for comparisons within Spring Security context
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
