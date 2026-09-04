package com.santander.sauron.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa una transacción financiera dentro del ecosistema Sauron AI.
 * Mapeada a la tabla de base de datos "SAURON_TRANSACTIONS", almacena los detalles
 * de los movimientos monetarios, cuentas involucradas y las marcas de fraude o riesgo asociadas.
 * 
 * @author Arturo Tovar
 * @version 1.0
 * @since 2026
 */
@Entity
@Table(name = "SAURON_TRANSACTIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    /**
     * Identificador único de la transacción (Clave Primaria).
     */
    @Id
    @Column(name = "tx_id", length = 50)
    private String txId;

    /**
     * Identificador de la cuenta de origen (Source ID) que emite los fondos.
     */
    @Column(name = "src_id", length = 50, nullable = false)
    private String srcId;

    /**
     * Identificador de la cuenta de destino (Destination ID) que recibe los fondos.
     */
    @Column(name = "dst_id", length = 50, nullable = false)
    private String dstId;

    /**
     * Monto o cantidad monetaria transferida en la transacción.
     */
    @Column(name = "amount", nullable = false)
    private Double amount;

    /**
     * Marca de tiempo (Timestamp) que indica la fecha y hora en que se ejecutó la transacción.
     */
    @Column(name = "timestamp", length = 30, nullable = false)
    private String timestamp;

    /**
     * Descripción, concepto o comentarios adicionales vinculados a la transacción.
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * Indicador booleano que señala si la transacción ha sido catalogada como fraudulenta (true) o legítima (false).
     */
    @Column(name = "is_fraud")
    private Boolean isFraud = false;
}