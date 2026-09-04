package com.santander.sauron.service;

import com.santander.sauron.model.entity.Account;
import com.santander.sauron.model.entity.FraudCase;
import com.santander.sauron.model.entity.Transaction;
import com.santander.sauron.repository.AccountRepository;
import com.santander.sauron.repository.FraudCaseRepository;
import com.santander.sauron.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de lógica de negocio principal para la gestión y consulta de datos de fraude,
 * cuentas y transacciones en el ecosistema Sauron AI.
 * Actúa como intermediario entre los controladores REST y los repositorios de persistencia JPA.
 * 
 * @author Arturo Tovar
 * @version 1.0
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class FraudService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final FraudCaseRepository fraudCaseRepository;

    /**
     * Recupera el listado completo de cuentas bancarias registradas en la base de datos.
     * 
     * @return Una lista de objetos {@link Account} con todas las cuentas del sistema.
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Recupera el listado completo de todas las transacciones históricas procesadas.
     * 
     * @return Una lista de objetos {@link Transaction} con todas las transacciones almacenadas.
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Consulta y filtra aquellas transacciones que han sido marcadas específicamente como fraudulentas.
     * 
     * @return Una lista de objetos {@link Transaction} donde el indicador de fraude es verdadero.
     */
    public List<Transaction> getFraudTransactions() {
        return transactionRepository.findByIsFraudTrue();
    }

    /**
     * Recupera todos los casos o patrones de fraude complejos detectados y estructurados.
     * 
     * @return Una lista de objetos {@link FraudCase} registrados en el sistema.
     */
    public List<FraudCase> getAllFraudCases() {
        return fraudCaseRepository.findAll();
    }
}