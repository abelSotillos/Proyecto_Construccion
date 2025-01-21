package app.abelsc.com.repository;

import app.abelsc.com.domain.EmpleadoObra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmpleadoObra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpleadoObraRepository extends JpaRepository<EmpleadoObra, Long> {}
