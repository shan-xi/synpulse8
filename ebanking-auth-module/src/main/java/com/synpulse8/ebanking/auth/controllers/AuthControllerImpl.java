package com.synpulse8.ebanking.auth.controllers;

import com.synpulse8.ebanking.auth.dto.LoginReq;
import com.synpulse8.ebanking.auth.dto.LoginRes;
import com.synpulse8.ebanking.auth.services.AuthService;
import com.synpulse8.ebanking.enums.Status;
import com.synpulse8.ebanking.response.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<ResponseDto<LoginRes>> login(LoginReq req) {
        var res = authService.login(req);
        return ResponseEntity.ok(
                new ResponseDto<>(
                        Status.SUCCESS,
                        res
                )
        );
    }
}
