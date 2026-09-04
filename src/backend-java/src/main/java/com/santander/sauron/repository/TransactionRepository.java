package com.santander.sauron.repository;

import com.santander.sauron.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la gestión y persistencia de la entidad {@link Transaction}.
 * Proporciona métodos CRUD estándar, además de consultas derivadas personalizadas para
 * el filtrado eficiente de transacciones financieras y alertas de fraude.
 * 
 * @author Arturo Tovar
 * @version 1.0
 * @since 2026
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    /**
     * Busca y recupera un listado de transacciones que han sido marcadas como fraudulentas.
     * Utiliza la funcionalidad de consultas derivadas de Spring Data JPA basadas en la propiedad isFraud.
     * 
     * @return Una lista de objetos {@link Transaction} cuyo indicador de fraude es verdadero.
     */
    List<Transaction> findByIsFraudTrue();
}