package com.example.bank.demo.domain.usecase;

import com.example.bank.demo.domain.adapters.useCase.MakeDepositAdapter;
import com.example.bank.demo.domain.model.BankAccount;
import com.example.bank.demo.domain.model.Operation;
import com.example.bank.demo.domain.ports.mapper.DepositResponseDtoMapperPort;
import com.example.bank.demo.domain.ports.repository.BankAccountRepositoryPort;
import com.example.bank.demo.domain.ports.repository.OperationRepositoryPort;
import com.example.bank.demo.domain.dto.response.DepositResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MakeDepositAdapterTest {

    @Mock
    private OperationRepositoryPort operationRepositoryPort;

    @Mock
    private BankAccountRepositoryPort bankAccountRepositoryPort;

    @Mock
    private DepositResponseDtoMapperPort depositResponseDtoMapperPort;

    @InjectMocks
    private MakeDepositAdapter makeDepositAdapter;

    @Test
    void testMakeDeposit() {
        //Given
        int depositValue = 100;
        BankAccount account = new BankAccount(1L, UUID.randomUUID(), new BigDecimal(depositValue), new BigDecimal(0));
        DepositResponseDto responseDto = DepositResponseDto.builder().accountNumber(account.getAccountNumber().toString()).balance(account.getBalance()).build();
        //Operation operation = new Operation(null, account, DEPOT, new BigDecimal(depositValue), new Date());

        //When
        when(bankAccountRepositoryPort.findById(1L)).thenReturn(Optional.of(account));
        when(depositResponseDtoMapperPort.mapToDepositResponseDto(account)).thenReturn(responseDto);

        DepositResponseDto response = makeDepositAdapter.makeDeposit(new BigDecimal(depositValue), 1L);

        //Then
        verify(bankAccountRepositoryPort, times(1)).findById(1L);
        verify(operationRepositoryPort, times(1)).save(any(Operation.class));
        verify(depositResponseDtoMapperPort, times(1)).mapToDepositResponseDto(account);

        assertThat(response.getBalance()).isEqualTo(new BigDecimal(depositValue));
    }
}