package app.abelsc.com.service;

import app.abelsc.com.domain.Material;
import app.abelsc.com.repository.MaterialRepository;
import app.abelsc.com.service.dto.MaterialDTO;
import app.abelsc.com.service.mapper.MaterialMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link app.abelsc.com.domain.Material}.
 */
@Service
@Transactional
public class MaterialService {

    private static final Logger LOG = LoggerFactory.getLogger(MaterialService.class);

    private final MaterialRepository materialRepository;

    private final MaterialMapper materialMapper;

    public MaterialService(MaterialRepository materialRepository, MaterialMapper materialMapper) {
        this.materialRepository = materialRepository;
        this.materialMapper = materialMapper;
    }

    /**
     * Save a material.
     *
     * @param materialDTO the entity to save.
     * @return the persisted entity.
     */
    public MaterialDTO save(MaterialDTO materialDTO) {
        LOG.debug("Request to save Material : {}", materialDTO);
        Material material = materialMapper.toEntity(materialDTO);
        material = materialRepository.save(material);
        return materialMapper.toDto(material);
    }

    /**
     * Update a material.
     *
     * @param materialDTO the entity to save.
     * @return the persisted entity.
     */
    public MaterialDTO update(MaterialDTO materialDTO) {
        LOG.debug("Request to update Material : {}", materialDTO);
        Material material = materialMapper.toEntity(materialDTO);
        material = materialRepository.save(material);
        return materialMapper.toDto(material);
    }

    /**
     * Partially update a material.
     *
     * @param materialDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MaterialDTO> partialUpdate(MaterialDTO materialDTO) {
        LOG.debug("Request to partially update Material : {}", materialDTO);

        return materialRepository
            .findById(materialDTO.getId())
            .map(existingMaterial -> {
                materialMapper.partialUpdate(existingMaterial, materialDTO);

                return existingMaterial;
            })
            .map(materialRepository::save)
            .map(materialMapper::toDto);
    }

    /**
     * Get all the materials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterialDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Materials");
        return materialRepository.findAll(pageable).map(materialMapper::toDto);
    }

    /**
     * Get one material by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MaterialDTO> findOne(Long id) {
        LOG.debug("Request to get Material : {}", id);
        return materialRepository.findById(id).map(materialMapper::toDto);
    }

    /**
     * Delete the material by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Material : {}", id);
        materialRepository.deleteById(id);
    }
}
