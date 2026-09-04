package com.santander.sauron;

import com.santander.sauron.model.entity.Account;
import com.santander.sauron.model.entity.FraudCase;
import com.santander.sauron.model.entity.Transaction;
import com.santander.sauron.repository.AccountRepository;
import com.santander.sauron.repository.FraudCaseRepository;
import com.santander.sauron.repository.TransactionRepository;
import com.santander.sauron.service.FraudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class FraudServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private FraudCaseRepository fraudCaseRepository;

    @InjectMocks
    private FraudService fraudService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAccounts() {
        Account acc = new Account();
        when(accountRepository.findAll()).thenReturn(Arrays.asList(acc));

        List<Account> accounts = fraudService.getAllAccounts();

        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTransactions() {
        Transaction tx = new Transaction();
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(tx));

        List<Transaction> transactions = fraudService.getAllTransactions();

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetFraudTransactions() {
        Transaction fraudTx = new Transaction();
        when(transactionRepository.findByIsFraudTrue()).thenReturn(Arrays.asList(fraudTx));

        List<Transaction> fraudTransactions = fraudService.getFraudTransactions();

        assertNotNull(fraudTransactions);
        assertEquals(1, fraudTransactions.size());
        verify(transactionRepository, times(1)).findByIsFraudTrue();
    }

    @Test
    void testGetAllFraudCases() {
        FraudCase fraudCase = new FraudCase();
        when(fraudCaseRepository.findAll()).thenReturn(Arrays.asList(fraudCase));

        List<FraudCase> fraudCases = fraudService.getAllFraudCases();

        assertNotNull(fraudCases);
        assertEquals(1, fraudCases.size());
        verify(fraudCaseRepository, times(1)).findAll();
    }
}