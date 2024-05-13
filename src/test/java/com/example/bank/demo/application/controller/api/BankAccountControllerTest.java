package com.example.bank.demo.application.controller.api;

import com.example.bank.demo.domain.dto.response.DepositResponseDto;
import com.example.bank.demo.domain.dto.response.WithdrawalResponseDto;
import com.example.bank.demo.domain.exceptions.AccountNotFoundException;
import com.example.bank.demo.domain.ports.useCase.MakeDepositUseCase;
import com.example.bank.demo.domain.ports.useCase.MakeWithDrawalUseCase;
import com.example.bank.demo.domain.ports.useCase.OperationsReportUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.math.BigDecimal;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(BankAccountController.class)
class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MakeWithDrawalUseCase makeWithDrawalUseCase;

    @MockBean
    private MakeDepositUseCase makeDepositUseCase;

    @MockBean
    private OperationsReportUseCase operationsReportUseCase;

    @Value("classpath:/controllerTest/request/requestForMakingDeposit.json")
    private Resource requestForMakingDeposit;

    @Value("classpath:/controllerTest/request/requestForMakingWithdrawal.json")
    private Resource requestForMakingWithdrawal;

    @Value("classpath:/controllerTest/response/responseForMakingDeposit.json")
    private Resource responseForMakingDeposit;

    @Value("classpath:/controllerTest/response/responseForMakingWithdrawal.json")
    private Resource responseForMakingWithdrawal;

    @Test
    void shouldSucceedDeposit() throws Exception {
        DepositResponseDto expectedResponse = DepositResponseDto.builder()
                .accountNumber("745c6891-1122-11ef-bee2-0242ac170002")
                .balance(new BigDecimal(250))
                .build();

        when(makeDepositUseCase.makeDeposit(new BigDecimal(150), 1L)).thenReturn(expectedResponse);

        mockMvc.perform(post("/bank/services/deposit")
                        .contentType(APPLICATION_JSON)
                        .content(requestForMakingDeposit.getContentAsString(UTF_8)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(responseForMakingDeposit.getContentAsString(UTF_8), true));
    }

    @Test
    void shouldSucceedWithdrawal() throws Exception {
        WithdrawalResponseDto expectedResponse = WithdrawalResponseDto.builder()
                .accountNumber("745c6891-1122-11ef-bee2-0242ac170002")
                .balance(new BigDecimal(250))
                .build();

        when(makeWithDrawalUseCase.makeWithdrawal(new BigDecimal(150), 1L)).thenReturn(expectedResponse);

        mockMvc.perform(post("/bank/services/withdrawal")
                        .contentType(APPLICATION_JSON)
                        .content(requestForMakingWithdrawal.getContentAsString(UTF_8)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(responseForMakingWithdrawal.getContentAsString(UTF_8), true));
    }

    /*
    @Test
    void shouldTrowAccountNotFoundException() throws Exception {
        when(makeDepositUseCase.makeDeposit(new BigDecimal(150), 1L)).thenThrow(new AccountNotFoundException("Account not found for id : 1"));

        mockMvc.perform(post("/bank/services/withdrawal")
                        .contentType(APPLICATION_JSON)
                        .content(requestForMakingWithdrawal.getContentAsString(UTF_8)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(responseForMakingWithdrawal.getContentAsString(UTF_8), true));
    }
*/
}