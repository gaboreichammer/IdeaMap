package org.ideamap.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity // Optional but good practice for clarity
public class SecurityConfig {

    // 1. PasswordEncoder Bean (already present)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. SecurityFilterChain Bean: CRITICAL for configuring security and CORS
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for APIs (unless you are using a secure token-based solution)
                .csrf(AbstractHttpConfigurer::disable)

                // Configure CORS
                // .cors() uses the CorsConfigurationSource bean defined below
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to the login endpoint (and the OPTIONS preflight)
                        .requestMatchers("/api/users/login").permitAll()
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // 3. CORS Configuration Source Bean: Defines the CORS policy
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // **Angular Origin**
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // **CRITICAL: Allow required methods, including OPTIONS for preflight**
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow necessary headers for requests (like Content-Type, Authorization, etc.)
        configuration.setAllowedHeaders(List.of("*"));

        // Allow cookies and authentication headers (if needed later for sessions/JWTs)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this configuration to all paths
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
