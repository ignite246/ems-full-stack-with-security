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
import org.springframework.security.config.http.SessionCreationPolicy;
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

                /*
                 * Enables Cross-Origin Resource Sharing (CORS) using the
                 * CorsConfigurationSource bean defined below.
                 * This allows the React application (http://localhost:3000)
                 * to communicate with the Spring Boot backend.
                 */
                .cors(Customizer.withDefaults())

                /*
                 * Disables Cross-Site Request Forgery (CSRF) protection.
                 * Since this application is a stateless REST API that uses
                 * JWT (Bearer Token) authentication instead of session-based
                 * authentication, CSRF protection is not required.
                 */
                .csrf(AbstractHttpConfigurer::disable)

                /*
                 * Configures Spring Security to operate in a stateless manner.
                 * No HTTP session will be created or used to store the user's
                 * authentication information. Every request must carry a valid
                 * JWT access token for authentication.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                /*
                 * Defines authorization rules for incoming HTTP requests.
                 * Access is granted based on the authenticated user's role.
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * Employee APIs
                         */

                        // Both USER and ADMIN can view employee information.
                        .requestMatchers(HttpMethod.GET, "/api/employees/**")
                        .hasAnyRole("USER", "ADMIN")

                        // Only ADMIN can create a new employee.
                        .requestMatchers(HttpMethod.POST, "/api/employees")
                        .hasRole("ADMIN")

                        // Only ADMIN can update employee information.
                        .requestMatchers(HttpMethod.PUT, "/api/employees/**")
                        .hasRole("ADMIN")

                        // Only ADMIN can delete an employee.
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**")
                        .hasRole("ADMIN")

                        /*
                         * Department APIs
                         */

                        // All department operations are restricted to ADMIN users.
                        .requestMatchers("/api/departments/**")
                        .hasRole("ADMIN")

                        /*
                         * Authentication APIs
                         */

                        // Login and registration endpoints are publicly accessible.
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        /*
                         * CORS Preflight Requests
                         */

                        // Allows browsers to perform CORS preflight (OPTIONS)
                        // requests before sending the actual API request.
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        /*
                         * Any request not matched above must be authenticated.
                         */
                        .anyRequest()
                        .authenticated()
                )

                /*
                 * Configures custom handlers for security-related exceptions.
                 *
                 * JwtAuthenticationEntryPoint:
                 * Invoked when an unauthenticated user tries to access
                 * a protected resource (returns HTTP 401 Unauthorized).
                 *
                 * JwtAccessDeniedHandler:
                 * Invoked when an authenticated user does not have sufficient
                 * permissions to access a resource (returns HTTP 403 Forbidden).
                 */
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                );

        /*
         * Registers the custom JWT authentication filter before Spring Security's
         * UsernamePasswordAuthenticationFilter.
         *
         * The JWT filter extracts the Bearer token from the Authorization header,
         * validates it, and sets the authenticated user in the SecurityContext
         * before Spring Security performs authorization.
         */
        httpSecurity.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

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