package app.abelsc.com.service;

import app.abelsc.com.domain.EmpleadoObra;
import app.abelsc.com.repository.EmpleadoObraRepository;
import app.abelsc.com.service.dto.EmpleadoObraDTO;
import app.abelsc.com.service.mapper.EmpleadoObraMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link app.abelsc.com.domain.EmpleadoObra}.
 */
@Service
@Transactional
public class EmpleadoObraService {

    private static final Logger LOG = LoggerFactory.getLogger(EmpleadoObraService.class);

    private final EmpleadoObraRepository empleadoObraRepository;

    private final EmpleadoObraMapper empleadoObraMapper;

    public EmpleadoObraService(EmpleadoObraRepository empleadoObraRepository, EmpleadoObraMapper empleadoObraMapper) {
        this.empleadoObraRepository = empleadoObraRepository;
        this.empleadoObraMapper = empleadoObraMapper;
    }

    /**
     * Save a empleadoObra.
     *
     * @param empleadoObraDTO the entity to save.
     * @return the persisted entity.
     */
    public EmpleadoObraDTO save(EmpleadoObraDTO empleadoObraDTO) {
        LOG.debug("Request to save EmpleadoObra : {}", empleadoObraDTO);
        EmpleadoObra empleadoObra = empleadoObraMapper.toEntity(empleadoObraDTO);
        empleadoObra = empleadoObraRepository.save(empleadoObra);
        return empleadoObraMapper.toDto(empleadoObra);
    }

    /**
     * Update a empleadoObra.
     *
     * @param empleadoObraDTO the entity to save.
     * @return the persisted entity.
     */
    public EmpleadoObraDTO update(EmpleadoObraDTO empleadoObraDTO) {
        LOG.debug("Request to update EmpleadoObra : {}", empleadoObraDTO);
        EmpleadoObra empleadoObra = empleadoObraMapper.toEntity(empleadoObraDTO);
        empleadoObra = empleadoObraRepository.save(empleadoObra);
        return empleadoObraMapper.toDto(empleadoObra);
    }

    /**
     * Partially update a empleadoObra.
     *
     * @param empleadoObraDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmpleadoObraDTO> partialUpdate(EmpleadoObraDTO empleadoObraDTO) {
        LOG.debug("Request to partially update EmpleadoObra : {}", empleadoObraDTO);

        return empleadoObraRepository
            .findById(empleadoObraDTO.getId())
            .map(existingEmpleadoObra -> {
                empleadoObraMapper.partialUpdate(existingEmpleadoObra, empleadoObraDTO);

                return existingEmpleadoObra;
            })
            .map(empleadoObraRepository::save)
            .map(empleadoObraMapper::toDto);
    }

    /**
     * Get all the empleadoObras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmpleadoObraDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all EmpleadoObras");
        return empleadoObraRepository.findAll(pageable).map(empleadoObraMapper::toDto);
    }

    /**
     * Get one empleadoObra by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmpleadoObraDTO> findOne(Long id) {
        LOG.debug("Request to get EmpleadoObra : {}", id);
        return empleadoObraRepository.findById(id).map(empleadoObraMapper::toDto);
    }

    /**
     * Delete the empleadoObra by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmpleadoObra : {}", id);
        empleadoObraRepository.deleteById(id);
    }
}
