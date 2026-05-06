package cl.techstore.api.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // Cambiamos la generación aleatoria por una clave fija de 32 caracteres (256 bits)
    // Esto evita que los tokens mueran cada vez que reinicias el proyecto
    private final String secret = "ClaveSecretaSuperSeguraTechStore2026_DuocUC"; 
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());
    
    // Duración de 60 minutos
    private final int jwtExpirationMs = 3600000;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Imprime el error para que veas en la consola de VS Code si el token expiró o es inválido
            System.err.println("Fallo en validación de JWT: " + e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}