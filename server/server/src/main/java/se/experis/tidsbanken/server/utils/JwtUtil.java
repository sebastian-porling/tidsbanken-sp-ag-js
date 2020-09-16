package se.experis.tidsbanken.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private byte[] SECRET =  Base64.getDecoder().decode("AUddQNAHCvz2yB5mdZXFdtJE0BGWyZ2chUwjDC/fc=");

    public String extractAudience(String token) {
        return extractClaim(token, Claims::getAudience);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, String audience) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), audience);
    }

    private String createToken(Map<String, Object> claims, String subject, String audience) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setAudience(audience)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }

    public Boolean validateToken(String token, String audience) {
        final String tokenAudience = extractAudience(token);
        return (tokenAudience.equals(audience) && !isTokenExpired(token));
    }
}
