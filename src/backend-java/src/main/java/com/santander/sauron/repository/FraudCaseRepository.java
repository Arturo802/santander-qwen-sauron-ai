package com.santander.sauron.repository;

import com.santander.sauron.model.entity.FraudCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la gestión y persistencia de la entidad {@link FraudCase}.
 * Proporciona métodos CRUD estándar y operaciones de acceso a datos para los casos o patrones
 * de fraude detectados en el ecosistema Sauron AI.
 * 
 * @author Arturo Tovar
 * @version 1.0
 * @since 2026
 */
@Repository
public interface FraudCaseRepository extends JpaRepository<FraudCase, String> {
}