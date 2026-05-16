package com.placement.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/students/register",
                    "/api/companies/register",
                    "/",
                    "/login.html",
                    "/student-dashboard.html",
                    "/company-dashboard.html",
                    "/admin-dashboard.html",
                    "/css/**", "/js/**", "/images/**"
                ).permitAll()
                .anyRequest().permitAll()   // simplified for demo — add JWT for production
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}