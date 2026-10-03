package com.rahul.learning.ems.backend.configs;

import com.rahul.learning.ems.backend.security.JwtAccessDeniedHandler;
import com.rahul.learning.ems.backend.security.JwtAuthenticationEntryPoint;
import com.rahul.learning.ems.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Log4j2
@Configuration
public class SpringSecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity

                /*
                 * CORS is handled by the API Gateway.
                 */

                /*
                 * JWT-based stateless REST API.
                 * CSRF protection is not required because authentication
                 * is performed using Bearer tokens rather than cookies.
                 */
                .csrf(csrf -> csrf.disable())

                /*
                 * No HTTP session is used.
                 * Every protected request must provide a valid JWT.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)
                )

                /*
                 * Authorization rules.
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * Employee APIs
                         */

                        .requestMatchers(HttpMethod.GET, "/api/employees/**")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/employees")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/employees/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**")
                        .hasRole("ADMIN")

                        /*
                         * Department APIs
                         */

                        .requestMatchers("/api/departments/**")
                        .hasRole("ADMIN")

                        /*
                         * Office APIs
                         */

                        .requestMatchers("/api/offices/**")
                        .hasRole("ADMIN")

                        /*
                         * Actuator
                         */

                        .requestMatchers("/actuator/**")
                        .permitAll()

                        /*
                         * Swagger / OpenAPI
                         */

                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()

                        /*
                         * CORS preflight requests.
                         */

                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        /*
                         * Spring Boot error endpoint.
                         */

                        .requestMatchers("/error")
                        .permitAll()

                        /*
                         * Everything else requires authentication.
                         */

                        .anyRequest()
                        .authenticated()
                )

                /*
                 * Custom JWT security exception handlers.
                 *
                 * 401 → unauthenticated
                 * 403 → authenticated but insufficient permissions
                 */
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                /*
                 * JWT filter extracts and validates the Bearer token
                 * before Spring Security performs authorization.
                 */
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("____ SecurityFilterChain Loaded ____");

        return httpSecurity.build();
    }
}