package app.abelsc.com.web.rest;

import static app.abelsc.com.domain.MaterialObraAsserts.*;
import static app.abelsc.com.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.abelsc.com.IntegrationTest;
import app.abelsc.com.domain.MaterialObra;
import app.abelsc.com.repository.MaterialObraRepository;
import app.abelsc.com.service.dto.MaterialObraDTO;
import app.abelsc.com.service.mapper.MaterialObraMapper;
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
 * Integration tests for the {@link MaterialObraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialObraResourceIT {

    private static final Long DEFAULT_CANTIDAD = 1L;
    private static final Long UPDATED_CANTIDAD = 2L;

    private static final String ENTITY_API_URL = "/api/material-obras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterialObraRepository materialObraRepository;

    @Autowired
    private MaterialObraMapper materialObraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialObraMockMvc;

    private MaterialObra materialObra;

    private MaterialObra insertedMaterialObra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialObra createEntity() {
        return new MaterialObra().cantidad(DEFAULT_CANTIDAD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialObra createUpdatedEntity() {
        return new MaterialObra().cantidad(UPDATED_CANTIDAD);
    }

    @BeforeEach
    public void initTest() {
        materialObra = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMaterialObra != null) {
            materialObraRepository.delete(insertedMaterialObra);
            insertedMaterialObra = null;
        }
    }

    @Test
    @Transactional
    void createMaterialObra() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MaterialObra
        MaterialObraDTO materialObraDTO = materialObraMapper.toDto(materialObra);
        var returnedMaterialObraDTO = om.readValue(
            restMaterialObraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialObraDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterialObraDTO.class
        );

        // Validate the MaterialObra in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaterialObra = materialObraMapper.toEntity(returnedMaterialObraDTO);
        assertMaterialObraUpdatableFieldsEquals(returnedMaterialObra, getPersistedMaterialObra(returnedMaterialObra));

        insertedMaterialObra = returnedMaterialObra;
    }

    @Test
    @Transactional
    void createMaterialObraWithExistingId() throws Exception {
        // Create the MaterialObra with an existing ID
        materialObra.setId(1L);
        MaterialObraDTO materialObraDTO = materialObraMapper.toDto(materialObra);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialObraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialObraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialObra in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterialObras() throws Exception {
        // Initialize the database
        insertedMaterialObra = materialObraRepository.saveAndFlush(materialObra);

        // Get all the materialObraList
        restMaterialObraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialObra.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())));
    }

    @Test
    @Transactional
    void getMaterialObra() throws Exception {
        // Initialize the database
        insertedMaterialObra = materialObraRepository.saveAndFlush(materialObra);

        // Get the materialObra
        restMaterialObraMockMvc
            .perform(get(ENTITY_API_URL_ID, materialObra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materialObra.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMaterialObra() throws Exception {
        // Get the materialObra
        restMaterialObraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterialObra() throws Exception {
        // Initialize the database
        insertedMaterialObra = materialObraRepository.saveAndFlush(materialObra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialObra
        MaterialObra updatedMaterialObra = materialObraRepository.findById(materialObra.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaterialObra are not directly saved in db
        em.detach(updatedMaterialObra);
        updatedMaterialObra.cantidad(UPDATED_CANTIDAD);
        MaterialObraDTO materialObraDTO = materialObraMapper.toDto(updatedMaterialObra);

        restMaterialObraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialObraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialObraDTO))
            )
            .andExpect(status().isOk());

        // Validate the MaterialObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterialObraToMatchAllProperties(updatedMaterialObra);
    }

    @Test
    @Transactional
    void putNonExistingMaterialObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialObra.setId(longCount.incrementAndGet());

        // Create the MaterialObra
        MaterialObraDTO materialObraDTO = materialObraMapper.toDto(materialObra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialObraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialObraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterialObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialObra.setId(longCount.incrementAndGet());

        // Create the MaterialObra
        MaterialObraDTO materialObraDTO = materialObraMapper.toDto(materialObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialObraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterialObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialObra.setId(longCount.incrementAndGet());

        // Create the MaterialObra
        MaterialObraDTO materialObraDTO = materialObraMapper.toDto(materialObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialObraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialObraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialObraWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialObra = materialObraRepository.saveAndFlush(materialObra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialObra using partial update
        MaterialObra partialUpdatedMaterialObra = new MaterialObra();
        partialUpdatedMaterialObra.setId(materialObra.getId());

        restMaterialObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialObra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialObra))
            )
            .andExpect(status().isOk());

        // Validate the MaterialObra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialObraUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMaterialObra, materialObra),
            getPersistedMaterialObra(materialObra)
        );
    }

    @Test
    @Transactional
    void fullUpdateMaterialObraWithPatch() throws Exception {
        // Initialize the database
        insertedMaterialObra = materialObraRepository.saveAndFlush(materialObra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the materialObra using partial update
        MaterialObra partialUpdatedMaterialObra = new MaterialObra();
        partialUpdatedMaterialObra.setId(materialObra.getId());

        partialUpdatedMaterialObra.cantidad(UPDATED_CANTIDAD);

        restMaterialObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterialObra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterialObra))
            )
            .andExpect(status().isOk());

        // Validate the MaterialObra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialObraUpdatableFieldsEquals(partialUpdatedMaterialObra, getPersistedMaterialObra(partialUpdatedMaterialObra));
    }

    @Test
    @Transactional
    void patchNonExistingMaterialObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialObra.setId(longCount.incrementAndGet());

        // Create the MaterialObra
        MaterialObraDTO materialObraDTO = materialObraMapper.toDto(materialObra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialObraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterialObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialObra.setId(longCount.incrementAndGet());

        // Create the MaterialObra
        MaterialObraDTO materialObraDTO = materialObraMapper.toDto(materialObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaterialObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterialObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        materialObra.setId(longCount.incrementAndGet());

        // Create the MaterialObra
        MaterialObraDTO materialObraDTO = materialObraMapper.toDto(materialObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialObraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materialObraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaterialObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterialObra() throws Exception {
        // Initialize the database
        insertedMaterialObra = materialObraRepository.saveAndFlush(materialObra);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the materialObra
        restMaterialObraMockMvc
            .perform(delete(ENTITY_API_URL_ID, materialObra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return materialObraRepository.count();
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

    protected MaterialObra getPersistedMaterialObra(MaterialObra materialObra) {
        return materialObraRepository.findById(materialObra.getId()).orElseThrow();
    }

    protected void assertPersistedMaterialObraToMatchAllProperties(MaterialObra expectedMaterialObra) {
        assertMaterialObraAllPropertiesEquals(expectedMaterialObra, getPersistedMaterialObra(expectedMaterialObra));
    }

    protected void assertPersistedMaterialObraToMatchUpdatableProperties(MaterialObra expectedMaterialObra) {
        assertMaterialObraAllUpdatablePropertiesEquals(expectedMaterialObra, getPersistedMaterialObra(expectedMaterialObra));
    }
}
