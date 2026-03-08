
package com.example.securitystarter.jwt;

import com.example.securitystarter.config.JwtProperties;
import com.example.securitystarter.model.SecureUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties props;

    public String generateToken(SecureUser user) {

        Date expiryDate = new Date(System.currentTimeMillis() + props.getExpiration());
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        claims.put("userId", user.getUserId());
        claims.put("exp", expiryDate);
        return Jwts
            .builder()
            .claims()
            .add(claims)
            .subject(user.getUsername())
            .issuer("MON")
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(expiryDate)
            .and()
            .signWith(generateKey())
            .compact();
    }
    private SecretKey generateKey() {
        byte[] decode
            = Decoders.BASE64.decode(props.getSecret());

        return Keys.hmacShaKeyFor(decode);
    }


    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts
            .parser()
            .verifyWith(generateKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}
