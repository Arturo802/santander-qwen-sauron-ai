package com.santander.sauron;

import com.santander.sauron.controller.FraudController;
import com.santander.sauron.model.entity.Account;
import com.santander.sauron.model.entity.FraudCase;
import com.santander.sauron.model.entity.Transaction;
import com.santander.sauron.service.FraudService;
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

class FraudControllerTest {

    @Mock
    private FraudService fraudService;

    @InjectMocks
    private FraudController fraudController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccounts() {
        when(fraudService.getAllAccounts()).thenReturn(Arrays.asList(new Account()));
        ResponseEntity<List<Account>> response = fraudController.getAccounts();
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        verify(fraudService, times(1)).getAllAccounts();
    }

    @Test
    void testGetTransactions() {
        when(fraudService.getAllTransactions()).thenReturn(Arrays.asList(new Transaction()));
        ResponseEntity<List<Transaction>> response = fraudController.getTransactions();
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        verify(fraudService, times(1)).getAllTransactions();
    }

    @Test
    void testGetFraudTransactions() {
        when(fraudService.getFraudTransactions()).thenReturn(Arrays.asList(new Transaction()));
        ResponseEntity<List<Transaction>> response = fraudController.getFraudTransactions();
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        verify(fraudService, times(1)).getFraudTransactions();
    }

    @Test
    void testGetFraudCases() {
        when(fraudService.getAllFraudCases()).thenReturn(Arrays.asList(new FraudCase()));
        ResponseEntity<List<FraudCase>> response = fraudController.getFraudCases();
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        verify(fraudService, times(1)).getAllFraudCases();
    }
}