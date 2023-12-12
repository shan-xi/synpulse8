package com.synpulse8.ebanking.auth.controllers;

import com.synpulse8.ebanking.auth.dto.LoginReq;
import com.synpulse8.ebanking.auth.dto.LoginRes;
import com.synpulse8.ebanking.dao.account.entity.Account;
import com.synpulse8.ebanking.dao.account.repo.AccountRepository;
import com.synpulse8.ebanking.dao.client.entity.Client;
import com.synpulse8.ebanking.dao.client.repo.ClientRepository;
import com.synpulse8.ebanking.enums.Currency;
import com.synpulse8.ebanking.enums.Status;
import com.synpulse8.ebanking.response.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(
        properties = "spring.config.location=classpath:application-test.properties",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthControllerImplIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void login_Success() {
        // Initial Data
        var uid = "P-0123456789";
        var password = "12345";
        var iban = "CH93-0000-0000-0000-0000-0";
        var name = "spin";
        prepareClientAccountData(uid, password, iban, name);

        // Arrange
        LoginReq loginReq = new LoginReq(uid, password);

        // Act
        ResponseEntity<ResponseDto<LoginRes>> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/ebanking/auth/login",
                HttpMethod.POST,
                new HttpEntity<>(loginReq),
                new ParameterizedTypeReference<ResponseDto<LoginRes>>() {
                });

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
        assertEquals(Status.SUCCESS, Objects.requireNonNull(responseEntity.getBody()).status());
    }

    private void prepareClientAccountData(String uid, String password, String iban, String name) {
        var b = new BCryptPasswordEncoder();
        var encodePassword = b.encode(password);
        var account = Account.builder()
                .uid(uid)
                .currency(Currency.CHF)
                .iban(iban)
                .build();
        var clientOptional = clientRepository.findByUid(uid);
        var client = clientOptional.orElseGet(() -> Client.builder()
                .uid(uid)
                .name(name)
                .password(encodePassword)
                .build());
        client.addAccountList(account);
        clientRepository.save(client);
        account.setClient(client);
        accountRepository.save(account);
    }
}
