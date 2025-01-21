package app.abelsc.com.web.rest;

import static app.abelsc.com.domain.PerfilUsuarioAsserts.*;
import static app.abelsc.com.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.abelsc.com.IntegrationTest;
import app.abelsc.com.domain.PerfilUsuario;
import app.abelsc.com.repository.PerfilUsuarioRepository;
import app.abelsc.com.service.dto.PerfilUsuarioDTO;
import app.abelsc.com.service.mapper.PerfilUsuarioMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PerfilUsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PerfilUsuarioResourceIT {

    private static final String ENTITY_API_URL = "/api/perfil-usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PerfilUsuarioRepository perfilUsuarioRepository;

    @Autowired
    private PerfilUsuarioMapper perfilUsuarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPerfilUsuarioMockMvc;

    private PerfilUsuario perfilUsuario;

    private PerfilUsuario insertedPerfilUsuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerfilUsuario createEntity() {
        return new PerfilUsuario();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerfilUsuario createUpdatedEntity() {
        return new PerfilUsuario();
    }

    @BeforeEach
    public void initTest() {
        perfilUsuario = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPerfilUsuario != null) {
            perfilUsuarioRepository.delete(insertedPerfilUsuario);
            insertedPerfilUsuario = null;
        }
    }

    @Test
    @Transactional
    void createPerfilUsuario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PerfilUsuario
        PerfilUsuarioDTO perfilUsuarioDTO = perfilUsuarioMapper.toDto(perfilUsuario);
        var returnedPerfilUsuarioDTO = om.readValue(
            restPerfilUsuarioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(perfilUsuarioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PerfilUsuarioDTO.class
        );

        // Validate the PerfilUsuario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPerfilUsuario = perfilUsuarioMapper.toEntity(returnedPerfilUsuarioDTO);
        assertPerfilUsuarioUpdatableFieldsEquals(returnedPerfilUsuario, getPersistedPerfilUsuario(returnedPerfilUsuario));

        insertedPerfilUsuario = returnedPerfilUsuario;
    }

    @Test
    @Transactional
    void createPerfilUsuarioWithExistingId() throws Exception {
        // Create the PerfilUsuario with an existing ID
        perfilUsuario.setId(1L);
        PerfilUsuarioDTO perfilUsuarioDTO = perfilUsuarioMapper.toDto(perfilUsuario);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerfilUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(perfilUsuarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPerfilUsuarios() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        // Get all the perfilUsuarioList
        restPerfilUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perfilUsuario.getId().intValue())));
    }

    @Test
    @Transactional
    void getPerfilUsuario() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        // Get the perfilUsuario
        restPerfilUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, perfilUsuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(perfilUsuario.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPerfilUsuario() throws Exception {
        // Get the perfilUsuario
        restPerfilUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPerfilUsuario() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the perfilUsuario
        PerfilUsuario updatedPerfilUsuario = perfilUsuarioRepository.findById(perfilUsuario.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPerfilUsuario are not directly saved in db
        em.detach(updatedPerfilUsuario);
        PerfilUsuarioDTO perfilUsuarioDTO = perfilUsuarioMapper.toDto(updatedPerfilUsuario);

        restPerfilUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perfilUsuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(perfilUsuarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPerfilUsuarioToMatchAllProperties(updatedPerfilUsuario);
    }

    @Test
    @Transactional
    void putNonExistingPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // Create the PerfilUsuario
        PerfilUsuarioDTO perfilUsuarioDTO = perfilUsuarioMapper.toDto(perfilUsuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perfilUsuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(perfilUsuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // Create the PerfilUsuario
        PerfilUsuarioDTO perfilUsuarioDTO = perfilUsuarioMapper.toDto(perfilUsuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(perfilUsuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // Create the PerfilUsuario
        PerfilUsuarioDTO perfilUsuarioDTO = perfilUsuarioMapper.toDto(perfilUsuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(perfilUsuarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePerfilUsuarioWithPatch() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the perfilUsuario using partial update
        PerfilUsuario partialUpdatedPerfilUsuario = new PerfilUsuario();
        partialUpdatedPerfilUsuario.setId(perfilUsuario.getId());

        restPerfilUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfilUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPerfilUsuario))
            )
            .andExpect(status().isOk());

        // Validate the PerfilUsuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPerfilUsuarioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPerfilUsuario, perfilUsuario),
            getPersistedPerfilUsuario(perfilUsuario)
        );
    }

    @Test
    @Transactional
    void fullUpdatePerfilUsuarioWithPatch() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the perfilUsuario using partial update
        PerfilUsuario partialUpdatedPerfilUsuario = new PerfilUsuario();
        partialUpdatedPerfilUsuario.setId(perfilUsuario.getId());

        restPerfilUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfilUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPerfilUsuario))
            )
            .andExpect(status().isOk());

        // Validate the PerfilUsuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPerfilUsuarioUpdatableFieldsEquals(partialUpdatedPerfilUsuario, getPersistedPerfilUsuario(partialUpdatedPerfilUsuario));
    }

    @Test
    @Transactional
    void patchNonExistingPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // Create the PerfilUsuario
        PerfilUsuarioDTO perfilUsuarioDTO = perfilUsuarioMapper.toDto(perfilUsuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, perfilUsuarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(perfilUsuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // Create the PerfilUsuario
        PerfilUsuarioDTO perfilUsuarioDTO = perfilUsuarioMapper.toDto(perfilUsuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(perfilUsuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerfilUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        perfilUsuario.setId(longCount.incrementAndGet());

        // Create the PerfilUsuario
        PerfilUsuarioDTO perfilUsuarioDTO = perfilUsuarioMapper.toDto(perfilUsuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilUsuarioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(perfilUsuarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerfilUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerfilUsuario() throws Exception {
        // Initialize the database
        insertedPerfilUsuario = perfilUsuarioRepository.saveAndFlush(perfilUsuario);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the perfilUsuario
        restPerfilUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, perfilUsuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return perfilUsuarioRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PerfilUsuario getPersistedPerfilUsuario(PerfilUsuario perfilUsuario) {
        return perfilUsuarioRepository.findById(perfilUsuario.getId()).orElseThrow();
    }

    protected void assertPersistedPerfilUsuarioToMatchAllProperties(PerfilUsuario expectedPerfilUsuario) {
        assertPerfilUsuarioAllPropertiesEquals(expectedPerfilUsuario, getPersistedPerfilUsuario(expectedPerfilUsuario));
    }

    protected void assertPersistedPerfilUsuarioToMatchUpdatableProperties(PerfilUsuario expectedPerfilUsuario) {
        assertPerfilUsuarioAllUpdatablePropertiesEquals(expectedPerfilUsuario, getPersistedPerfilUsuario(expectedPerfilUsuario));
    }
}
