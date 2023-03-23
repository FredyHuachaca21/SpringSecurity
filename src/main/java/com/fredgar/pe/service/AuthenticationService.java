package com.fredgar.pe.service;

import com.fredgar.pe.auth.AuthenticationRequest;
import com.fredgar.pe.auth.AuthenticationResponse;
import com.fredgar.pe.auth.RegisterRequest;
import com.fredgar.pe.repository.UserRepository;
import com.fredgar.pe.user.Role;
import com.fredgar.pe.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// AuthenticationService es un servicio que maneja la autenticación y el registro de usuarios
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  // Registra a un nuevo usuario y devuelve un token JWT
  public AuthenticationResponse register(RegisterRequest request) {
    // Crea un objeto User a partir del objeto RegisterRequest
    var user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword())) // Codifica la contraseña del usuario
        .role(Role.USER)
        .build();

    // Guarda el nuevo usuario en la base de datos
    repository.save(user);

    // Genera un token JWT para el nuevo usuario
    var jwtToken = jwtService.generateToken(user);

    // Construye y devuelve una respuesta de autenticación con el token JWT
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  // Autentica al usuario y devuelve un token JWT
  public AuthenticationResponse authentication(AuthenticationRequest request) {
    // Realiza la autenticación utilizando el AuthenticationManager de Spring Security
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    // Busca al usuario por su correo electrónico
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();

    // Genera un token JWT para el usuario autenticado
    var jwtToken = jwtService.generateToken(user);

    // Construye y devuelve una respuesta de autenticación con el token JWT
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

}
