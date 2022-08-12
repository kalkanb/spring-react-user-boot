package com.kalkanb.authority.jwt;

import com.kalkanb.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtUtils {
    private static final Logger LOGGER = Logger.getLogger("Project Logger");

    @Value("${kalkanb.app.jwtSecret}")
    private String jwtSecret;

    @Value("${kalkanb.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private BlacklistedTokenRepository blacklistedTokenRepository;

    public String generateJwtToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public Date getExpirationDateFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            if (blacklistedTokenRepository.findByToken(authToken).isPresent()) {
                LOGGER.log(Level.SEVERE, "JWT token is expired: ");
                return false;
            }
            return true;
        } catch (SignatureException e) {
            LOGGER.log(Level.SEVERE, "Invalid JWT signature: ", e);
        } catch (MalformedJwtException e) {
            LOGGER.log(Level.SEVERE, "Invalid JWT token: ", e);
        } catch (ExpiredJwtException e) {
            LOGGER.log(Level.SEVERE, "JWT token is expired: ", e);
        } catch (UnsupportedJwtException e) {
            LOGGER.log(Level.SEVERE, "JWT token is unsupported: ", e);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "JWT claims string is empty: ", e);
        }

        return false;
    }

    @Autowired
    public void setBlacklistedTokenRepository(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }
}
