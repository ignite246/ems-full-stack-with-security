package com.rahul.learning.ems.authservice.services.impl;

import com.rahul.learning.ems.authservice.dtos.JwtAuthResponseDTO;
import com.rahul.learning.ems.authservice.dtos.LoginDTO;
import com.rahul.learning.ems.authservice.dtos.RegisterDTO;
import com.rahul.learning.ems.authservice.entities.Role;
import com.rahul.learning.ems.authservice.entities.User;
import com.rahul.learning.ems.authservice.repos.RoleRepository;
import com.rahul.learning.ems.authservice.repos.UserRepository;
import com.rahul.learning.ems.authservice.config.JwtTokenProvider;
import com.rahul.learning.ems.authservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String register(RegisterDTO registerDTO) {

        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new IllegalStateException("Username already exists");
        }

        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        User user = new User();

        user.setName(registerDTO.getName());
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());

        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);

        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public JwtAuthResponseDTO login(LoginDTO loginDTO) {

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.usernameOrEmail(), loginDTO.password());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            String accessToken = jwtTokenProvider.generateToken(authentication);

            String role = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst().orElse(null);

            JwtAuthResponseDTO response = new JwtAuthResponseDTO();

            response.setAccessToken(accessToken);
            response.setRole(role);

            return response;

        } catch (AuthenticationException ex) {
            throw new IllegalStateException("Invalid username or password");
        }
    }
}