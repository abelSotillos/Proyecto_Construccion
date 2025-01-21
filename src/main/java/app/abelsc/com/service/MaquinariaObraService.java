package app.abelsc.com.service;

import app.abelsc.com.domain.MaquinariaObra;
import app.abelsc.com.repository.MaquinariaObraRepository;
import app.abelsc.com.service.dto.MaquinariaObraDTO;
import app.abelsc.com.service.mapper.MaquinariaObraMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link app.abelsc.com.domain.MaquinariaObra}.
 */
@Service
@Transactional
public class MaquinariaObraService {

    private static final Logger LOG = LoggerFactory.getLogger(MaquinariaObraService.class);

    private final MaquinariaObraRepository maquinariaObraRepository;

    private final MaquinariaObraMapper maquinariaObraMapper;

    public MaquinariaObraService(MaquinariaObraRepository maquinariaObraRepository, MaquinariaObraMapper maquinariaObraMapper) {
        this.maquinariaObraRepository = maquinariaObraRepository;
        this.maquinariaObraMapper = maquinariaObraMapper;
    }

    /**
     * Save a maquinariaObra.
     *
     * @param maquinariaObraDTO the entity to save.
     * @return the persisted entity.
     */
    public MaquinariaObraDTO save(MaquinariaObraDTO maquinariaObraDTO) {
        LOG.debug("Request to save MaquinariaObra : {}", maquinariaObraDTO);
        MaquinariaObra maquinariaObra = maquinariaObraMapper.toEntity(maquinariaObraDTO);
        maquinariaObra = maquinariaObraRepository.save(maquinariaObra);
        return maquinariaObraMapper.toDto(maquinariaObra);
    }

    /**
     * Update a maquinariaObra.
     *
     * @param maquinariaObraDTO the entity to save.
     * @return the persisted entity.
     */
    public MaquinariaObraDTO update(MaquinariaObraDTO maquinariaObraDTO) {
        LOG.debug("Request to update MaquinariaObra : {}", maquinariaObraDTO);
        MaquinariaObra maquinariaObra = maquinariaObraMapper.toEntity(maquinariaObraDTO);
        maquinariaObra = maquinariaObraRepository.save(maquinariaObra);
        return maquinariaObraMapper.toDto(maquinariaObra);
    }

    /**
     * Partially update a maquinariaObra.
     *
     * @param maquinariaObraDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MaquinariaObraDTO> partialUpdate(MaquinariaObraDTO maquinariaObraDTO) {
        LOG.debug("Request to partially update MaquinariaObra : {}", maquinariaObraDTO);

        return maquinariaObraRepository
            .findById(maquinariaObraDTO.getId())
            .map(existingMaquinariaObra -> {
                maquinariaObraMapper.partialUpdate(existingMaquinariaObra, maquinariaObraDTO);

                return existingMaquinariaObra;
            })
            .map(maquinariaObraRepository::save)
            .map(maquinariaObraMapper::toDto);
    }

    /**
     * Get all the maquinariaObras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MaquinariaObraDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MaquinariaObras");
        return maquinariaObraRepository.findAll(pageable).map(maquinariaObraMapper::toDto);
    }

    /**
     * Get one maquinariaObra by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MaquinariaObraDTO> findOne(Long id) {
        LOG.debug("Request to get MaquinariaObra : {}", id);
        return maquinariaObraRepository.findById(id).map(maquinariaObraMapper::toDto);
    }

    /**
     * Delete the maquinariaObra by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MaquinariaObra : {}", id);
        maquinariaObraRepository.deleteById(id);
    }
}
