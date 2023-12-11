package com.synpulse8.ebanking.auth.services;

import com.synpulse8.ebanking.auth.dto.LoginReq;
import com.synpulse8.ebanking.auth.dto.LoginRes;

public interface AuthService {
    LoginRes login(LoginReq loginReq);
}
