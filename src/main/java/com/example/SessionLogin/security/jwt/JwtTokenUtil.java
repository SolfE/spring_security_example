package com.example.SessionLogin.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

public class JwtTokenUtil {

    private static SecretKey secretToKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String createToken(String loginId, String secret, long expireTimeMs) {
        Claims claims = Jwts.claims()
                .add("loginId", loginId)
                .build();

        SecretKey key = secretToKey(secret);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(key)
                .compact();
    }

    public static String getLoginId(String token, String secret) {
        return extractClaims(token, secret).get("loginId").toString();
    }

    public static boolean isExpired(String token, String secret) {
        Date expiredDate = extractClaims(token, secret).getExpiration();
        return expiredDate.before(new Date());
    }

    private static Claims extractClaims(String token, String secret) {
        SecretKey key = secretToKey(secret);
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

}
