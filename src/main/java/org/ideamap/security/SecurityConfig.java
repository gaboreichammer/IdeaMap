package org.ideamap.security;

import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Inject the secret key from properties
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // 2. PasswordEncoder Bean (already present)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the JwtDecoder to validate tokens using the application's secret key.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        // Decode the Base64 Secret Key used by your JwtService
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        // Create a SecretKey object from the decoded bytes.
        SecretKey secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA256");

        // Build the NimbusJwtDecoder using the secret key and the algorithm
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    // 3. SecurityFilterChain Bean: CRITICAL for configuring security and CORS
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for APIs
                .csrf(AbstractHttpConfigurer::disable)

                // --- FIX APPLIED HERE ---
                // Explicitly disable form login and HTTP basic auth to prevent early 401 rejection
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // ------------------------

                // Configure CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CRITICAL: Enable OAuth2 Resource Server support to process JWTs
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()))
                )

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to the login endpoint (and the OPTIONS preflight)
                        .requestMatchers("/api/users/login").permitAll()
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // 4. CORS Configuration Source Bean: Defines the CORS policy
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
