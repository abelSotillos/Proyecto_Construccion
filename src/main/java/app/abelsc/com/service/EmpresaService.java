package app.abelsc.com.service;

import app.abelsc.com.domain.Empresa;
import app.abelsc.com.repository.EmpresaRepository;
import app.abelsc.com.service.dto.EmpresaDTO;
import app.abelsc.com.service.mapper.EmpresaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link app.abelsc.com.domain.Empresa}.
 */
@Service
@Transactional
public class EmpresaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmpresaService.class);

    private final EmpresaRepository empresaRepository;

    private final EmpresaMapper empresaMapper;

    public EmpresaService(EmpresaRepository empresaRepository, EmpresaMapper empresaMapper) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
    }

    /**
     * Save a empresa.
     *
     * @param empresaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmpresaDTO save(EmpresaDTO empresaDTO) {
        LOG.debug("Request to save Empresa : {}", empresaDTO);
        Empresa empresa = empresaMapper.toEntity(empresaDTO);
        empresa = empresaRepository.save(empresa);
        return empresaMapper.toDto(empresa);
    }

    /**
     * Update a empresa.
     *
     * @param empresaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmpresaDTO update(EmpresaDTO empresaDTO) {
        LOG.debug("Request to update Empresa : {}", empresaDTO);
        Empresa empresa = empresaMapper.toEntity(empresaDTO);
        empresa = empresaRepository.save(empresa);
        return empresaMapper.toDto(empresa);
    }

    /**
     * Partially update a empresa.
     *
     * @param empresaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmpresaDTO> partialUpdate(EmpresaDTO empresaDTO) {
        LOG.debug("Request to partially update Empresa : {}", empresaDTO);

        return empresaRepository
            .findById(empresaDTO.getId())
            .map(existingEmpresa -> {
                empresaMapper.partialUpdate(existingEmpresa, empresaDTO);

                return existingEmpresa;
            })
            .map(empresaRepository::save)
            .map(empresaMapper::toDto);
    }

    /**
     * Get all the empresas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmpresaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Empresas");
        return empresaRepository.findAll(pageable).map(empresaMapper::toDto);
    }

    /**
     * Get one empresa by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmpresaDTO> findOne(Long id) {
        LOG.debug("Request to get Empresa : {}", id);
        return empresaRepository.findById(id).map(empresaMapper::toDto);
    }

    /**
     * Delete the empresa by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Empresa : {}", id);
        empresaRepository.deleteById(id);
    }
}
