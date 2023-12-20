package com.synpulse8.ebanking.transaction.controllers;

import com.synpulse8.ebanking.enums.Currency;
import com.synpulse8.ebanking.enums.Status;
import com.synpulse8.ebanking.security.EbankingPrincipal;
import com.synpulse8.ebanking.transaction.dto.TransactionListRes;
import com.synpulse8.ebanking.transaction.dto.TransactionSearchDto;
import com.synpulse8.ebanking.transaction.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionControllerImplUnitTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private TransactionControllerImpl transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void getTransactionList() throws Exception {
        // Arrange
        var startDate = "2023-12-01";
        var endDate = "2023-12-31";
        var page = "0";
        var size = "10";
        var baseCurrency = Currency.USD;
        var mockPrincipal = mock(EbankingPrincipal.class);
        when(mockPrincipal.getName()).thenReturn("P-0123456789");
        var mockedTransactionListRes = new TransactionListRes(1, 10, 1, 1L, baseCurrency, Collections.emptyList());
        when(transactionService.getTransactionList(any(TransactionSearchDto.class))).thenReturn(mockedTransactionListRes);

        // Act and Assert
        mockMvc.perform(get("/transactions")
                        .param("startDate", startDate)
                        .param("endDate", endDate)
                        .param("pageNumber", page)
                        .param("pageSize", size)
                        .param("baseCurrency", baseCurrency.name())
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(Status.SUCCESS.toString()))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.pageNumber").value(mockedTransactionListRes.pageNumber()))
                .andExpect(jsonPath("$.data.size").value(mockedTransactionListRes.size()))
                .andExpect(jsonPath("$.data.totalPages").value(mockedTransactionListRes.totalPages()))
                .andExpect(jsonPath("$.data.totalElements").value(mockedTransactionListRes.totalElements()));
    }
}
