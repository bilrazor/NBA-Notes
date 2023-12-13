package com.project.BackendNBA_Notes.security.service;

import com.project.BackendNBA_Notes.models.User;
import com.project.BackendNBA_Notes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Define la clase UserDetailsServiceImpl que implementa la interfaz UserDetailsService
// de Spring Security.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inyección de la dependencia de UserRepository.
    // Este repositorio se utiliza para buscar usuarios en la base de datos.
    @Autowired
    private UserRepository userRepository;

    // Sobreescribe el método loadUserByUsername de la interfaz UserDetailsService.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(user);
    }

}
