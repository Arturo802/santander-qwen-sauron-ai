package com.santander.sauron.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa un caso o patrón de fraude detectado dentro del ecosistema Sauron AI.
 * Mapeada a la tabla de base de datos "SAURON_FRAUD_CASES", almacena información
 * sobre estructuras complejas de fraude (como grafos o rutas sospechosas) identificadas
 * por los módulos de análisis.
 * 
 * @author Arturo Tovar
 * @version 1.0
 * @since 2026
 */
@Entity
@Table(name = "SAURON_FRAUD_CASES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FraudCase {

    /**
     * Identificador único del patrón de fraude detectado (Clave Primaria).
     */
    @Id
    @Column(name = "pattern_id", length = 50)
    private String patternId;

    /**
     * Identificador de la cuenta bancaria de origen o inicio donde se detectó el patrón sospechoso.
     */
    @Column(name = "start_acc_id", length = 50, nullable = false)
    private String startAccId;

    /**
     * Tipo o clasificación de la tipología de fraude identificada.
     */
    @Column(name = "pattern_type", length = 50)
    private String patternType;

    /**
     * Nivel de profundidad alcanzado por el análisis del patrón o grafo de transacciones.
     */
    @Column(name = "depth")
    private Integer depth;

    /**
     * Cadena de texto que almacena la lista de cuentas involucradas en el caso,
     * separadas por el carácter pipe ('|') tal como se genera desde los scripts de análisis en Python.
     */
    @Column(name = "involved_accounts", length = 4000, nullable = false)
    private String involvedAccounts;
}