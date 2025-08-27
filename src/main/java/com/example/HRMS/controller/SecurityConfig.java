package com.example.HRMS.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() //disable csrf
            .authorizeHttpRequests(auth -> auth
               .requestMatchers("/users/**", "/departments/**").hasRole("ADMIN")
                .requestMatchers("/leave/list").hasRole("MANAGER")
                .requestMatchers("/leave/apply", "/leave/my").hasRole("EMPLOYEE")
                .requestMatchers("/dashboard", "/login", "/logout").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}