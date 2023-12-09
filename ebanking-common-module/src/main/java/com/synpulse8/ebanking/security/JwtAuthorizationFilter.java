package com.synpulse8.ebanking.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final ObjectMapper mapper;

    public JwtAuthorizationFilter(JwtUtils jwtUtils, ObjectMapper mapper) {
        this.jwtUtils = jwtUtils;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            processAuthentication(request);
        } catch (AuthenticationException e) {
            handleAuthenticationError(response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void processAuthentication(HttpServletRequest request) throws AuthenticationException {
        var accessToken = jwtUtils.resolveToken(request);

        if (Objects.isNull(accessToken)) {
            return;
        }

        log.debug("Token: {}", accessToken);

        var claims = jwtUtils.resolveClaims(request);

        if (Objects.nonNull(claims) && jwtUtils.validateClaims(claims)) {
            var uid = claims.getSubject();
            var authentication = new UsernamePasswordAuthenticationToken(uid, "", new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void handleAuthenticationError(HttpServletResponse response, Exception e) throws IOException {
        var errorDetails = Map.of(
                "message", "Authentication Error",
                "details", e.getMessage()
        );

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), errorDetails);
    }
}