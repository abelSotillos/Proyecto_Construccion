package app.abelsc.com.service;

import app.abelsc.com.domain.PerfilUsuario;
import app.abelsc.com.domain.User;
import app.abelsc.com.repository.PerfilUsuarioRepository;
import app.abelsc.com.repository.UserRepository;
import app.abelsc.com.security.SecurityUtils;
import app.abelsc.com.service.dto.PerfilUsuarioDTO;
import app.abelsc.com.service.mapper.PerfilUsuarioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link app.abelsc.com.domain.PerfilUsuario}.
 */
@Service
@Transactional
public class PerfilUsuarioService {

    private static final Logger LOG = LoggerFactory.getLogger(PerfilUsuarioService.class);

    private final PerfilUsuarioRepository perfilUsuarioRepository;

    private final PerfilUsuarioMapper perfilUsuarioMapper;

    private final UserRepository userRepository;

    public PerfilUsuarioService(
        PerfilUsuarioRepository perfilUsuarioRepository,
        PerfilUsuarioMapper perfilUsuarioMapper,
        UserRepository userRepository
    ) {
        this.perfilUsuarioRepository = perfilUsuarioRepository;
        this.perfilUsuarioMapper = perfilUsuarioMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a perfilUsuario.
     *
     * @param perfilUsuarioDTO the entity to save.
     * @return the persisted entity.
     */
    public PerfilUsuarioDTO save(PerfilUsuarioDTO perfilUsuarioDTO) {
        LOG.debug("Request to save PerfilUsuario : {}", perfilUsuarioDTO);
        PerfilUsuario perfilUsuario = perfilUsuarioMapper.toEntity(perfilUsuarioDTO);
        perfilUsuario = perfilUsuarioRepository.save(perfilUsuario);
        return perfilUsuarioMapper.toDto(perfilUsuario);
    }

    /**
     * Update a perfilUsuario.
     *
     * @param perfilUsuarioDTO the entity to save.
     * @return the persisted entity.
     */
    public PerfilUsuarioDTO update(PerfilUsuarioDTO perfilUsuarioDTO) {
        LOG.debug("Request to update PerfilUsuario : {}", perfilUsuarioDTO);
        PerfilUsuario perfilUsuario = perfilUsuarioMapper.toEntity(perfilUsuarioDTO);
        perfilUsuario = perfilUsuarioRepository.save(perfilUsuario);
        return perfilUsuarioMapper.toDto(perfilUsuario);
    }

    /**
     * Partially update a perfilUsuario.
     *
     * @param perfilUsuarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PerfilUsuarioDTO> partialUpdate(PerfilUsuarioDTO perfilUsuarioDTO) {
        LOG.debug("Request to partially update PerfilUsuario : {}", perfilUsuarioDTO);

        return perfilUsuarioRepository
            .findById(perfilUsuarioDTO.getId())
            .map(existingPerfilUsuario -> {
                perfilUsuarioMapper.partialUpdate(existingPerfilUsuario, perfilUsuarioDTO);

                return existingPerfilUsuario;
            })
            .map(perfilUsuarioRepository::save)
            .map(perfilUsuarioMapper::toDto);
    }

    /**
     * Get all the perfilUsuarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PerfilUsuarioDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PerfilUsuarios");
        return perfilUsuarioRepository.findAll(pageable).map(perfilUsuarioMapper::toDto);
    }

    /**
     * Get one perfilUsuario by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PerfilUsuarioDTO> findOne(Long id) {
        LOG.debug("Request to get PerfilUsuario : {}", id);
        return perfilUsuarioRepository.findById(id).map(perfilUsuarioMapper::toDto);
    }

    /**
     * Delete the perfilUsuario by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PerfilUsuario : {}", id);
        perfilUsuarioRepository.deleteById(id);
    }

    /**
     * Get current perfilUsuario.
     *
     * @return the entity.
     */
    public Optional<PerfilUsuarioDTO> findCurrent() {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow();
        User user = userRepository.findOneByLogin(login).orElseThrow();
        return perfilUsuarioRepository.findById(user.getId()).map(perfilUsuarioMapper::toDto);
    }
}
