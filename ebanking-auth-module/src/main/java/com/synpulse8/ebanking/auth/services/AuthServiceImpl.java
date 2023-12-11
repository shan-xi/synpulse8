package com.synpulse8.ebanking.auth.services;

import com.synpulse8.ebanking.auth.dto.LoginReq;
import com.synpulse8.ebanking.auth.dto.LoginRes;
import com.synpulse8.ebanking.exceptions.UserDataNotFoundRuntimeException;
import com.synpulse8.ebanking.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginRes login(LoginReq loginReq) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.uid(), loginReq.password()));
        } catch (AuthenticationException e) {
            throw new UserDataNotFoundRuntimeException("user account not found");
        }
        assert authentication != null;
        var uid = authentication.getName();
        var token = jwtUtils.createToken(uid);
        return new LoginRes(token);
    }
}
