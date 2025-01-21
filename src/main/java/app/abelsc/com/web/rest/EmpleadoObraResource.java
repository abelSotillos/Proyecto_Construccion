package app.abelsc.com.web.rest;

import app.abelsc.com.repository.EmpleadoObraRepository;
import app.abelsc.com.service.EmpleadoObraService;
import app.abelsc.com.service.dto.EmpleadoObraDTO;
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
 * REST controller for managing {@link app.abelsc.com.domain.EmpleadoObra}.
 */
@RestController
@RequestMapping("/api/empleado-obras")
public class EmpleadoObraResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmpleadoObraResource.class);

    private static final String ENTITY_NAME = "empleadoObra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpleadoObraService empleadoObraService;

    private final EmpleadoObraRepository empleadoObraRepository;

    public EmpleadoObraResource(EmpleadoObraService empleadoObraService, EmpleadoObraRepository empleadoObraRepository) {
        this.empleadoObraService = empleadoObraService;
        this.empleadoObraRepository = empleadoObraRepository;
    }

    /**
     * {@code POST  /empleado-obras} : Create a new empleadoObra.
     *
     * @param empleadoObraDTO the empleadoObraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empleadoObraDTO, or with status {@code 400 (Bad Request)} if the empleadoObra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmpleadoObraDTO> createEmpleadoObra(@RequestBody EmpleadoObraDTO empleadoObraDTO) throws URISyntaxException {
        LOG.debug("REST request to save EmpleadoObra : {}", empleadoObraDTO);
        if (empleadoObraDTO.getId() != null) {
            throw new BadRequestAlertException("A new empleadoObra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        empleadoObraDTO = empleadoObraService.save(empleadoObraDTO);
        return ResponseEntity.created(new URI("/api/empleado-obras/" + empleadoObraDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, empleadoObraDTO.getId().toString()))
            .body(empleadoObraDTO);
    }

    /**
     * {@code PUT  /empleado-obras/:id} : Updates an existing empleadoObra.
     *
     * @param id the id of the empleadoObraDTO to save.
     * @param empleadoObraDTO the empleadoObraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleadoObraDTO,
     * or with status {@code 400 (Bad Request)} if the empleadoObraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empleadoObraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoObraDTO> updateEmpleadoObra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmpleadoObraDTO empleadoObraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmpleadoObra : {}, {}", id, empleadoObraDTO);
        if (empleadoObraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleadoObraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empleadoObraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        empleadoObraDTO = empleadoObraService.update(empleadoObraDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empleadoObraDTO.getId().toString()))
            .body(empleadoObraDTO);
    }

    /**
     * {@code PATCH  /empleado-obras/:id} : Partial updates given fields of an existing empleadoObra, field will ignore if it is null
     *
     * @param id the id of the empleadoObraDTO to save.
     * @param empleadoObraDTO the empleadoObraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleadoObraDTO,
     * or with status {@code 400 (Bad Request)} if the empleadoObraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the empleadoObraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the empleadoObraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmpleadoObraDTO> partialUpdateEmpleadoObra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmpleadoObraDTO empleadoObraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmpleadoObra partially : {}, {}", id, empleadoObraDTO);
        if (empleadoObraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleadoObraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empleadoObraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmpleadoObraDTO> result = empleadoObraService.partialUpdate(empleadoObraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empleadoObraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /empleado-obras} : get all the empleadoObras.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of empleadoObras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmpleadoObraDTO>> getAllEmpleadoObras(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of EmpleadoObras");
        Page<EmpleadoObraDTO> page = empleadoObraService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /empleado-obras/:id} : get the "id" empleadoObra.
     *
     * @param id the id of the empleadoObraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empleadoObraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoObraDTO> getEmpleadoObra(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmpleadoObra : {}", id);
        Optional<EmpleadoObraDTO> empleadoObraDTO = empleadoObraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(empleadoObraDTO);
    }

    /**
     * {@code DELETE  /empleado-obras/:id} : delete the "id" empleadoObra.
     *
     * @param id the id of the empleadoObraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleadoObra(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmpleadoObra : {}", id);
        empleadoObraService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
