package app.abelsc.com.web.rest;

import static app.abelsc.com.domain.EmpleadoObraAsserts.*;
import static app.abelsc.com.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.abelsc.com.IntegrationTest;
import app.abelsc.com.domain.EmpleadoObra;
import app.abelsc.com.repository.EmpleadoObraRepository;
import app.abelsc.com.service.dto.EmpleadoObraDTO;
import app.abelsc.com.service.mapper.EmpleadoObraMapper;
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
 * Integration tests for the {@link EmpleadoObraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmpleadoObraResourceIT {

    private static final Long DEFAULT_HORAS = 1L;
    private static final Long UPDATED_HORAS = 2L;

    private static final String ENTITY_API_URL = "/api/empleado-obras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmpleadoObraRepository empleadoObraRepository;

    @Autowired
    private EmpleadoObraMapper empleadoObraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpleadoObraMockMvc;

    private EmpleadoObra empleadoObra;

    private EmpleadoObra insertedEmpleadoObra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmpleadoObra createEntity() {
        return new EmpleadoObra().horas(DEFAULT_HORAS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmpleadoObra createUpdatedEntity() {
        return new EmpleadoObra().horas(UPDATED_HORAS);
    }

    @BeforeEach
    public void initTest() {
        empleadoObra = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmpleadoObra != null) {
            empleadoObraRepository.delete(insertedEmpleadoObra);
            insertedEmpleadoObra = null;
        }
    }

    @Test
    @Transactional
    void createEmpleadoObra() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmpleadoObra
        EmpleadoObraDTO empleadoObraDTO = empleadoObraMapper.toDto(empleadoObra);
        var returnedEmpleadoObraDTO = om.readValue(
            restEmpleadoObraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoObraDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmpleadoObraDTO.class
        );

        // Validate the EmpleadoObra in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmpleadoObra = empleadoObraMapper.toEntity(returnedEmpleadoObraDTO);
        assertEmpleadoObraUpdatableFieldsEquals(returnedEmpleadoObra, getPersistedEmpleadoObra(returnedEmpleadoObra));

        insertedEmpleadoObra = returnedEmpleadoObra;
    }

    @Test
    @Transactional
    void createEmpleadoObraWithExistingId() throws Exception {
        // Create the EmpleadoObra with an existing ID
        empleadoObra.setId(1L);
        EmpleadoObraDTO empleadoObraDTO = empleadoObraMapper.toDto(empleadoObra);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpleadoObraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoObraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmpleadoObra in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmpleadoObras() throws Exception {
        // Initialize the database
        insertedEmpleadoObra = empleadoObraRepository.saveAndFlush(empleadoObra);

        // Get all the empleadoObraList
        restEmpleadoObraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empleadoObra.getId().intValue())))
            .andExpect(jsonPath("$.[*].horas").value(hasItem(DEFAULT_HORAS.intValue())));
    }

    @Test
    @Transactional
    void getEmpleadoObra() throws Exception {
        // Initialize the database
        insertedEmpleadoObra = empleadoObraRepository.saveAndFlush(empleadoObra);

        // Get the empleadoObra
        restEmpleadoObraMockMvc
            .perform(get(ENTITY_API_URL_ID, empleadoObra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empleadoObra.getId().intValue()))
            .andExpect(jsonPath("$.horas").value(DEFAULT_HORAS.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEmpleadoObra() throws Exception {
        // Get the empleadoObra
        restEmpleadoObraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmpleadoObra() throws Exception {
        // Initialize the database
        insertedEmpleadoObra = empleadoObraRepository.saveAndFlush(empleadoObra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleadoObra
        EmpleadoObra updatedEmpleadoObra = empleadoObraRepository.findById(empleadoObra.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmpleadoObra are not directly saved in db
        em.detach(updatedEmpleadoObra);
        updatedEmpleadoObra.horas(UPDATED_HORAS);
        EmpleadoObraDTO empleadoObraDTO = empleadoObraMapper.toDto(updatedEmpleadoObra);

        restEmpleadoObraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoObraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoObraDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmpleadoObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmpleadoObraToMatchAllProperties(updatedEmpleadoObra);
    }

    @Test
    @Transactional
    void putNonExistingEmpleadoObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoObra.setId(longCount.incrementAndGet());

        // Create the EmpleadoObra
        EmpleadoObraDTO empleadoObraDTO = empleadoObraMapper.toDto(empleadoObra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoObraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoObraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmpleadoObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpleadoObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoObra.setId(longCount.incrementAndGet());

        // Create the EmpleadoObra
        EmpleadoObraDTO empleadoObraDTO = empleadoObraMapper.toDto(empleadoObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoObraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmpleadoObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpleadoObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoObra.setId(longCount.incrementAndGet());

        // Create the EmpleadoObra
        EmpleadoObraDTO empleadoObraDTO = empleadoObraMapper.toDto(empleadoObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoObraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoObraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmpleadoObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpleadoObraWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleadoObra = empleadoObraRepository.saveAndFlush(empleadoObra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleadoObra using partial update
        EmpleadoObra partialUpdatedEmpleadoObra = new EmpleadoObra();
        partialUpdatedEmpleadoObra.setId(empleadoObra.getId());

        partialUpdatedEmpleadoObra.horas(UPDATED_HORAS);

        restEmpleadoObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleadoObra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpleadoObra))
            )
            .andExpect(status().isOk());

        // Validate the EmpleadoObra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadoObraUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmpleadoObra, empleadoObra),
            getPersistedEmpleadoObra(empleadoObra)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmpleadoObraWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleadoObra = empleadoObraRepository.saveAndFlush(empleadoObra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleadoObra using partial update
        EmpleadoObra partialUpdatedEmpleadoObra = new EmpleadoObra();
        partialUpdatedEmpleadoObra.setId(empleadoObra.getId());

        partialUpdatedEmpleadoObra.horas(UPDATED_HORAS);

        restEmpleadoObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleadoObra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpleadoObra))
            )
            .andExpect(status().isOk());

        // Validate the EmpleadoObra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadoObraUpdatableFieldsEquals(partialUpdatedEmpleadoObra, getPersistedEmpleadoObra(partialUpdatedEmpleadoObra));
    }

    @Test
    @Transactional
    void patchNonExistingEmpleadoObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoObra.setId(longCount.incrementAndGet());

        // Create the EmpleadoObra
        EmpleadoObraDTO empleadoObraDTO = empleadoObraMapper.toDto(empleadoObra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empleadoObraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empleadoObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmpleadoObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpleadoObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoObra.setId(longCount.incrementAndGet());

        // Create the EmpleadoObra
        EmpleadoObraDTO empleadoObraDTO = empleadoObraMapper.toDto(empleadoObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empleadoObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmpleadoObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpleadoObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadoObra.setId(longCount.incrementAndGet());

        // Create the EmpleadoObra
        EmpleadoObraDTO empleadoObraDTO = empleadoObraMapper.toDto(empleadoObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoObraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(empleadoObraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmpleadoObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpleadoObra() throws Exception {
        // Initialize the database
        insertedEmpleadoObra = empleadoObraRepository.saveAndFlush(empleadoObra);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the empleadoObra
        restEmpleadoObraMockMvc
            .perform(delete(ENTITY_API_URL_ID, empleadoObra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return empleadoObraRepository.count();
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

    protected EmpleadoObra getPersistedEmpleadoObra(EmpleadoObra empleadoObra) {
        return empleadoObraRepository.findById(empleadoObra.getId()).orElseThrow();
    }

    protected void assertPersistedEmpleadoObraToMatchAllProperties(EmpleadoObra expectedEmpleadoObra) {
        assertEmpleadoObraAllPropertiesEquals(expectedEmpleadoObra, getPersistedEmpleadoObra(expectedEmpleadoObra));
    }

    protected void assertPersistedEmpleadoObraToMatchUpdatableProperties(EmpleadoObra expectedEmpleadoObra) {
        assertEmpleadoObraAllUpdatablePropertiesEquals(expectedEmpleadoObra, getPersistedEmpleadoObra(expectedEmpleadoObra));
    }
}
