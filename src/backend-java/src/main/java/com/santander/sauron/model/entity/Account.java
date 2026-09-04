package com.santander.sauron.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa una cuenta bancaria dentro del ecosistema Sauron AI.
 * Mapeada a la tabla de base de datos "SAURON_ACCOUNTS", almacena información
 * financiera, identificadores de titularidad y métricas de riesgo evaluadas.
 * 
 * @author Arturo Tovar
 * @version 1.0
 * @since 2026
 */
@Entity
@Table(name = "SAURON_ACCOUNTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    /**
     * Identificador único de la cuenta bancaria (Clave Primaria).
     */
    @Id
    @Column(name = "account_id", length = 50)
    private String accountId;

    /**
     * Nombre completo o razón social del cliente titular de la cuenta.
     */
    @Column(name = "customer_name", length = 100, nullable = false)
    private String customerName;

    /**
     * Saldo actual disponible en la cuenta.
     */
    @Column(name = "balance")
    private Double balance;

    /**
     * Puntuación de riesgo (Risk Score) asignada o calculada mediante los modelos y agentes de análisis.
     */
    @Column(name = "risk_score")
    private Double riskScore;

    /**
     * Fecha y hora de registro o apertura de la cuenta en el sistema.
     */
    @Column(name = "creation_date", length = 30)
    private String creationDate;
}