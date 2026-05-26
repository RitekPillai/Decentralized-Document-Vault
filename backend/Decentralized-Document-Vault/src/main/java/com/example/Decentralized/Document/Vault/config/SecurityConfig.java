package com.example.Decentralized.Document.Vault.config;

import com.example.Decentralized.Document.Vault.filters.JWTFilters;
import com.example.Decentralized.Document.Vault.services.auth.CoustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestBody;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final PasswordEncoder passwordEncoder;

    private final CoustomUserDetails customUserDetailsService;

    private final JWTFilters jwtFilters;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->  session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(req->
                        req.
                                requestMatchers("/api/v1/auth/**").permitAll()

                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated()

                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilters, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }
}
