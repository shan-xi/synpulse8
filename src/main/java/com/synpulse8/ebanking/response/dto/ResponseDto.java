package com.synpulse8.ebanking.response.dto;

import com.synpulse8.ebanking.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO")
public record ResponseDto<T>(
        @Schema(description = "api success or fail", example = "SUCCESS")
        Status status,
        @Schema(description = "api response content")
        T data) {
}
