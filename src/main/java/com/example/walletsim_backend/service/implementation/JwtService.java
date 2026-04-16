package com.example.walletsim_backend.service.implementation;

import com.example.walletsim_backend.dto.response.AuthResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${SECRET_KEY}")
    private String secretkey;

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",userDetails.getAuthorities());
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userDetails.getUsername())
                .issuer("WalletSim")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 7 * 24 * 60 * 60 * 1000)) // 7 days
                .and()
                .signWith(generatedKey())
                .compact();
    }

    public String generateAccessToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",userDetails.getAuthorities());
        claims.put("type","access");
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userDetails.getUsername())
                .issuer("WalletSim")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 17 * 60 * 1000)) // 15 minutes
                .and()
                .signWith(generatedKey())
                .compact();
    }
    public String generateRefreshToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",userDetails.getAuthorities());
        claims.put("type","refresh");
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userDetails.getUsername())
                .issuer("WalletSim")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 30L * 24 * 60 * 60 * 1000)) // 30 days
                .and()
                .signWith(generatedKey())
                .compact();
    }

    private SecretKey generatedKey(){
        byte[] decode = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(decode);
    }

    public String extractUserName(String token) {
    return extractClaims(token, Claims::getSubject);
}

    public <T> T extractClaims(String token, Function<Claims, T> claimResolver){
    Claims claims = extractClaims(token);
    return claimResolver.apply(claims);
}

    private Claims extractClaims(String token) {
    return Jwts
            .parser()
            .verifyWith(generatedKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
}

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}

