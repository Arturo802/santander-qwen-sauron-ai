package com.santander.sauron.controller;

import com.santander.sauron.model.entity.Account;
import com.santander.sauron.model.entity.Transaction;
import com.santander.sauron.repository.AccountRepository;
import com.santander.sauron.repository.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de la ingesta y consulta masiva de datos en el sistema Sauron.
 * Gestiona la recepción de cuentas y transacciones entrantes para su persistencia directa
 * en la base de datos Oracle, además de proveer endpoints de consulta general.
 * 
 * @author Arturo Tovar
 * @version 1.0
 * @since 2026
 */
@RestController
@RequestMapping("/api/ingest")
@CrossOrigin(origins = "http://localhost:4200") // Permite la comunicación segura con Angular
public class IngestController {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructor para la inyección de dependencias de los repositorios de datos.
     * 
     * @-param accountRepository Repositorio JPA para la entidad {@link Account}
     * @-param transactionRepository Repositorio JPA para la entidad {@link Transaction}
     */
    public IngestController(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    // --- ENDPOINTS GET PARA CONSULTAR DATOS DESDE EL FRONTEND ---

    /**
     * Obtiene el listado completo de todas las cuentas almacenadas en la base de datos.
     * 
     * @return ResponseEntity que contiene la lista de cuentas y código HTTP 200 (OK).
     */
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return ResponseEntity.ok(accounts);
    }

    /**
     * Obtiene el listado completo de todas las transacciones históricas registradas.
     * 
     * @return ResponseEntity que contiene la lista de transacciones y código HTTP 200 (OK).
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return ResponseEntity.ok(transactions);
    }

    // --- ENDPOINTS POST EXISTENTES PARA INGESTA DE DATOS ---

    /**
     * Ingesta y guarda un lote de cuentas nuevas de forma transaccional en Oracle.
     * 
     * @param accounts Lista de objetos {@link Account} recibidos en el cuerpo de la petición (JSON).
     * @return ResponseEntity con mensaje de éxito y cantidad de registros, o un error 500 en caso de fallo.
     */
    @PostMapping("/accounts")
    @Transactional
    public ResponseEntity<String> ingestAccounts(@RequestBody List<Account> accounts) {
        try {
            accountRepository.saveAll(accounts);
            return ResponseEntity.ok("Se han guardado " + accounts.size() + " cuentas con éxito en Oracle.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al guardar cuentas: " + e.getMessage());
        }
    }

    /**
     * Ingesta y guarda un lote de transacciones de forma transaccional en Oracle.
     * 
     * @param transactions Lista de objetos {@link Transaction} recibidos en el cuerpo de la petición (JSON).
     * @return ResponseEntity con mensaje de éxito y cantidad de registros, o un error 500 en caso de fallo.
     */
    @PostMapping("/transactions")
    @Transactional
    public ResponseEntity<String> ingestTransactions(@RequestBody List<Transaction> transactions) {
        try {
            transactionRepository.saveAll(transactions);
            return ResponseEntity.ok("Se han guardado " + transactions.size() + " transacciones con éxito en Oracle.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al guardar transacciones: " + e.getMessage());
        }
    }
}