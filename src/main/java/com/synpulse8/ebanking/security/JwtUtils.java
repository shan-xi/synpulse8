package com.synpulse8.ebanking.security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private final long ACCESS_TOKEN_VALIDITY = 60 * 60 * 1000;
    private JwtParser jwtParser;
    @Value("${ebanking.jwt.secret-key}")
    private String SECRET_KEY;

    public JwtUtils() {
    }

    @PostConstruct
    private void init() {
        this.jwtParser = Jwts.parser().setSigningKey(SECRET_KEY);
    }

    public String createToken(String accountUid) {
        var claims = Jwts.claims().setSubject(accountUid);
        var tokenCreateTime = new Date();
        var tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(ACCESS_TOKEN_VALIDITY));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            var token = resolveToken(req);
            return (token != null) ? parseJwtClaims(token) : null;
        } catch (ExpiredJwtException ex) {
            handleJwtException(req, "expired", ex);
            throw ex;
        } catch (Exception ex) {
            handleJwtException(req, "invalid", ex);
            throw ex;
        }
    }
    
    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    private void handleJwtException(HttpServletRequest req, String attributeName, Exception ex) {
        req.setAttribute(attributeName, ex.getMessage());
    }

    public String resolveToken(HttpServletRequest request) {

        var bearerToken = request.getHeader(TOKEN_HEADER);
        if (Objects.nonNull(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }

    public String getUid(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }
}
