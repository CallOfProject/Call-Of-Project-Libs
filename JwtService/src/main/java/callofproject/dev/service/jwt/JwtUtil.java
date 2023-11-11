package callofproject.dev.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;


public final class JwtUtil
{

    private static final String secretKey = "b3p0dXJrY2FuZW1pcm51cmlrb8OnYWhtZXRrYWZhZGFydGNlam9ycGZmb2xsYWMyMDIzIQ==";

    private static final long jwtExpiration = 10_800_000L;

    private static final long refreshExpiration = 604_800_000;

    public static String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public static String generateToken(String username)
    {
        return generateToken(new HashMap<>(), username);
    }

    public static String generateToken(Map<String, Object> extraClaims, String username)
    {
        return buildToken(extraClaims, username, jwtExpiration);
    }

    public static String generateRefreshToken(String username)
    {
        return buildToken(new HashMap<>(), username, refreshExpiration);
    }

    public static String generateRefreshToken(Map<String, Object> claims, String username)
    {
        return buildToken(claims, username, refreshExpiration);
    }

    public static boolean verifyWithUsernameAndToken(String token, String username)
    {
        return isTokenValid(token, username);
    }

    private static String buildToken(Map<String, Object> extraClaims, String username, long expiration)
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

    public static boolean isTokenValid(String token, String uname)
    {
        final String username = extractUsername(token);
        return (username.equals(uname)) && !isTokenExpired(token);
    }

    private static boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    private static Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    public static String populateAuthorities(Collection<? extends GrantedAuthority> collection)
    {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection)
        {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

    private static Claims extractAllClaims(String token)
    {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static SecretKey getSignInKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
