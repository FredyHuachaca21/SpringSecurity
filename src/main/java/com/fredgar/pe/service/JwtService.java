package com.fredgar.pe.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// JwtService es un servicio que maneja la creación y validación de tokens JWT (JSON Web Tokens)
@Service
public class JwtService {

  // Llave secreta utilizada para firmar y verificar tokens JWT
  private static final String SECRET_KEY = "6250645367566B5970337336763979244226452948404D6351665468576D5A71";

  // Extrae el nombre de usuario (en este caso, el correo electrónico) del token JWT
  public String extractUsername(String token){
    return extractClaim(token, Claims::getSubject);
  }

  // Extrae una reclamación(Petición-Request) específica del token JWT utilizando una función de
  // resolución de reclamaciones
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  // Genera un token JWT a partir de los detalles del usuario
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  // Genera un token JWT con reclamaciones(Petición-Request) adicionales y detalles del usuario
  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
  ){
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername()) // Guarda el nombre de usuario como el sujeto del token
        .setIssuedAt(new Date(System.currentTimeMillis())) // Establece la fecha de emisión del token
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // Establece la fecha de expiración del token
        .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Firma el token con la llave secreta y el algoritmo HS256
        .compact();
  }

  // Verifica si el token JWT es válido para los detalles del usuario proporcionado
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  // Verifica si el token JWT está expirado
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  // Extrae la fecha de expiración del token JWT
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  // Extrae todas las reclamaciones(Petición-Request) del token JWT
  private Claims extractAllClaims(String token){
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // Obtiene la llave secreta para firmar y verificar tokens JWT
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
