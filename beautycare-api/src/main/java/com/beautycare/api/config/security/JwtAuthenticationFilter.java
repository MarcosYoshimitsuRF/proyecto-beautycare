package com.beautycare.api.config.security;

import com.beautycare.api.service.JpaUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JpaUserDetailsService userDetailsService;

    /**
     * El método principal del filtro. Se ejecuta una vez por cada petición.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. Obtener el token de la cabecera
            String jwt = getJwtFromRequest(request);

            // 2. Validar el token
            if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {

                // 3. Extraer el username
                String username = jwtUtil.getUsernameFromToken(jwt);

                // 4. Cargar los detalles del usuario desde la BD
                // (Es importante recargar, no confiar solo en el token,
                // por si el usuario fue deshabilitado o sus roles cambiaron)
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Crear el objeto de autenticación
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // No se necesitan credenciales (password) aquí
                        userDetails.getAuthorities() // Los roles
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Establecer la autenticación en el contexto de seguridad
                // ¡Este es el paso que "autentica" al usuario para esta petición!
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // En caso de error (token inválido, etc.), simplemente no se autentica
            // y la petición continuará. Si la ruta está protegida,
            // SecurityConfig la bloqueará (Error 401/403).
            // (Aquí iría un log de error en un proyecto real)
        }

        // 7. Continuar con el resto de la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Método de ayuda para extraer el token de la cabecera "Authorization".
     *
     * @param request La petición HTTP.
     * @return El token (String) o null si no se encuentra.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // El token debe tener texto y empezar con "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Devuelve solo el token, sin el "Bearer "
            return bearerToken.substring(7);
        }
        return null;
    }
}