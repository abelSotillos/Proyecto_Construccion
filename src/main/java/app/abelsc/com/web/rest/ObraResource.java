package app.abelsc.com.web.rest;

import app.abelsc.com.repository.ObraRepository;
import app.abelsc.com.service.ObraService;
import app.abelsc.com.service.dto.ObraDTO;
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
 * REST controller for managing {@link app.abelsc.com.domain.Obra}.
 */
@RestController
@RequestMapping("/api/obras")
public class ObraResource {

    private static final Logger LOG = LoggerFactory.getLogger(ObraResource.class);

    private static final String ENTITY_NAME = "obra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObraService obraService;

    private final ObraRepository obraRepository;

    public ObraResource(ObraService obraService, ObraRepository obraRepository) {
        this.obraService = obraService;
        this.obraRepository = obraRepository;
    }

    /**
     * {@code POST  /obras} : Create a new obra.
     *
     * @param obraDTO the obraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new obraDTO, or with status {@code 400 (Bad Request)} if the obra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ObraDTO> createObra(@RequestBody ObraDTO obraDTO) throws URISyntaxException {
        LOG.debug("REST request to save Obra : {}", obraDTO);
        if (obraDTO.getId() != null) {
            throw new BadRequestAlertException("A new obra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        obraDTO = obraService.save(obraDTO);
        return ResponseEntity.created(new URI("/api/obras/" + obraDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, obraDTO.getId().toString()))
            .body(obraDTO);
    }

    /**
     * {@code PUT  /obras/:id} : Updates an existing obra.
     *
     * @param id the id of the obraDTO to save.
     * @param obraDTO the obraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated obraDTO,
     * or with status {@code 400 (Bad Request)} if the obraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the obraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ObraDTO> updateObra(@PathVariable(value = "id", required = false) final Long id, @RequestBody ObraDTO obraDTO)
        throws URISyntaxException {
        LOG.debug("REST request to update Obra : {}, {}", id, obraDTO);
        if (obraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, obraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!obraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        obraDTO = obraService.update(obraDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, obraDTO.getId().toString()))
            .body(obraDTO);
    }

    /**
     * {@code PATCH  /obras/:id} : Partial updates given fields of an existing obra, field will ignore if it is null
     *
     * @param id the id of the obraDTO to save.
     * @param obraDTO the obraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated obraDTO,
     * or with status {@code 400 (Bad Request)} if the obraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the obraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the obraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ObraDTO> partialUpdateObra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ObraDTO obraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Obra partially : {}, {}", id, obraDTO);
        if (obraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, obraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!obraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ObraDTO> result = obraService.partialUpdate(obraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, obraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /obras} : get all the obras.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of obras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ObraDTO>> getAllObras(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Obras");
        Page<ObraDTO> page = obraService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /obras/:id} : get the "id" obra.
     *
     * @param id the id of the obraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the obraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ObraDTO> getObra(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Obra : {}", id);
        Optional<ObraDTO> obraDTO = obraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(obraDTO);
    }

    /**
     * {@code DELETE  /obras/:id} : delete the "id" obra.
     *
     * @param id the id of the obraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObra(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Obra : {}", id);
        obraService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
