package app.abelsc.com.web.rest;

import app.abelsc.com.repository.MaterialObraRepository;
import app.abelsc.com.service.MaterialObraService;
import app.abelsc.com.service.dto.MaterialObraDTO;
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
 * REST controller for managing {@link app.abelsc.com.domain.MaterialObra}.
 */
@RestController
@RequestMapping("/api/material-obras")
public class MaterialObraResource {

    private static final Logger LOG = LoggerFactory.getLogger(MaterialObraResource.class);

    private static final String ENTITY_NAME = "materialObra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialObraService materialObraService;

    private final MaterialObraRepository materialObraRepository;

    public MaterialObraResource(MaterialObraService materialObraService, MaterialObraRepository materialObraRepository) {
        this.materialObraService = materialObraService;
        this.materialObraRepository = materialObraRepository;
    }

    /**
     * {@code POST  /material-obras} : Create a new materialObra.
     *
     * @param materialObraDTO the materialObraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialObraDTO, or with status {@code 400 (Bad Request)} if the materialObra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaterialObraDTO> createMaterialObra(@RequestBody MaterialObraDTO materialObraDTO) throws URISyntaxException {
        LOG.debug("REST request to save MaterialObra : {}", materialObraDTO);
        if (materialObraDTO.getId() != null) {
            throw new BadRequestAlertException("A new materialObra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        materialObraDTO = materialObraService.save(materialObraDTO);
        return ResponseEntity.created(new URI("/api/material-obras/" + materialObraDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, materialObraDTO.getId().toString()))
            .body(materialObraDTO);
    }

    /**
     * {@code PUT  /material-obras/:id} : Updates an existing materialObra.
     *
     * @param id the id of the materialObraDTO to save.
     * @param materialObraDTO the materialObraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialObraDTO,
     * or with status {@code 400 (Bad Request)} if the materialObraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialObraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaterialObraDTO> updateMaterialObra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialObraDTO materialObraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MaterialObra : {}, {}", id, materialObraDTO);
        if (materialObraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialObraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialObraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        materialObraDTO = materialObraService.update(materialObraDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materialObraDTO.getId().toString()))
            .body(materialObraDTO);
    }

    /**
     * {@code PATCH  /material-obras/:id} : Partial updates given fields of an existing materialObra, field will ignore if it is null
     *
     * @param id the id of the materialObraDTO to save.
     * @param materialObraDTO the materialObraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialObraDTO,
     * or with status {@code 400 (Bad Request)} if the materialObraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the materialObraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the materialObraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaterialObraDTO> partialUpdateMaterialObra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaterialObraDTO materialObraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MaterialObra partially : {}, {}", id, materialObraDTO);
        if (materialObraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, materialObraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialObraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaterialObraDTO> result = materialObraService.partialUpdate(materialObraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materialObraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /material-obras} : get all the materialObras.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialObras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaterialObraDTO>> getAllMaterialObras(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of MaterialObras");
        Page<MaterialObraDTO> page = materialObraService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /material-obras/:id} : get the "id" materialObra.
     *
     * @param id the id of the materialObraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialObraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaterialObraDTO> getMaterialObra(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MaterialObra : {}", id);
        Optional<MaterialObraDTO> materialObraDTO = materialObraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialObraDTO);
    }

    /**
     * {@code DELETE  /material-obras/:id} : delete the "id" materialObra.
     *
     * @param id the id of the materialObraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterialObra(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MaterialObra : {}", id);
        materialObraService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
