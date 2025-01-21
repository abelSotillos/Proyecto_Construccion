package app.abelsc.com.web.rest;

import app.abelsc.com.repository.PerfilUsuarioRepository;
import app.abelsc.com.service.PerfilUsuarioService;
import app.abelsc.com.service.dto.PerfilUsuarioDTO;
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
 * REST controller for managing {@link app.abelsc.com.domain.PerfilUsuario}.
 */
@RestController
@RequestMapping("/api/perfil-usuarios")
public class PerfilUsuarioResource {

    private static final Logger LOG = LoggerFactory.getLogger(PerfilUsuarioResource.class);

    private static final String ENTITY_NAME = "perfilUsuario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerfilUsuarioService perfilUsuarioService;

    private final PerfilUsuarioRepository perfilUsuarioRepository;

    public PerfilUsuarioResource(PerfilUsuarioService perfilUsuarioService, PerfilUsuarioRepository perfilUsuarioRepository) {
        this.perfilUsuarioService = perfilUsuarioService;
        this.perfilUsuarioRepository = perfilUsuarioRepository;
    }

    /**
     * {@code POST  /perfil-usuarios} : Create a new perfilUsuario.
     *
     * @param perfilUsuarioDTO the perfilUsuarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new perfilUsuarioDTO, or with status {@code 400 (Bad Request)} if the perfilUsuario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PerfilUsuarioDTO> createPerfilUsuario(@RequestBody PerfilUsuarioDTO perfilUsuarioDTO) throws URISyntaxException {
        LOG.debug("REST request to save PerfilUsuario : {}", perfilUsuarioDTO);
        if (perfilUsuarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new perfilUsuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        perfilUsuarioDTO = perfilUsuarioService.save(perfilUsuarioDTO);
        return ResponseEntity.created(new URI("/api/perfil-usuarios/" + perfilUsuarioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, perfilUsuarioDTO.getId().toString()))
            .body(perfilUsuarioDTO);
    }

    /**
     * {@code PUT  /perfil-usuarios/:id} : Updates an existing perfilUsuario.
     *
     * @param id the id of the perfilUsuarioDTO to save.
     * @param perfilUsuarioDTO the perfilUsuarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfilUsuarioDTO,
     * or with status {@code 400 (Bad Request)} if the perfilUsuarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the perfilUsuarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PerfilUsuarioDTO> updatePerfilUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PerfilUsuarioDTO perfilUsuarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PerfilUsuario : {}, {}", id, perfilUsuarioDTO);
        if (perfilUsuarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfilUsuarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        perfilUsuarioDTO = perfilUsuarioService.update(perfilUsuarioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, perfilUsuarioDTO.getId().toString()))
            .body(perfilUsuarioDTO);
    }

    /**
     * {@code PATCH  /perfil-usuarios/:id} : Partial updates given fields of an existing perfilUsuario, field will ignore if it is null
     *
     * @param id the id of the perfilUsuarioDTO to save.
     * @param perfilUsuarioDTO the perfilUsuarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfilUsuarioDTO,
     * or with status {@code 400 (Bad Request)} if the perfilUsuarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the perfilUsuarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the perfilUsuarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PerfilUsuarioDTO> partialUpdatePerfilUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PerfilUsuarioDTO perfilUsuarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PerfilUsuario partially : {}, {}", id, perfilUsuarioDTO);
        if (perfilUsuarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfilUsuarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PerfilUsuarioDTO> result = perfilUsuarioService.partialUpdate(perfilUsuarioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, perfilUsuarioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /perfil-usuarios} : get all the perfilUsuarios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of perfilUsuarios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PerfilUsuarioDTO>> getAllPerfilUsuarios(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of PerfilUsuarios");
        Page<PerfilUsuarioDTO> page = perfilUsuarioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /perfil-usuarios/:id} : get the "id" perfilUsuario.
     *
     * @param id the id of the perfilUsuarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the perfilUsuarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PerfilUsuarioDTO> getPerfilUsuario(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PerfilUsuario : {}", id);
        Optional<PerfilUsuarioDTO> perfilUsuarioDTO = perfilUsuarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(perfilUsuarioDTO);
    }

    /**
     * {@code DELETE  /perfil-usuarios/:id} : delete the "id" perfilUsuario.
     *
     * @param id the id of the perfilUsuarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerfilUsuario(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PerfilUsuario : {}", id);
        perfilUsuarioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
