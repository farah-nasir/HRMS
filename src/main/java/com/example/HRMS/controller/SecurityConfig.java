package com.example.HRMS.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() //disable csrf for simplicity
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/dashboard", "/css/**", "/js/**").permitAll() // allow login page
                .anyRequest().authenticated()
            )
            .formLogin().disable();

        return http.build();
    }
}