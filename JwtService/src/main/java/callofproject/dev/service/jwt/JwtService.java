package callofproject.dev.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static callofproject.dev.service.jwt.JwtServiceBeanName.JWT_SERVICE_BEAN_NAME;

@Component(JWT_SERVICE_BEAN_NAME)
@Lazy
public class JwtService
{

    private final String secretKey = "b3p0dXJrY2FuZW1pcm51cmlrb8OnYWhtZXRrYWZhZGFydGNlam9ycGZmb2xsYWMyMDIzIQ==";

    private final long jwtExpiration = 10_800_000L;

    private final long refreshExpiration = 604_800_000;

    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username)
    {
        return generateToken(new HashMap<>(), username);
    }

    public String generateToken(Map<String, Object> extraClaims, String username)
    {
        return buildToken(extraClaims, username, jwtExpiration);
    }

    public String generateRefreshToken(String username)
    {
        return buildToken(new HashMap<>(), username, refreshExpiration);
    }

    public boolean verifyWithUsernameAndToken(String token, String username)
    {
        return isTokenValid(token, username);
    }

    private String buildToken(Map<String, Object> extraClaims, String username, long expiration)
    {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String uname)
    {
        final String username = extractUsername(token);
        return (username.equals(uname)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
