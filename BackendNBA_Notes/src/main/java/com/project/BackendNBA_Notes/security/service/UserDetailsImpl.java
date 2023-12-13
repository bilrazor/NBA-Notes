package com.project.BackendNBA_Notes.security.service;

import com.project.BackendNBA_Notes.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    // Propiedades básicas del usuario
    private Long id;
    private String username;
    private String email;
    private String password;

    // Constructor de la clase, inicializa los atributos del usuario.
    public UserDetailsImpl(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Método que devuelve las autoridades (roles y permisos) del usuario.

    // Método para obtener el ID del usuario.
    public Long getId() {
        return id;
    }

    // Método para obtener el email del usuario.
    public String getEmail() {
        return email;
    }

    // Método para obtener la contraseña del usuario.
    @Override
    public String getPassword() {
        return password;
    }

    // Método para obtener el nombre de usuario.
    @Override
    public String getUsername() {
        return username;
    }

    // Métodos que indican el estado de la cuenta del usuario.
    // Actualmente, todos devuelven true, lo que indica que la cuenta no está expirada, bloqueada,
    // y que las credenciales no están expiradas. También indica que el usuario está habilitado.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // O puedes devolver null si prefieres
    }


    // Método estático para construir un UserDetailsImpl a partir de un objeto User.
    // Convierte los roles del usuario a una lista de GrantedAuthority.
    public static UserDetailsImpl build(User user) {


        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
    }
}