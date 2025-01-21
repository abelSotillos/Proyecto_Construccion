package app.abelsc.com.service;

import app.abelsc.com.domain.Maquinaria;
import app.abelsc.com.repository.MaquinariaRepository;
import app.abelsc.com.service.dto.MaquinariaDTO;
import app.abelsc.com.service.mapper.MaquinariaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link app.abelsc.com.domain.Maquinaria}.
 */
@Service
@Transactional
public class MaquinariaService {

    private static final Logger LOG = LoggerFactory.getLogger(MaquinariaService.class);

    private final MaquinariaRepository maquinariaRepository;

    private final MaquinariaMapper maquinariaMapper;

    public MaquinariaService(MaquinariaRepository maquinariaRepository, MaquinariaMapper maquinariaMapper) {
        this.maquinariaRepository = maquinariaRepository;
        this.maquinariaMapper = maquinariaMapper;
    }

    /**
     * Save a maquinaria.
     *
     * @param maquinariaDTO the entity to save.
     * @return the persisted entity.
     */
    public MaquinariaDTO save(MaquinariaDTO maquinariaDTO) {
        LOG.debug("Request to save Maquinaria : {}", maquinariaDTO);
        Maquinaria maquinaria = maquinariaMapper.toEntity(maquinariaDTO);
        maquinaria = maquinariaRepository.save(maquinaria);
        return maquinariaMapper.toDto(maquinaria);
    }

    /**
     * Update a maquinaria.
     *
     * @param maquinariaDTO the entity to save.
     * @return the persisted entity.
     */
    public MaquinariaDTO update(MaquinariaDTO maquinariaDTO) {
        LOG.debug("Request to update Maquinaria : {}", maquinariaDTO);
        Maquinaria maquinaria = maquinariaMapper.toEntity(maquinariaDTO);
        maquinaria = maquinariaRepository.save(maquinaria);
        return maquinariaMapper.toDto(maquinaria);
    }

    /**
     * Partially update a maquinaria.
     *
     * @param maquinariaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MaquinariaDTO> partialUpdate(MaquinariaDTO maquinariaDTO) {
        LOG.debug("Request to partially update Maquinaria : {}", maquinariaDTO);

        return maquinariaRepository
            .findById(maquinariaDTO.getId())
            .map(existingMaquinaria -> {
                maquinariaMapper.partialUpdate(existingMaquinaria, maquinariaDTO);

                return existingMaquinaria;
            })
            .map(maquinariaRepository::save)
            .map(maquinariaMapper::toDto);
    }

    /**
     * Get all the maquinarias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MaquinariaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Maquinarias");
        return maquinariaRepository.findAll(pageable).map(maquinariaMapper::toDto);
    }

    /**
     * Get one maquinaria by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MaquinariaDTO> findOne(Long id) {
        LOG.debug("Request to get Maquinaria : {}", id);
        return maquinariaRepository.findById(id).map(maquinariaMapper::toDto);
    }

    /**
     * Delete the maquinaria by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Maquinaria : {}", id);
        maquinariaRepository.deleteById(id);
    }
}
