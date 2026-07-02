package cl.techstore.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.techstore.api.dto.LoginRequest;
import cl.techstore.api.dto.LoginResponse;
import cl.techstore.api.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    // Leemos las credenciales desde application.properties
    @Value("${techstore.auth.username}")
    private String validUsername;

    @Value("${techstore.auth.password}")
    private String validPassword;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody LoginRequest loginRequest) {
        // Validamos las credenciales (No usamos BD por requerimiento)
        if (validUsername.equals(loginRequest.getUsername()) && 
            validPassword.equals(loginRequest.getPassword())) {
            
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(new LoginResponse(token, "Bearer"));
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }
}