package se.experis.tidsbanken.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import se.experis.tidsbanken.server.models.AppUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private String SECRET = "test";

    /**
     *
     * @param token
     * @return
     */
    public String extractAudience(String token) {
        return extractClaim(token, Claims::getAudience);
    }

    /**
     *
     * @param token
     * @return
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     *
     * @param token
     * @return
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     *
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
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
     *
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     *
     * @param user
     * @param audience
     * @return
     */
    public String generateToken(AppUser user, String audience) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.email, audience);
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
    public Boolean validateToken(String token, AppUser user, String audience) {
        final String tokenAudience = extractAudience(token);
        final String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(user.email) && tokenAudience.equals(audience) && !isTokenExpired(token));
    }

    /**
     * Checks if the token is valid by checking token email and expiration
     * @param token jwt token
     * @param user user
     * @return true if valid
     */
    public Boolean validateToken(String token, AppUser user) {
        final String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(user.email) && !isTokenExpired(token));
    }

}
