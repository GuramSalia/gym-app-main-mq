package com.epam.gym_app_main_mq.service;

import com.epam.gym_app_main_mq.model.Token;
import com.epam.gym_app_main_mq.model.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    public static final String SECRET_KEY = "17E9C669A04009A080C8E13E517472B00F939EE6E18F81771E36D8B0DC35FABA";
    public static final long EXPIRATION_TIME = 86400000; // 24 hours

    private final TokenService tokenService;
    private final UserService userService;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Token token = new Token();
        String tokenString = Jwts.builder()
                                 .setClaims(extraClaims)
                                 .setSubject(userDetails.getUsername())
                                 .setIssuedAt((new Date(System.currentTimeMillis())))
                                 .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                                 .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                                 .compact();

        token.setToken(tokenString);
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        token.setExpired(false);
        token.setUser(userService.getUser(userDetails.getUsername()));
        tokenService.save(token);

        return tokenString;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.info("\n\n JwtService >> isTokenValid(token, userDetails) >> Token: " + token);
        Token tokenEntity = tokenService.findByToken(token);
        if (tokenEntity == null) {
            log.error("\n\n JwtService >> isTokenValid >> Token not found in the database");
            return false;
        }

        boolean tokenRevoked = tokenEntity.isRevoked();
        if (tokenRevoked) {
            log.error("\n\n JwtService >> isTokenValid >> Token is revoked");
            return false;
        }
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token) {
        if (!isTokenExpired(token)) {
            log.info("\n\n JwtService >> isTokenValid(token) >> Token: {} >> false", token);
            return false;
        }

        Token tokenEntity = tokenService.findByToken(token);
        return true;
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
