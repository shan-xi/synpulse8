package com.synpulse8.ebanking.auth.services;

import com.synpulse8.ebanking.auth.dto.LoginReq;
import com.synpulse8.ebanking.exceptions.UserDataNotFoundRuntimeException;
import com.synpulse8.ebanking.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Successful() {
        // Arrange
        var uid = "testUser";
        var token = "testToken";
        var password = "testPassword";
        var loginReq = new LoginReq(uid, password);
        var authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(uid);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.createToken(uid)).thenReturn(token);

        // Act
        var actualResult = authService.login(loginReq);

        // Assert
        assertNotNull(actualResult);
        assertEquals(token, actualResult.token());

        // Verify that authenticationManager.authenticate is called with the correct arguments
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(uid, password));
        // Verify that jwtUtils.createToken is called with the correct argument
        verify(jwtUtils).createToken(uid);
    }

    @Test
    void login_UserNotFound() {
        // Arrange
        var uid = "nonexistentUser";
        var password = "testPassword";
        var loginReq = new LoginReq(uid, password);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("user account not found") {
                });

        // Act and Assert
        assertThrows(UserDataNotFoundRuntimeException.class, () -> authService.login(loginReq));

        // Verify that authenticationManager.authenticate is called with the correct arguments
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(uid, password));
        // Verify that jwtUtils.createToken is not called in case of exception
        verifyNoInteractions(jwtUtils);
    }
}
