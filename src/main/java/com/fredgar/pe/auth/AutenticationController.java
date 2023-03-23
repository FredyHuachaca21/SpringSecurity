package com.fredgar.pe.auth;

import com.fredgar.pe.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Clase que maneja las solicitudes de registro y autenticación de usuarios.
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AutenticationController {

  private final AuthenticationService authenticationService;

  //Método que permite registrar al usuario y generar un token
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authenticationService.register(request));
  }

  //Método que permite autenticar al usuario y generar un token.
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(authenticationService.authentication(request));
  }

}
