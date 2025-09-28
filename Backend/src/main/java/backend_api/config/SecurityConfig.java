package backend_api.config;

import backend_api.services.UserService;
import backend_api.utils.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserService userService) throws Exception {
        http
                // Disabled because we're using JWTs and not sessions
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz

                        // Allow unauthenticated access to endpoints listed below
                        .requestMatchers(
                                "/auth/**",
                                "/api/users/register",
                                "/api/users/login"
                        ).permitAll()

                        .anyRequest().authenticated() // All other endpoints require authentication
                )
                // No sessions will be created or used by Spring Security
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Add JWT filter from JwtAuthFilter class
                .addFilterBefore(new JwtAuthFilter(userService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
