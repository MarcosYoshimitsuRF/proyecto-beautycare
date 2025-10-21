package com.beautycare.api.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String jwtSecretString;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    // Esta será nuestra llave segura, cargada una vez
    private SecretKey jwtSecretKey;

    /**
     * Se ejecuta después de inyectar las propiedades.
     * Convierte la clave secreta (String Base64) en un objeto SecretKey seguro.
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecretString);
        this.jwtSecretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        String roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512) // Usamos la llave pre-cargada
                .compact();
    }

    /**
     * Extrae el nombre de usuario (Subject) del token JWT.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Valida si un token es correcto (firma y no expirado).
     * Usamos Jwts.parser() que tu IDE sí reconoce.
     */
    public boolean validateToken(String token) {
        try {
            // Usamos el método parser() (obsoleto) pero funcional
            Jwts.parser()
                    .setSigningKey(jwtSecretKey) // Le pasamos la llave segura
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // Token malformado, expirado, firma incorrecta, etc.
        }
        return false;
    }

    // --- Métodos de Ayuda (privados) ---

    /**
     * Método genérico para extraer un 'claim' (dato) específico del token.
     */
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parsea el token y obtiene todos los 'claims' (datos).
     */
    private Claims getAllClaimsFromToken(String token) {
        // Usamos el método parser() (obsoleto) pero funcional
        return Jwts.parser()
                .setSigningKey(jwtSecretKey) // Le pasamos la llave segura
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}