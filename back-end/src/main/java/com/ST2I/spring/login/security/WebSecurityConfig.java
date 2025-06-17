package com.ST2I.spring.login.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ST2I.spring.login.security.jwt.AuthEntryPointJwt;
import com.ST2I.spring.login.security.jwt.AuthTokenFilter;
import com.ST2I.spring.login.security.services.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  // Bean for JWT authentication filter
  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  // Bean for authentication provider
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  // Bean for authentication manager
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  // Bean for password encoder
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Bean for security filter chain
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf(csrf -> csrf.disable()) // Disable CSRF and enable CORS
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // Handle unauthorized requests
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
            .authorizeHttpRequests(auth -> auth
                    // Public endpoints (no authentication required)
                    .requestMatchers("/api/auth/**").permitAll() // Allow authentication endpoints
                    .requestMatchers("/api/test/**").permitAll() // Allow test endpoints
                    .requestMatchers("/swagger-ui/**").permitAll() // Allow Swagger UI
                    .requestMatchers("/v3/api-docs/**").permitAll() // Allow Swagger API docs
                    .requestMatchers("/api/notifications/**").permitAll() // Allow notifications endpoints
                    .requestMatchers("/api/equipes").authenticated() // Allow GET /api/equipes
                    .requestMatchers("/api/equipes/**").authenticated()
                    .requestMatchers("/api/chef-equipe/**").authenticated()// Authenticated endpoints (require authentication)
                    .requestMatchers("/api/projets").authenticated() // Allow GET /api/projets
                    .requestMatchers("/api/projets/**").authenticated() // Require authentication for /api/projets/*
                    .requestMatchers("/api/users").authenticated() // Require authentication for /api/users
                    .requestMatchers("/api/users/**").authenticated() // Require authentication for /api/users/*
                    .requestMatchers("/api/chef-projet/**").authenticated() // Require authentication for /api/chef-projet/*
                    .requestMatchers("/api/demandes/**").authenticated() // Require authentication for /api/demandes/*
                    .requestMatchers("/api/solde-conge/**").authenticated() // Require authentication for /api/solde-conge/*

                    // All other requests require authentication
                    .anyRequest().authenticated()
            );

    // Add the JWT authentication filter
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // Bean for CORS configuration
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("http://localhost:4200"); // Allow requests from this origin
    configuration.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
    configuration.addAllowedHeader("*"); // Allow all headers
    configuration.setAllowCredentials(true); // Allow credentials (cookies, authorization headers)

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
    return source;
  }
}