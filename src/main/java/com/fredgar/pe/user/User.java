package com.fredgar.pe.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// La clase User representa un usuario en una aplicación con autenticación y autorización.
// Implementa la interfaz UserDetails de Spring Security.
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;
  private String firstName;
  private String lastName;
  // Correo electrónico del usuario, que también actúa como nombre de usuario
  private String email;
  private String password;
  // Rol del usuario, que determina sus permisos en la aplicación
  @Enumerated(EnumType.STRING)
  private Role role;

  // Devuelve una colección de autoridades (permisos) otorgadas al usuario
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  // Devuelve la contraseña del usuario
  @Override
  public String getPassword() {
    return password;
  }

  // Devuelve el nombre de usuario, que en este caso es el correo electrónico
  @Override
  public String getUsername() {
    return email;
  }

  // Devuelve si la cuenta no ha expirado
  // En este caso, siempre devuelve true (no hay manejo de expiración)
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // Devuelve si la cuenta no está bloqueada
  // En este caso, siempre devuelve true (no hay manejo de bloqueo)
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  // Devuelve si las credenciales no han expirado
  // En este caso, siempre devuelve true (no hay manejo de expiración de credenciales)
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // Devuelve si el usuario está habilitado
  // En este caso, siempre devuelve true (no hay manejo de deshabilitación)
  @Override
  public boolean isEnabled() {
    return true;
  }
}