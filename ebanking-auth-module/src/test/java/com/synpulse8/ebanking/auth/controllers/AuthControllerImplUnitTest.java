package com.synpulse8.ebanking.auth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpulse8.ebanking.auth.dto.LoginReq;
import com.synpulse8.ebanking.auth.dto.LoginRes;
import com.synpulse8.ebanking.auth.services.AuthService;
import com.synpulse8.ebanking.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class AuthControllerImplUnitTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthControllerImpl authController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void login_Success() throws Exception {
        // Arrange
        var testUser = "testUser";
        var testToken = "testToken";
        var testPassword = "testPassword";
        var loginReq = new LoginReq(testUser, testPassword);
        var loginRes = new LoginRes(testToken);
        when(authService.login(loginReq)).thenReturn(loginRes);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(Status.SUCCESS.name()))
                .andExpect(jsonPath("$.data.token").value(testToken));
    }
}
