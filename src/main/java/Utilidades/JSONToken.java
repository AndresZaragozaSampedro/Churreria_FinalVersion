package Utilidades;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JSONToken
{
    private static final String SECRET_KEY = "secret";

    public static String generateToken(String username)
    {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 8)) // 1 seg * 60 = 1 min -> 1 min * 60 = 1 Hora -> 1 h * 8 = 8 horas
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

    }

    public static String extractUsername(String token)
    {
        return getClaims(token).getSubject();
    }

    public static boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    private static Claims getClaims(String token)
    {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private static boolean isTokenExpired(String token)
    {
        return getClaims(token).getExpiration().before(new Date());
    }
}
