package com.synpulse8.ebanking.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login request")
public record LoginReq(
        @Schema(description = "User's identity ID", example = "P-0123456789")
        String uid,
        @Schema(description = "User's password", example = "12345")
        String password) {
}
