package com.example.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

                httpSecurity
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/posts").permitAll()
                                                .requestMatchers("/error").permitAll()
                                                .requestMatchers("/auth/**").permitAll()
                                                .requestMatchers("/posts/**").hasAnyRole("ADMIN")
                                                .anyRequest()
                                                .authenticated())
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(sessionConfig -> sessionConfig
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                return httpSecurity.build();
        }

        // @Bean
        // UserDetailsService myInMemoryUserDetailsService() {
        // UserDetails userDetails =
        // User.withUsername("anuj").password(passwordEncoder().encode("sonali123"))
        // .roles("USER").build();
        // UserDetails adminUser =
        // User.withUsername("admin").password(passwordEncoder().encode("admin123")).roles("ADMIN")
        // .build();
        // UserDetails manager =
        // User.withUsername("manager").password(passwordEncoder().encode("manager123"))
        // .roles("MANAGER").build();

        // return new InMemoryUserDetailsManager(userDetails, adminUser, manager);
        // }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}
