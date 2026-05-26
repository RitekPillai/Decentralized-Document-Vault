package com.example.Decentralized.Document.Vault.services.auth;


import com.example.Decentralized.Document.Vault.model.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUtil {
    @Value("${jwt.key}")
    private  String jwtSecretKeyBase64;

    private static final String CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom random = new SecureRandom();


    public SecretKey getSecretKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKeyBase64);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token)throws SignatureException {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();


    }
    public String generateToken(User user){
        try{
            return Jwts.builder()
                    .signWith(getSecretKey())
                    .subject(user.getEmail())
                    .claim("userId",user.getUserId())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis()+1000*60*10))
                    .compact();

        } catch (Exception e) {
            throw  new JwtException("Failed to Generate Token");
        }
    }




}