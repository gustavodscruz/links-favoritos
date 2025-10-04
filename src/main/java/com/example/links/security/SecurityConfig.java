package com.example.links.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.links.service.UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final UserService userService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .userDetailsService(userService)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/register", "/public/**", "/css/**", "/js/**",
                                                                "/images/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .usernameParameter("email")
                                                .defaultSuccessUrl("/dashboard")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .exceptionHandling(exceptions -> exceptions
                                                .accessDeniedPage("/access-denied"))
                                .sessionManagement(session -> session
                                                .maximumSessions(1)
                                                .expiredUrl("/login?expired"));

                return http.build();
        }

}
