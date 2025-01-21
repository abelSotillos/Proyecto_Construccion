package app.abelsc.com.repository;

import app.abelsc.com.domain.Maquinaria;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Maquinaria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaquinariaRepository extends JpaRepository<Maquinaria, Long> {}
