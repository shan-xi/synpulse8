package com.synpulse8.ebanking.test.auth;

import com.synpulse8.ebanking.dao.account.entity.Account;
import com.synpulse8.ebanking.dao.account.repo.AccountRepository;
import com.synpulse8.ebanking.enums.Status;
import com.synpulse8.ebanking.response.dto.ResponseDto;
import com.synpulse8.ebanking.test.auth.dto.CreateAccountReq;
import com.synpulse8.ebanking.test.auth.dto.CreateAccountRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test - Account Simulation", description = "Account Simulation APIs")
@RequestMapping("/test/account")
@RestController
public class AccountController {
    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Operation(
            summary = "Create account",
            description = "Create account")
    @ApiResponses(
            value = @ApiResponse(
                    responseCode = "200",
                    description = "Create account success",
                    useReturnTypeSchema = true))
    @PostMapping(value = "/create-account")
    ResponseEntity<ResponseDto<CreateAccountRes>> createAccount(
            @Schema(implementation = CreateAccountReq.class)
            @RequestBody
            CreateAccountReq req) {
        var b = new BCryptPasswordEncoder();
        var encodePassword = b.encode(req.password());
        var account = Account.builder()
                .uid(req.uid())
                .currency(req.currency())
                .name(req.name())
                .password(encodePassword)
                .build();
        accountRepository.save(account);
        return ResponseEntity.ok(
                new ResponseDto<>(
                        Status.SUCCESS,
                        new CreateAccountRes(
                                req.uid(),
                                encodePassword
                        )
                )
        );
    }
}
