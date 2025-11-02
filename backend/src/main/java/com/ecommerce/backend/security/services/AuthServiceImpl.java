package com.ecommerce.backend.security.services;

// Corrected DTO imports
import com.ecommerce.backend.security.payload.request.LoginRequest;
import com.ecommerce.backend.security.payload.request.SignupRequest;
import com.ecommerce.backend.security.payload.response.JwtResponse;

// Corrected User/Role model imports
import com.ecommerce.backend.user.model.ERole;
import com.ecommerce.backend.user.model.Role;

import com.ecommerce.backend.exception.UserAlreadyExistsException;
import com.ecommerce.backend.security.jwt.JwtUtils;
import com.ecommerce.backend.user.repository.RoleRepository;
import com.ecommerce.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the AuthService interface for handling user authentication and registration logic.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    /**
     * Authenticates a user and generates a JWT token.
     * @param loginRequest DTO containing username and password.
     * @return JwtResponse containing the token and user details.
     */
    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        // 1. Authenticate user credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2. Generate JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 3. Extract user details and roles
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // 4. Return response DTO
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    /**
     * Registers a new user with default 'ROLE_USER'.
     * @param signUpRequest DTO containing registration details.
     * @return A success message.
     * @throws UserAlreadyExistsException if username or email is already taken.
     */
    @Override
    public String registerUser(SignupRequest signUpRequest) {
        // Check for existing username
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserAlreadyExistsException("Error: Username is already taken!");
        }

        // Check for existing email
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            // Default role: ROLE_USER
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found (ROLE_USER)."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found (ROLE_ADMIN)."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found (ROLE_MODERATOR)."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found (ROLE_USER)."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return "User registered successfully!";
    }
}
