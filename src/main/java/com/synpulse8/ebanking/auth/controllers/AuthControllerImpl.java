package com.synpulse8.ebanking.auth.controllers;

import com.synpulse8.ebanking.account.entity.Account;
import com.synpulse8.ebanking.auth.dto.LoginReq;
import com.synpulse8.ebanking.auth.dto.LoginRes;
import com.synpulse8.ebanking.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthControllerImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ResponseEntity<LoginRes> login(LoginReq loginReq) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.email(), loginReq.password()));
        var email = authentication.getName();
        var account = new Account(email);
        var token = jwtUtils.createToken(account);
        var loginRes = new LoginRes(email, token);
        return ResponseEntity.ok(loginRes);
    }
}
