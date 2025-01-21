package app.abelsc.com.service;

import app.abelsc.com.domain.Obra;
import app.abelsc.com.repository.ObraRepository;
import app.abelsc.com.service.dto.ObraDTO;
import app.abelsc.com.service.mapper.ObraMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link app.abelsc.com.domain.Obra}.
 */
@Service
@Transactional
public class ObraService {

    private static final Logger LOG = LoggerFactory.getLogger(ObraService.class);

    private final ObraRepository obraRepository;

    private final ObraMapper obraMapper;

    public ObraService(ObraRepository obraRepository, ObraMapper obraMapper) {
        this.obraRepository = obraRepository;
        this.obraMapper = obraMapper;
    }

    /**
     * Save a obra.
     *
     * @param obraDTO the entity to save.
     * @return the persisted entity.
     */
    public ObraDTO save(ObraDTO obraDTO) {
        LOG.debug("Request to save Obra : {}", obraDTO);
        Obra obra = obraMapper.toEntity(obraDTO);
        obra = obraRepository.save(obra);
        return obraMapper.toDto(obra);
    }

    /**
     * Update a obra.
     *
     * @param obraDTO the entity to save.
     * @return the persisted entity.
     */
    public ObraDTO update(ObraDTO obraDTO) {
        LOG.debug("Request to update Obra : {}", obraDTO);
        Obra obra = obraMapper.toEntity(obraDTO);
        obra = obraRepository.save(obra);
        return obraMapper.toDto(obra);
    }

    /**
     * Partially update a obra.
     *
     * @param obraDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ObraDTO> partialUpdate(ObraDTO obraDTO) {
        LOG.debug("Request to partially update Obra : {}", obraDTO);

        return obraRepository
            .findById(obraDTO.getId())
            .map(existingObra -> {
                obraMapper.partialUpdate(existingObra, obraDTO);

                return existingObra;
            })
            .map(obraRepository::save)
            .map(obraMapper::toDto);
    }

    /**
     * Get all the obras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ObraDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Obras");
        return obraRepository.findAll(pageable).map(obraMapper::toDto);
    }

    /**
     * Get one obra by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ObraDTO> findOne(Long id) {
        LOG.debug("Request to get Obra : {}", id);
        return obraRepository.findById(id).map(obraMapper::toDto);
    }

    /**
     * Delete the obra by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Obra : {}", id);
        obraRepository.deleteById(id);
    }
}
