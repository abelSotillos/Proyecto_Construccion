package app.abelsc.com.repository;

import app.abelsc.com.domain.MaterialObra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MaterialObra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialObraRepository extends JpaRepository<MaterialObra, Long> {}
