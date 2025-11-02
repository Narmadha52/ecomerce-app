package com.ecommerce.backend.security.services;

import com.ecommerce.backend.security.jwt.JwtUtils;
import com.ecommerce.backend.security.payload.request.LoginRequest;
import com.ecommerce.backend.security.payload.request.RegisterRequest;
import com.ecommerce.backend.user.model.ERole;
import com.ecommerce.backend.user.model.Role;
import com.ecommerce.backend.user.repository.RoleRepository;
import com.ecommerce.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Service class for handling user authentication and registration logic.
 */
@Service
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder encoder,
                       JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Authenticates the user credentials and generates a JWT.
     *
     * @param loginRequest The login request containing username/email and password.
     * @return The generated JWT token string.
     */
    public String authenticateUser(LoginRequest loginRequest) {
        // Use AuthenticationManager to authenticate the user against the database
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        // Set the Authentication object in the Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate the JWT token based on the authenticated user details
        return jwtUtils.generateJwtToken(authentication);
    }

    /**
     * Registers a new user account.
     *
     * @param registerRequest The registration request containing user details.
     * @throws RuntimeException if username or email is already taken, or if a required role is missing.
     */
    public void registerUser(RegisterRequest registerRequest) {
        // 1. Check for existing username
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        // 2. Check for existing email
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // 3. Create new User's account and encode the password
        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()));

        // Set optional fields
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());

        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            // Default role: ROLE_USER
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Default role 'ROLE_USER' not found in database."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_ADMIN' not found in database."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_MODERATOR' not found in database."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_USER' not found in database."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
    }
}
