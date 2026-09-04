package com.santander.sauron.controller;

import com.santander.sauron.model.entity.Account;
import com.santander.sauron.model.entity.FraudCase;
import com.santander.sauron.model.entity.Transaction;
import com.santander.sauron.service.FraudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST principal para el ecosistema Sauron AI.
 * Expone endpoints seguros para la consulta de cuentas, transacciones generales,
 * transacciones marcadas como fraude y casos de fraude detectados.
 * 
 * @author Arturo Tovar
 * @version 1.0
 * @since 2026
 */
@RestController
@RequestMapping("/api/sauron")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class FraudController {

    private final FraudService fraudService;

    /**
     * Obtiene el listado completo de cuentas registradas en el sistema.
     * 
     * @return ResponseEntity con la lista de objetos {@link Account} y código HTTP 200 (OK).
     */
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok(fraudService.getAllAccounts());
    }

    /**
     * Obtiene el listado completo de transacciones procesadas por el sistema.
     * 
     * @return ResponseEntity con la lista de objetos {@link Transaction} y código HTTP 200 (OK).
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions() {
        return ResponseEntity.ok(fraudService.getAllTransactions());
    }

    /**
     * Filtra y obtiene únicamente aquellas transacciones que han sido catalogadas como fraudulentas.
     * 
     * @return ResponseEntity con la lista de {@link Transaction} fraudulentas y código HTTP 200 (OK).
     */
    @GetMapping("/fraud-transactions")
    public ResponseEntity<List<Transaction>> getFraudTransactions() {
        return ResponseEntity.ok(fraudService.getFraudTransactions());
    }

    /**
     * Obtiene el listado detallado de todos los casos de fraude abiertos o gestionados por los agentes.
     * 
     * @return ResponseEntity con la lista de objetos {@link FraudCase} y código HTTP 200 (OK).
     */
    @GetMapping("/fraud-cases")
    public ResponseEntity<List<FraudCase>> getFraudCases() {
        return ResponseEntity.ok(fraudService.getAllFraudCases());
    }
}