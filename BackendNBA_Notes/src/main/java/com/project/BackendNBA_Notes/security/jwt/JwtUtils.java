package com.project.BackendNBA_Notes.security.jwt;

import com.project.BackendNBA_Notes.exception.InvalidJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${billy.app.jwtSecret}")
    private String jwtSecret;

    @Value("${billy.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    public boolean validateToken(String token) throws InvalidJwtException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);


            if (isTokenExpired(token)) {
                throw new InvalidJwtException("El token ha expirado");
            }

            return true;
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtException("El token ha expirado");
        } catch (JwtException e) {
            throw new InvalidJwtException("Token de autenticación no válido");
        }
    }



    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(Date.from(Instant.now()));
    }


    private Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public String generateTokenFromUsername(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // Confirmar valores    System.out.println("Current Time: " + now);
        System.out.println("jwtExpirationMs: " + jwtExpirationMs);
        System.out.println("Expiration Time: " + expiryDate);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("iat", now);
        claims.put("exp", expiryDate);

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        return token;
    }
}
