package com.example.bank.demo.domain.usecase;

import com.example.bank.demo.domain.adapters.service.MakeDepositUseCaseService;
import com.example.bank.demo.domain.dto.response.DepositResponseDto;
import com.example.bank.demo.domain.model.BankAccount;
import com.example.bank.demo.domain.model.SavingAccount;
import com.example.bank.demo.domain.ports.mapper.DepositResponseDtoMapperPort;
import com.example.bank.demo.domain.utils.DateProvider;
import com.example.bank.demo.infrastructure.repository.BankAccountRepository;
import com.example.bank.demo.infrastructure.repository.BankRepository;
import com.example.bank.demo.infrastructure.repository.OperationRepository;
import com.example.bank.demo.infrastructure.repository.SavingAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.example.bank.demo.domain.model.enumpackage.AccountType.CLASSIC_ACCOUNT;
import static com.example.bank.demo.domain.model.enumpackage.AccountType.SAVING_ACCOUNT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MakeDepositUseCaseServiceTest {

    /**
     * TODO: Revoir cette classe de test après !!!🔥🔥🔥
     */

    @Mock
     private BankRepository bankRepository;

    @Mock
    private DateProvider dateProvider;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private SavingAccountRepository savingAccountRepository;

    @Mock
    private DepositResponseDtoMapperPort depositResponseDtoMapperPort;

    @InjectMocks
    private MakeDepositUseCaseService makeDepositUseCaseService;

    @Test
    void shouldMakeDepositForNormalAccount() {
        //Given
        int depositValue = 100;
        BankAccount account = new BankAccount(1L, UUID.randomUUID(), new BigDecimal(depositValue), CLASSIC_ACCOUNT, new ArrayList<>(), new BigDecimal(0));
        DepositResponseDto responseDto = DepositResponseDto.builder().accountNumber(account.getAccountNumber().toString()).balance(account.getBalance()).build();

        //When
        when(bankRepository.findById(1L)).thenReturn(Optional.of(account));
        when(depositResponseDtoMapperPort.mapToDepositResponseDto(account)).thenReturn(responseDto);
        when(dateProvider.getCurrentDate()).thenReturn(LocalDateTime.of(2024, 5, 14, 16, 24, 30));

        DepositResponseDto response = makeDepositUseCaseService.makeDeposit(new BigDecimal(depositValue), 1L);

        //Then
        verify(bankRepository, times(1)).findById(1L);
        //verify(operationRepository, times(1)).save(any(Operation.class));
        verify(depositResponseDtoMapperPort, times(1)).mapToDepositResponseDto(account);

        assertThat(response.getBalance()).isEqualTo(new BigDecimal(depositValue));
    }

    @Test
    void shouldMakeDepositForSavingAccount() {
        //Given
        int initBalance = 100;
        SavingAccount account = new SavingAccount(1L, UUID.randomUUID(), new BigDecimal(initBalance), SAVING_ACCOUNT, new ArrayList<>(), new BigDecimal(1000));
        DepositResponseDto responseDto = DepositResponseDto.builder().accountNumber(account.getAccountNumber().toString()).balance(account.getBalance()).build();

        //When
        when(bankRepository.findById(1L)).thenReturn(Optional.of(account));
        when(depositResponseDtoMapperPort.mapToDepositResponseDto(account)).thenReturn(responseDto);
        when(dateProvider.getCurrentDate()).thenReturn(LocalDateTime.of(2024, 5, 14, 16, 24, 30));

        DepositResponseDto response = makeDepositUseCaseService.makeDeposit(new BigDecimal(initBalance), 1L);

        //Then
        verify(bankRepository, times(1)).findById(1L);
        //verify(operationRepository, times(1)).save(any(Operation.class));
        verify(depositResponseDtoMapperPort, times(1)).mapToDepositResponseDto(account);

        assertThat(response.getBalance()).isEqualTo(new BigDecimal(initBalance));
    }
}