package com.rahul.learning.javaguide.emsbackend.configs;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        httpSecurity
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // Anyone logged in can read
                        .requestMatchers(HttpMethod.GET, "/api/employees/**")
                        .hasAnyRole("USER", "ADMIN")

                        // Only ADMIN can create
                        .requestMatchers(HttpMethod.POST, "/api/employees")
                        .hasRole("ADMIN")

                        // Only ADMIN can update
                        .requestMatchers(HttpMethod.PUT, "/api/employees/**")
                        .hasRole("ADMIN")

                        // Only ADMIN can delete
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                );
        log.info("____ SecurityFilterChain Loaded ____");
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        final UserDetails userDetails1 = User.withUsername("user")
                .password(passwordEncoder().encode("p@ssword"))
                .roles("USER")
                .build();

        final UserDetails userDetails2 = User.withUsername("admin")
                .password(passwordEncoder().encode("p@ssword"))
                .roles("USER", "ADMIN")
                .build();

        UserDetailsService userDetailsService = new InMemoryUserDetailsManager(userDetails1, userDetails2);
        log.info("____ UserDetailsService Loaded ____");

        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}