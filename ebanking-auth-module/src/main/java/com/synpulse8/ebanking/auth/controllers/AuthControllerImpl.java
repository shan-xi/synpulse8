package com.synpulse8.ebanking.auth.controllers;

import com.synpulse8.ebanking.auth.dto.LoginReq;
import com.synpulse8.ebanking.auth.dto.LoginRes;
import com.synpulse8.ebanking.dao.account.repo.AccountRepository;
import com.synpulse8.ebanking.enums.Status;
import com.synpulse8.ebanking.exceptions.UserDataNotFoundRuntimeException;
import com.synpulse8.ebanking.response.dto.ResponseDto;
import com.synpulse8.ebanking.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final JwtUtils jwtUtils;

    public AuthControllerImpl(AuthenticationManager authenticationManager,
                              AccountRepository accountRepository,
                              JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ResponseEntity<ResponseDto<LoginRes>> login(LoginReq loginReq) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.uid(), loginReq.password()));
        } catch (AuthenticationException e) {
            throw new UserDataNotFoundRuntimeException("user account not found");
        }
        assert authentication != null;
        var uid = authentication.getName();
        var token = jwtUtils.createToken(uid);
        var loginRes = new LoginRes(token);
        return ResponseEntity.ok(
                new ResponseDto<>(
                        Status.SUCCESS,
                        loginRes
                )
        );
    }
}
