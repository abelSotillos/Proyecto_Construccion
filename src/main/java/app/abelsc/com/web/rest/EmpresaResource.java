package app.abelsc.com.web.rest;

import app.abelsc.com.repository.EmpresaRepository;
import app.abelsc.com.service.EmpresaService;
import app.abelsc.com.service.dto.EmpresaDTO;
import app.abelsc.com.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link app.abelsc.com.domain.Empresa}.
 */
@RestController
@RequestMapping("/api/empresas")
public class EmpresaResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmpresaResource.class);

    private static final String ENTITY_NAME = "empresa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpresaService empresaService;

    private final EmpresaRepository empresaRepository;

    public EmpresaResource(EmpresaService empresaService, EmpresaRepository empresaRepository) {
        this.empresaService = empresaService;
        this.empresaRepository = empresaRepository;
    }

    /**
     * {@code POST  /empresas} : Create a new empresa.
     *
     * @param empresaDTO the empresaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empresaDTO, or with status {@code 400 (Bad Request)} if the empresa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmpresaDTO> createEmpresa(@RequestBody EmpresaDTO empresaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Empresa : {}", empresaDTO);
        if (empresaDTO.getId() != null) {
            throw new BadRequestAlertException("A new empresa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        empresaDTO = empresaService.save(empresaDTO);
        return ResponseEntity.created(new URI("/api/empresas/" + empresaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, empresaDTO.getId().toString()))
            .body(empresaDTO);
    }

    /**
     * {@code PUT  /empresas/:id} : Updates an existing empresa.
     *
     * @param id the id of the empresaDTO to save.
     * @param empresaDTO the empresaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empresaDTO,
     * or with status {@code 400 (Bad Request)} if the empresaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empresaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> updateEmpresa(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmpresaDTO empresaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Empresa : {}, {}", id, empresaDTO);
        if (empresaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empresaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empresaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        empresaDTO = empresaService.update(empresaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empresaDTO.getId().toString()))
            .body(empresaDTO);
    }

    /**
     * {@code PATCH  /empresas/:id} : Partial updates given fields of an existing empresa, field will ignore if it is null
     *
     * @param id the id of the empresaDTO to save.
     * @param empresaDTO the empresaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empresaDTO,
     * or with status {@code 400 (Bad Request)} if the empresaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the empresaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the empresaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmpresaDTO> partialUpdateEmpresa(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmpresaDTO empresaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Empresa partially : {}, {}", id, empresaDTO);
        if (empresaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empresaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empresaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmpresaDTO> result = empresaService.partialUpdate(empresaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empresaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /empresas} : get all the empresas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of empresas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmpresaDTO>> getAllEmpresas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Empresas");
        Page<EmpresaDTO> page = empresaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /empresas/:id} : get the "id" empresa.
     *
     * @param id the id of the empresaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empresaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> getEmpresa(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Empresa : {}", id);
        Optional<EmpresaDTO> empresaDTO = empresaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(empresaDTO);
    }

    /**
     * {@code DELETE  /empresas/:id} : delete the "id" empresa.
     *
     * @param id the id of the empresaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Empresa : {}", id);
        empresaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
