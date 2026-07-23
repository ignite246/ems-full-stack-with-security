package com.rahul.learning.javaguide.emsbackend.services.impl;

import com.rahul.learning.javaguide.emsbackend.dtos.RegisterDTO;
import com.rahul.learning.javaguide.emsbackend.entities.Role;
import com.rahul.learning.javaguide.emsbackend.entities.User;
import com.rahul.learning.javaguide.emsbackend.exceptions.UserRegistrationException;
import com.rahul.learning.javaguide.emsbackend.repos.RoleRepository;
import com.rahul.learning.javaguide.emsbackend.repos.UserRepository;
import com.rahul.learning.javaguide.emsbackend.services.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found."));

        roles.add(roleUser);

        user.setRoles(roles);
        userRepository.save(user);

        return "User registered successfully";
    }
}
