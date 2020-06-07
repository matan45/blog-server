package com.matan.blog.blog.security;

import com.matan.blog.blog.exception.JwtException;
import com.matan.blog.blog.exception.JwtExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;

@Service
public class JwtProvider {

    private KeyStore keyStore = null;

    @Value("${jwt.secret}")
    private String SecretKey;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, SecretKey.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new JwtException("Exception occurred while loading keystore");
        }

    }

    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();
        return Jwts.builder().setSubject(principal.getUsername()).signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis))).compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springBlog", SecretKey.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new JwtException("Exception occured while retrieving public key from keystore");
        }
    }

    public boolean validateToken(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        try {
            Jwts.parserBuilder().setSigningKey(getPublickey()).build().parseClaimsJws(jwt);
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException("JWT token Expired");
        }

        return true;
    }

    public String getEmailFromJWT(String token) {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getPublickey()).build().parseClaimsJws(token);

        return claims.getBody().getSubject();
    }

    public String generateTokenWithEmail(String email) {
        return Jwts.builder().setSubject(email).setIssuedAt(Date.from(Instant.now())).signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis))).compact();
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }

    private PublicKey getPublickey() {
        try {
            return keyStore.getCertificate("springBlog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new JwtException("Exception occurred while retrieving public key from keystore");
        }
    }
}
