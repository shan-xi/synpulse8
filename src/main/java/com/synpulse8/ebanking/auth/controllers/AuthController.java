package com.synpulse8.ebanking.auth.controllers;

import com.synpulse8.ebanking.auth.dto.LoginReq;
import com.synpulse8.ebanking.auth.dto.LoginRes;
import com.synpulse8.ebanking.response.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "E-Banking - Auth", description = "Auth management APIs")
@RequestMapping("/auth")
public interface AuthController {
    @Operation(
            summary = "Login",
            description = "Login")
    @ApiResponses(
            value = @ApiResponse(
                    responseCode = "200",
                    description = "Login success",
                    useReturnTypeSchema = true))
    @PostMapping(value = "/login")
    ResponseEntity<ResponseDto<LoginRes>> login(
            @Schema(implementation = LoginReq.class)
            @RequestBody
            LoginReq loginReq);
}
