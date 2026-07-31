package com.rahul.learning.javaguide.emsbackend.configs;

import com.rahul.learning.javaguide.emsbackend.security.JwtAccessDeniedHandler;
import com.rahul.learning.javaguide.emsbackend.security.JwtAuthenticationEntryPoint;
import com.rahul.learning.javaguide.emsbackend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Configuration
public class SpringSecurityConfig {

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        httpSecurity
                .cors(Customizer.withDefaults())
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

                        // Accessible to all (login and register APIs)
                        .requestMatchers("/api/auth/**").permitAll()

                        // Preflight API
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .anyRequest().authenticated()
                );

        // Handling exception for AuthenticationFailure and AccessDenied
        httpSecurity.exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
        );
        // Our jwtAuthenticationFilter filter must be executed before Spring's UsernamePasswordAuthenticationFilter
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        log.info("____ SecurityFilterChain Loaded ____");
        return httpSecurity.build();
    }

    /*
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
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}