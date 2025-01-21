package app.abelsc.com.web.rest;

import app.abelsc.com.repository.MaquinariaObraRepository;
import app.abelsc.com.service.MaquinariaObraService;
import app.abelsc.com.service.dto.MaquinariaObraDTO;
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
 * REST controller for managing {@link app.abelsc.com.domain.MaquinariaObra}.
 */
@RestController
@RequestMapping("/api/maquinaria-obras")
public class MaquinariaObraResource {

    private static final Logger LOG = LoggerFactory.getLogger(MaquinariaObraResource.class);

    private static final String ENTITY_NAME = "maquinariaObra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaquinariaObraService maquinariaObraService;

    private final MaquinariaObraRepository maquinariaObraRepository;

    public MaquinariaObraResource(MaquinariaObraService maquinariaObraService, MaquinariaObraRepository maquinariaObraRepository) {
        this.maquinariaObraService = maquinariaObraService;
        this.maquinariaObraRepository = maquinariaObraRepository;
    }

    /**
     * {@code POST  /maquinaria-obras} : Create a new maquinariaObra.
     *
     * @param maquinariaObraDTO the maquinariaObraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maquinariaObraDTO, or with status {@code 400 (Bad Request)} if the maquinariaObra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaquinariaObraDTO> createMaquinariaObra(@RequestBody MaquinariaObraDTO maquinariaObraDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MaquinariaObra : {}", maquinariaObraDTO);
        if (maquinariaObraDTO.getId() != null) {
            throw new BadRequestAlertException("A new maquinariaObra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        maquinariaObraDTO = maquinariaObraService.save(maquinariaObraDTO);
        return ResponseEntity.created(new URI("/api/maquinaria-obras/" + maquinariaObraDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, maquinariaObraDTO.getId().toString()))
            .body(maquinariaObraDTO);
    }

    /**
     * {@code PUT  /maquinaria-obras/:id} : Updates an existing maquinariaObra.
     *
     * @param id the id of the maquinariaObraDTO to save.
     * @param maquinariaObraDTO the maquinariaObraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maquinariaObraDTO,
     * or with status {@code 400 (Bad Request)} if the maquinariaObraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maquinariaObraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaquinariaObraDTO> updateMaquinariaObra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaquinariaObraDTO maquinariaObraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MaquinariaObra : {}, {}", id, maquinariaObraDTO);
        if (maquinariaObraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maquinariaObraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maquinariaObraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        maquinariaObraDTO = maquinariaObraService.update(maquinariaObraDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maquinariaObraDTO.getId().toString()))
            .body(maquinariaObraDTO);
    }

    /**
     * {@code PATCH  /maquinaria-obras/:id} : Partial updates given fields of an existing maquinariaObra, field will ignore if it is null
     *
     * @param id the id of the maquinariaObraDTO to save.
     * @param maquinariaObraDTO the maquinariaObraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maquinariaObraDTO,
     * or with status {@code 400 (Bad Request)} if the maquinariaObraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the maquinariaObraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the maquinariaObraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaquinariaObraDTO> partialUpdateMaquinariaObra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaquinariaObraDTO maquinariaObraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MaquinariaObra partially : {}, {}", id, maquinariaObraDTO);
        if (maquinariaObraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maquinariaObraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maquinariaObraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaquinariaObraDTO> result = maquinariaObraService.partialUpdate(maquinariaObraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maquinariaObraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /maquinaria-obras} : get all the maquinariaObras.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maquinariaObras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaquinariaObraDTO>> getAllMaquinariaObras(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of MaquinariaObras");
        Page<MaquinariaObraDTO> page = maquinariaObraService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /maquinaria-obras/:id} : get the "id" maquinariaObra.
     *
     * @param id the id of the maquinariaObraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maquinariaObraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaquinariaObraDTO> getMaquinariaObra(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MaquinariaObra : {}", id);
        Optional<MaquinariaObraDTO> maquinariaObraDTO = maquinariaObraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maquinariaObraDTO);
    }

    /**
     * {@code DELETE  /maquinaria-obras/:id} : delete the "id" maquinariaObra.
     *
     * @param id the id of the maquinariaObraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaquinariaObra(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MaquinariaObra : {}", id);
        maquinariaObraService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
