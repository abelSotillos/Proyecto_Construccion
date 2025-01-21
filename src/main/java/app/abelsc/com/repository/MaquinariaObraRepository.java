package app.abelsc.com.repository;

import app.abelsc.com.domain.MaquinariaObra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaquinariaObra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaquinariaObraRepository extends JpaRepository<MaquinariaObra, Long> {}
