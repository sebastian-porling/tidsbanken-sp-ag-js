package se.experis.tidsbanken.server.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.experis.tidsbanken.server.models.User;

import java.util.*;
import java.util.function.Function;

/**
 * Generates and validates JWT tokens used for Http Requests and sockets
 */
@Service
public class JwtUtil {

    @Value("${jwt.secret}") private String SECRET;

    /**
     * Extracts the user id from the JWT
     * @param token String JWT
     * @return Long, the user id
     */
    public Long extractUserId(String token) {
        final Claims claims = extractAllClaims(token);
        return Long.valueOf(claims.get("user_id").toString());
    }

    /**
     * Extracts the user role
     * @param token String, JWT
     * @return the user role
     */
    public String extractAudience(String token) {
        return extractClaim(token, Claims::getAudience);
    }

    /**
     * Extracts the user email
     * @param token String, JWT
     * @return String, user email
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration Date
     * @param token String, JWT
     * @return Expiration Date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Function to extract the given claims
     * @param token String, JWT
     * @param claimsResolver Function, with claim to extract
     * @param <T> Function
     * @return Claim any type
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     *
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the JWT is expired
     * @param token String, JWT
     * @return true if expired
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT of the given user
     * @param user User the JWT owns to
     * @param audience the user role
     * @return JWT token as a string
     */
    public String generateToken(User user, String audience) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        return createToken(claims, user.getEmail(), audience);
    }

    /**
     * Creates a JWT token
     * @param claims other user info
     * @param subject user email
     * @param audience user role
     * @return jwt token
     * @throws Exception if subject or audience is wrong
     */
    private String createToken(Map<String, Object> claims, String subject, String audience) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setAudience(audience)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }

    /**
     * Checks if the token is valid by checking token email and expiration
     * @param token jwt token
     * @param user user
     * @param audience user role
     * @return true if valid
     */
    public Boolean validateToken(String token, User user, String audience) {
        final String tokenAudience = extractAudience(token);
        final Long tokenUserId = extractUserId(token);
        return (tokenUserId.equals(user.getId()) && tokenAudience.equals(audience) && !isTokenExpired(token));
    }

    /**
     * Checks if the token is valid by checking token email and expiration
     * @param token jwt token
     * @param user user
     * @return true if valid
     */
    public Boolean validateToken(String token, User user) {
        final Long tokenUserId = extractUserId(token);
        return (tokenUserId.equals(user.getId()) && !isTokenExpired(token));
    }

}
