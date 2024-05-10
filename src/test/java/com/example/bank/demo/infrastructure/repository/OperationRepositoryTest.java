package com.example.bank.demo.infrastructure.repository;

import com.example.bank.demo.domain.model.BankAccount;
import org.assertj.core.util.BigDecimalComparator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OperationRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @BeforeEach
    void setUp() {
        //BankAccount account = new BankAccount(1L, UUID.randomUUID(), new BigDecimal("100.00"), new BigDecimal(0));
        //bankAccountRepository.save(account);
    }

    @Test
    void shouldFindEntity() {
        BankAccount savedAccount = initDatabase();

        Optional<BankAccount> bankAccount = bankAccountRepository.findById(savedAccount.getAccountId());

        assertThat(bankAccount).isNotEmpty();

        assertThat(bankAccount.get().getAccountId()).isEqualTo(savedAccount.getAccountId());
        assertThat(bankAccount.get().getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
        assertThat(bankAccount.get().getBalance())
                .usingComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualByComparingTo(savedAccount.getBalance());
        assertThat(bankAccount.get().getMontantAutorisationDeDecouvert())
                .usingComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualByComparingTo(savedAccount.getMontantAutorisationDeDecouvert());
    }

    private @NotNull BankAccount initDatabase() {
        BankAccount account = new BankAccount(1L, UUID.randomUUID(), new BigDecimal("100"), new BigDecimal("0"));
        BankAccount savedAccount = bankAccountRepository.save(account);
        return savedAccount;
    }

}