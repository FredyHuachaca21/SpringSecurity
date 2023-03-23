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

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    repository.save(user);
    var jetToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jetToken)
        .build();
  }
  public AuthenticationResponse authentication(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jetToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jetToken)
        .build();
  }

}
