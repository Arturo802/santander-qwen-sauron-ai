package com.santander.sauron;

import com.santander.sauron.controller.IngestController;
import com.santander.sauron.model.entity.Account;
import com.santander.sauron.model.entity.Transaction;
import com.santander.sauron.repository.AccountRepository;
import com.santander.sauron.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class IngestControllerTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private IngestController ingestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAccounts() {
        when(accountRepository.findAll()).thenReturn(Arrays.asList(new Account()));
        ResponseEntity<List<Account>> response = ingestController.getAllAccounts();
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testGetAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(new Transaction()));
        ResponseEntity<List<Transaction>> response = ingestController.getAllTransactions();
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testIngestAccountsSuccess() {
        List<Account> accounts = Arrays.asList(new Account());
        when(accountRepository.saveAll(anyList())).thenReturn(accounts);

        ResponseEntity<String> response = ingestController.ingestAccounts(accounts);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testIngestTransactionsSuccess() {
        List<Transaction> transactions = Arrays.asList(new Transaction());
        when(transactionRepository.saveAll(anyList())).thenReturn(transactions);

        ResponseEntity<String> response = ingestController.ingestTransactions(transactions);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }
}