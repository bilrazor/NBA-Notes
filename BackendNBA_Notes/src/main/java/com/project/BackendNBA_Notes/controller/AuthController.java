package com.project.BackendNBA_Notes.controller;

import com.project.BackendNBA_Notes.models.User;
import com.project.BackendNBA_Notes.payload.request.LoginRequest;
import com.project.BackendNBA_Notes.payload.request.SignupRequest;
import com.project.BackendNBA_Notes.payload.response.JwtResponse;
import com.project.BackendNBA_Notes.payload.response.UserInfoResponse;
import com.project.BackendNBA_Notes.repository.UserRepository;
import com.project.BackendNBA_Notes.security.jwt.AuthTokenFilter;
import com.project.BackendNBA_Notes.security.jwt.JwtUtils;
import com.project.BackendNBA_Notes.security.jwt.TokenBlacklistService;
import com.project.BackendNBA_Notes.security.service.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthTokenFilter authTokenFilter;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @Autowired
    private JwtUtils jwtUtils;
    /* Este método maneja las solicitudes de registro de usuarios.*/
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new UserInfoResponse(user.getId(), user.getUsername(), user.getEmail()));
    }

    /* Este método maneja las solicitudes de inicio de sesión de los usuarios
    Toma una LoginRequest con el nombre de usuario y la contraseña.
   Intenta autenticar al usuario utilizando el AuthenticationManager.*/
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        /*Se utiliza AuthenticationManager para autenticar al
        usuario utilizando las credenciales proporcionadas en loginRequest.*/
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        /*Se genera un token para el usuario*/
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.setJwtToken(jwtToken);

        userRepository.save(user);
        /*Se devuelve una respuesta HTTP exitosa (ResponseEntity.ok(...))
        con un objeto JwtResponse en el cuerpo. JwtResponse*/
        return ResponseEntity.ok(new JwtResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(),jwtToken));
    }

    /*Este método maneja las solicitudes de cierre de sesión de los usuarios.*/
    @DeleteMapping("/signout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String jwt = authTokenFilter.parseJwt(request);
        if (jwt != null) {
            tokenBlacklistService.blacklistToken(jwt);
            return ResponseEntity.ok("Log out successful!");
        } else {
            throw new RuntimeException("No se pudo obtener el token del usuario autenticado");
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        // Elimina al usuario
        userRepository.delete(user);
        return ResponseEntity.ok("User deleted successfully.");
    }

}
