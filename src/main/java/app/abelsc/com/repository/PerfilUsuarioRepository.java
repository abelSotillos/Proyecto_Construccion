package app.abelsc.com.repository;

import app.abelsc.com.domain.PerfilUsuario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PerfilUsuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Long> {}
