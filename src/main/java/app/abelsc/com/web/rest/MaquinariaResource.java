package app.abelsc.com.web.rest;

import app.abelsc.com.repository.MaquinariaRepository;
import app.abelsc.com.service.MaquinariaService;
import app.abelsc.com.service.dto.MaquinariaDTO;
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
 * REST controller for managing {@link app.abelsc.com.domain.Maquinaria}.
 */
@RestController
@RequestMapping("/api/maquinarias")
public class MaquinariaResource {

    private static final Logger LOG = LoggerFactory.getLogger(MaquinariaResource.class);

    private static final String ENTITY_NAME = "maquinaria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaquinariaService maquinariaService;

    private final MaquinariaRepository maquinariaRepository;

    public MaquinariaResource(MaquinariaService maquinariaService, MaquinariaRepository maquinariaRepository) {
        this.maquinariaService = maquinariaService;
        this.maquinariaRepository = maquinariaRepository;
    }

    /**
     * {@code POST  /maquinarias} : Create a new maquinaria.
     *
     * @param maquinariaDTO the maquinariaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maquinariaDTO, or with status {@code 400 (Bad Request)} if the maquinaria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaquinariaDTO> createMaquinaria(@RequestBody MaquinariaDTO maquinariaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Maquinaria : {}", maquinariaDTO);
        if (maquinariaDTO.getId() != null) {
            throw new BadRequestAlertException("A new maquinaria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        maquinariaDTO = maquinariaService.save(maquinariaDTO);
        return ResponseEntity.created(new URI("/api/maquinarias/" + maquinariaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, maquinariaDTO.getId().toString()))
            .body(maquinariaDTO);
    }

    /**
     * {@code PUT  /maquinarias/:id} : Updates an existing maquinaria.
     *
     * @param id the id of the maquinariaDTO to save.
     * @param maquinariaDTO the maquinariaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maquinariaDTO,
     * or with status {@code 400 (Bad Request)} if the maquinariaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maquinariaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaquinariaDTO> updateMaquinaria(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaquinariaDTO maquinariaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Maquinaria : {}, {}", id, maquinariaDTO);
        if (maquinariaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maquinariaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maquinariaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        maquinariaDTO = maquinariaService.update(maquinariaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maquinariaDTO.getId().toString()))
            .body(maquinariaDTO);
    }

    /**
     * {@code PATCH  /maquinarias/:id} : Partial updates given fields of an existing maquinaria, field will ignore if it is null
     *
     * @param id the id of the maquinariaDTO to save.
     * @param maquinariaDTO the maquinariaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maquinariaDTO,
     * or with status {@code 400 (Bad Request)} if the maquinariaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the maquinariaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the maquinariaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaquinariaDTO> partialUpdateMaquinaria(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaquinariaDTO maquinariaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Maquinaria partially : {}, {}", id, maquinariaDTO);
        if (maquinariaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maquinariaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maquinariaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaquinariaDTO> result = maquinariaService.partialUpdate(maquinariaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maquinariaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /maquinarias} : get all the maquinarias.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maquinarias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaquinariaDTO>> getAllMaquinarias(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Maquinarias");
        Page<MaquinariaDTO> page = maquinariaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /maquinarias/:id} : get the "id" maquinaria.
     *
     * @param id the id of the maquinariaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maquinariaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaquinariaDTO> getMaquinaria(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Maquinaria : {}", id);
        Optional<MaquinariaDTO> maquinariaDTO = maquinariaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maquinariaDTO);
    }

    /**
     * {@code DELETE  /maquinarias/:id} : delete the "id" maquinaria.
     *
     * @param id the id of the maquinariaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaquinaria(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Maquinaria : {}", id);
        maquinariaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
