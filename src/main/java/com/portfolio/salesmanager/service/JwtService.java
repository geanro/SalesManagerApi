package com.portfolio.salesmanager.service;

import com.portfolio.salesmanager.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class JwtService {

    @Value("${security.jwt.expiration-minutes}")
    private long EXPIRATION_MINUTES;
    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(User user, Map<String,Object> extraClaims) {
        Date issuedAt= new Date(System.currentTimeMillis());
        Date expiration= new Date(issuedAt.getTime()+(EXPIRATION_MINUTES*60*1000));

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key generateKey() {
        byte[] secretAssByte= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secretAssByte);
    }

    public String extractUsername(String jwt) {
        return ExtraAllClaims(jwt).getSubject();
    }

    private Claims ExtraAllClaims(String jwt) {
        return Jwts.parser()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
