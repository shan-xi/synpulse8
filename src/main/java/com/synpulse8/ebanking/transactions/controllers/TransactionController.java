package com.synpulse8.ebanking.transactions.controllers;

import com.synpulse8.ebanking.transactions.dto.TransactionListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "E-Banking - Transaction", description = "Transaction management APIs")
@RequestMapping("/transactions")
public interface TransactionController {
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Query user transaction records",
            description = "Query user transaction records")
    @ApiResponses(
            value = @ApiResponse(
                    responseCode = "200",
                    description = "Query user transaction records success",
                    useReturnTypeSchema = true))
    @GetMapping(value = "/{account_id}")
    ResponseEntity<TransactionListRes> getTransactionList(@PathVariable("account_id") String account_id);
}
