package com.rahul.learning.javaguide.emsbackend.services.impl;

import com.rahul.learning.javaguide.emsbackend.dtos.LoginDTO;
import com.rahul.learning.javaguide.emsbackend.dtos.RegisterDTO;
import com.rahul.learning.javaguide.emsbackend.entities.Role;
import com.rahul.learning.javaguide.emsbackend.entities.User;
import com.rahul.learning.javaguide.emsbackend.exceptions.UserLoginFailedException;
import com.rahul.learning.javaguide.emsbackend.exceptions.UserRegistrationException;
import com.rahul.learning.javaguide.emsbackend.repos.RoleRepository;
import com.rahul.learning.javaguide.emsbackend.repos.UserRepository;
import com.rahul.learning.javaguide.emsbackend.services.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(RegisterDTO registerDTO) {

        // Check username is already existing
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new UserRegistrationException("Username already exists");
        }

        // Check email is already existing
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new UserRegistrationException("Email already exists");
        }

        // Creating user
        User user = new User();
        user.setName(registerDTO.getName());
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role roleUser = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new IllegalStateException("ROLE_USER not found."));

        roles.add(roleUser);

        user.setRoles(roles);
        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public String login(LoginDTO loginDTO) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDTO.usernameOrEmail(), loginDTO.password());

            Authentication authentication = authenticationManager.authenticate(token);

            /**
             * Spring Security never returns an unauthenticated Authentication object.
             * It either returns a fully authenticated object, or
             * throws an exception.
             */
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails != null) {
                log.info("User authentication successful={}", userDetails.getUsername());
            }
            return "User logged-in successfully";

        } catch (AuthenticationException ex) {
            log.error("Authentication failed", ex);
            throw new UserLoginFailedException("Invalid username or password");
        }
    }
}
