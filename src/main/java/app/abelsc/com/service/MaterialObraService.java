package app.abelsc.com.service;

import app.abelsc.com.domain.MaterialObra;
import app.abelsc.com.repository.MaterialObraRepository;
import app.abelsc.com.service.dto.MaterialObraDTO;
import app.abelsc.com.service.mapper.MaterialObraMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link app.abelsc.com.domain.MaterialObra}.
 */
@Service
@Transactional
public class MaterialObraService {

    private static final Logger LOG = LoggerFactory.getLogger(MaterialObraService.class);

    private final MaterialObraRepository materialObraRepository;

    private final MaterialObraMapper materialObraMapper;

    public MaterialObraService(MaterialObraRepository materialObraRepository, MaterialObraMapper materialObraMapper) {
        this.materialObraRepository = materialObraRepository;
        this.materialObraMapper = materialObraMapper;
    }

    /**
     * Save a materialObra.
     *
     * @param materialObraDTO the entity to save.
     * @return the persisted entity.
     */
    public MaterialObraDTO save(MaterialObraDTO materialObraDTO) {
        LOG.debug("Request to save MaterialObra : {}", materialObraDTO);
        MaterialObra materialObra = materialObraMapper.toEntity(materialObraDTO);
        materialObra = materialObraRepository.save(materialObra);
        return materialObraMapper.toDto(materialObra);
    }

    /**
     * Update a materialObra.
     *
     * @param materialObraDTO the entity to save.
     * @return the persisted entity.
     */
    public MaterialObraDTO update(MaterialObraDTO materialObraDTO) {
        LOG.debug("Request to update MaterialObra : {}", materialObraDTO);
        MaterialObra materialObra = materialObraMapper.toEntity(materialObraDTO);
        materialObra = materialObraRepository.save(materialObra);
        return materialObraMapper.toDto(materialObra);
    }

    /**
     * Partially update a materialObra.
     *
     * @param materialObraDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MaterialObraDTO> partialUpdate(MaterialObraDTO materialObraDTO) {
        LOG.debug("Request to partially update MaterialObra : {}", materialObraDTO);

        return materialObraRepository
            .findById(materialObraDTO.getId())
            .map(existingMaterialObra -> {
                materialObraMapper.partialUpdate(existingMaterialObra, materialObraDTO);

                return existingMaterialObra;
            })
            .map(materialObraRepository::save)
            .map(materialObraMapper::toDto);
    }

    /**
     * Get all the materialObras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterialObraDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MaterialObras");
        return materialObraRepository.findAll(pageable).map(materialObraMapper::toDto);
    }

    /**
     * Get one materialObra by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MaterialObraDTO> findOne(Long id) {
        LOG.debug("Request to get MaterialObra : {}", id);
        return materialObraRepository.findById(id).map(materialObraMapper::toDto);
    }

    /**
     * Delete the materialObra by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MaterialObra : {}", id);
        materialObraRepository.deleteById(id);
    }
}
