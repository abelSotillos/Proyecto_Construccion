package app.abelsc.com.repository;

import app.abelsc.com.domain.Obra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Obra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObraRepository extends JpaRepository<Obra, Long> {}
