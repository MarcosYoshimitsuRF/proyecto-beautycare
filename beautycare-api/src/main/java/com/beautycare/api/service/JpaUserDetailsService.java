package com.beautycare.api.service;

import com.beautycare.api.model.Rol;
import com.beautycare.api.model.Usuario;
import com.beautycare.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Carga un usuario por su nombre de usuario para Spring Security.
     *
     * @param username El nombre de usuario (ej. "admin")
     * @return Un objeto UserDetails que Spring Security utilizará para la autenticación.
     * @throws UsernameNotFoundException Si el usuario no se encuentra en la BD.
     */
    @Override
    @Transactional(readOnly = true) // Transacción de solo lectura para optimizar
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Buscar al usuario en nuestro repositorio
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado: " + username));

        // 2. Convertir nuestros Roles (de BD) a GrantedAuthority (de Spring Security)
        // Spring Security requiere que los roles tengan el prefijo "ROLE_"
        Collection<? extends GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toSet());

        // 3. Devolver el objeto User (de Spring Security)
        return new User(
                usuario.getUsername(),
                usuario.getPasswordBcrypt(), // Pasamos el hash BCrypt de la BD
                usuario.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities // La lista de roles/autoridades
        );
    }
}