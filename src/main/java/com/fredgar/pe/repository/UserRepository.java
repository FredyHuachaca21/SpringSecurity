package com.fredgar.pe.repository;

import com.fredgar.pe.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// UserRepository es una interfaz que define un repositorio para gestionar objetos User
// Extiende JpaRepository para utilizar las funcionalidades de Spring Data JPA
public interface UserRepository extends JpaRepository<User, Integer> {

  // Encuentra un usuario por su correo electrónico (email)
  // Devuelve un objeto Optional<User> que puede contener un objeto User si se encuentra uno
  // con el correo electrónico especificado, o estará vacío si no se encuentra ningún usuario
  Optional<User> findByEmail(String email);

}
