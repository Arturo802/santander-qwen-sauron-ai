package com.santander.sauron.repository;

import com.santander.sauron.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Spring Data JPA para la gestión y persistencia de la entidad {@link Account}.
 * Proporciona métodos CRUD estándar y operaciones de acceso a datos para las cuentas bancarias
 * almacenadas en la base de datos Oracle.
 * 
 * @author Arturo Tovar
 * @version 1.0
 * @since 2026
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    // Hereda métodos estándar como save, findById, findAll, etc.
}