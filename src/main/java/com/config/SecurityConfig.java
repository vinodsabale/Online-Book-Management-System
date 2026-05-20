package com.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                		"/login","/login/**","/home",
                		"/css/**","/js/**","/images/**",
                		"/webjars/**","/error",
                		"/verify-email",
                		"/register"
                		).permitAll()
                .requestMatchers("/member/**").hasRole("MEMBER")

                // LIBRARIAN BLOCKED
                .requestMatchers("/librarian/books/new").hasRole("ADMIN")

                // LIBRARIAN ALLOWED
                .requestMatchers("/librarian/**").hasAnyRole("LIBRARIAN", "ADMIN")

                .requestMatchers(
                    "/books/**", "/users/**",
                    "/issues/**", "/dashboard"
                ).hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}