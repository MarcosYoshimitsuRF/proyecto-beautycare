package com.beautycare.api.controller;

import com.beautycare.api.config.security.JwtUtil;
import com.beautycare.api.controller.dto.LoginRequest;
import com.beautycare.api.controller.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Endpoint para autenticar un usuario y devolver un token JWT.
     * Es público, según SecurityConfig.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 1. Autenticar al usuario con Spring Security
        // Esto usará nuestro JpaUserDetailsService y BCryptPasswordEncoder
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. Si la autenticación fue exitosa, establecerla en el contexto
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generar el token JWT usando nuestro JwtUtil
        String jwt = jwtUtil.generateToken(authentication);

        // 4. Devolver el token en la respuesta
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}