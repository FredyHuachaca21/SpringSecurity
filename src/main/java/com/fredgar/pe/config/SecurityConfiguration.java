package com.fredgar.pe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Configuración de seguridad se compone de un filtro de autenticación JWT, un proveedor de autenticación personalizado y
// reglas de autorización de solicitudes HTTP.
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authenticationProvider;

  // Configura la cadena de filtros de seguridad de Spring Security
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf()
        .disable()
        .authorizeHttpRequests()
        // Permite el acceso a todos los endpoints bajo "/api/v1/auth/**" sin autenticación
        .requestMatchers("/api/v1/auth/**")
        .permitAll()
        // Requiere autenticación para cualquier otra solicitud
        .anyRequest()
        .authenticated()
        .and()
        // Establece la política de creación de sesión como sin estado
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // Agrega el proveedor de autenticación personalizado
        .authenticationProvider(authenticationProvider)
        // Agrega el filtro JwtAuthenticationFilter antes de UsernamePasswordAuthenticationFilter
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    // Construye y devuelve la cadena de filtros de seguridad
    return httpSecurity.build();
  }

}
