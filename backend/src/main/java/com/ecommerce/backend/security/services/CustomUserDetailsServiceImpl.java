package com.ecommerce.backend.security.services;

import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements Spring Security's UserDetailsService interface using our custom User model.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Locates the user based on the username.
     * @param username The username identifying the user whose data is required.
     * @return A fully populated user record (CustomUserDetails).
     * @throws ResourceNotFoundException if the user is not found.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found with username: " + username));

        return CustomUserDetails.build(user);
    }
}
