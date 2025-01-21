package app.abelsc.com.web.rest;

import static app.abelsc.com.domain.MaquinariaObraAsserts.*;
import static app.abelsc.com.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.abelsc.com.IntegrationTest;
import app.abelsc.com.domain.MaquinariaObra;
import app.abelsc.com.repository.MaquinariaObraRepository;
import app.abelsc.com.service.dto.MaquinariaObraDTO;
import app.abelsc.com.service.mapper.MaquinariaObraMapper;
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
 * Integration tests for the {@link MaquinariaObraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaquinariaObraResourceIT {

    private static final Long DEFAULT_HORAS = 1L;
    private static final Long UPDATED_HORAS = 2L;

    private static final String ENTITY_API_URL = "/api/maquinaria-obras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaquinariaObraRepository maquinariaObraRepository;

    @Autowired
    private MaquinariaObraMapper maquinariaObraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaquinariaObraMockMvc;

    private MaquinariaObra maquinariaObra;

    private MaquinariaObra insertedMaquinariaObra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaquinariaObra createEntity() {
        return new MaquinariaObra().horas(DEFAULT_HORAS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaquinariaObra createUpdatedEntity() {
        return new MaquinariaObra().horas(UPDATED_HORAS);
    }

    @BeforeEach
    public void initTest() {
        maquinariaObra = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMaquinariaObra != null) {
            maquinariaObraRepository.delete(insertedMaquinariaObra);
            insertedMaquinariaObra = null;
        }
    }

    @Test
    @Transactional
    void createMaquinariaObra() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MaquinariaObra
        MaquinariaObraDTO maquinariaObraDTO = maquinariaObraMapper.toDto(maquinariaObra);
        var returnedMaquinariaObraDTO = om.readValue(
            restMaquinariaObraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maquinariaObraDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaquinariaObraDTO.class
        );

        // Validate the MaquinariaObra in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaquinariaObra = maquinariaObraMapper.toEntity(returnedMaquinariaObraDTO);
        assertMaquinariaObraUpdatableFieldsEquals(returnedMaquinariaObra, getPersistedMaquinariaObra(returnedMaquinariaObra));

        insertedMaquinariaObra = returnedMaquinariaObra;
    }

    @Test
    @Transactional
    void createMaquinariaObraWithExistingId() throws Exception {
        // Create the MaquinariaObra with an existing ID
        maquinariaObra.setId(1L);
        MaquinariaObraDTO maquinariaObraDTO = maquinariaObraMapper.toDto(maquinariaObra);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaquinariaObraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maquinariaObraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaquinariaObra in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaquinariaObras() throws Exception {
        // Initialize the database
        insertedMaquinariaObra = maquinariaObraRepository.saveAndFlush(maquinariaObra);

        // Get all the maquinariaObraList
        restMaquinariaObraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maquinariaObra.getId().intValue())))
            .andExpect(jsonPath("$.[*].horas").value(hasItem(DEFAULT_HORAS.intValue())));
    }

    @Test
    @Transactional
    void getMaquinariaObra() throws Exception {
        // Initialize the database
        insertedMaquinariaObra = maquinariaObraRepository.saveAndFlush(maquinariaObra);

        // Get the maquinariaObra
        restMaquinariaObraMockMvc
            .perform(get(ENTITY_API_URL_ID, maquinariaObra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maquinariaObra.getId().intValue()))
            .andExpect(jsonPath("$.horas").value(DEFAULT_HORAS.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMaquinariaObra() throws Exception {
        // Get the maquinariaObra
        restMaquinariaObraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaquinariaObra() throws Exception {
        // Initialize the database
        insertedMaquinariaObra = maquinariaObraRepository.saveAndFlush(maquinariaObra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maquinariaObra
        MaquinariaObra updatedMaquinariaObra = maquinariaObraRepository.findById(maquinariaObra.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaquinariaObra are not directly saved in db
        em.detach(updatedMaquinariaObra);
        updatedMaquinariaObra.horas(UPDATED_HORAS);
        MaquinariaObraDTO maquinariaObraDTO = maquinariaObraMapper.toDto(updatedMaquinariaObra);

        restMaquinariaObraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maquinariaObraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maquinariaObraDTO))
            )
            .andExpect(status().isOk());

        // Validate the MaquinariaObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaquinariaObraToMatchAllProperties(updatedMaquinariaObra);
    }

    @Test
    @Transactional
    void putNonExistingMaquinariaObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinariaObra.setId(longCount.incrementAndGet());

        // Create the MaquinariaObra
        MaquinariaObraDTO maquinariaObraDTO = maquinariaObraMapper.toDto(maquinariaObra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaquinariaObraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maquinariaObraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maquinariaObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaquinariaObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaquinariaObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinariaObra.setId(longCount.incrementAndGet());

        // Create the MaquinariaObra
        MaquinariaObraDTO maquinariaObraDTO = maquinariaObraMapper.toDto(maquinariaObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaquinariaObraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maquinariaObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaquinariaObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaquinariaObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinariaObra.setId(longCount.incrementAndGet());

        // Create the MaquinariaObra
        MaquinariaObraDTO maquinariaObraDTO = maquinariaObraMapper.toDto(maquinariaObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaquinariaObraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maquinariaObraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaquinariaObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaquinariaObraWithPatch() throws Exception {
        // Initialize the database
        insertedMaquinariaObra = maquinariaObraRepository.saveAndFlush(maquinariaObra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maquinariaObra using partial update
        MaquinariaObra partialUpdatedMaquinariaObra = new MaquinariaObra();
        partialUpdatedMaquinariaObra.setId(maquinariaObra.getId());

        partialUpdatedMaquinariaObra.horas(UPDATED_HORAS);

        restMaquinariaObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaquinariaObra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaquinariaObra))
            )
            .andExpect(status().isOk());

        // Validate the MaquinariaObra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaquinariaObraUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMaquinariaObra, maquinariaObra),
            getPersistedMaquinariaObra(maquinariaObra)
        );
    }

    @Test
    @Transactional
    void fullUpdateMaquinariaObraWithPatch() throws Exception {
        // Initialize the database
        insertedMaquinariaObra = maquinariaObraRepository.saveAndFlush(maquinariaObra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maquinariaObra using partial update
        MaquinariaObra partialUpdatedMaquinariaObra = new MaquinariaObra();
        partialUpdatedMaquinariaObra.setId(maquinariaObra.getId());

        partialUpdatedMaquinariaObra.horas(UPDATED_HORAS);

        restMaquinariaObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaquinariaObra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaquinariaObra))
            )
            .andExpect(status().isOk());

        // Validate the MaquinariaObra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaquinariaObraUpdatableFieldsEquals(partialUpdatedMaquinariaObra, getPersistedMaquinariaObra(partialUpdatedMaquinariaObra));
    }

    @Test
    @Transactional
    void patchNonExistingMaquinariaObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinariaObra.setId(longCount.incrementAndGet());

        // Create the MaquinariaObra
        MaquinariaObraDTO maquinariaObraDTO = maquinariaObraMapper.toDto(maquinariaObra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaquinariaObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maquinariaObraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(maquinariaObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaquinariaObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaquinariaObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinariaObra.setId(longCount.incrementAndGet());

        // Create the MaquinariaObra
        MaquinariaObraDTO maquinariaObraDTO = maquinariaObraMapper.toDto(maquinariaObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaquinariaObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(maquinariaObraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaquinariaObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaquinariaObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinariaObra.setId(longCount.incrementAndGet());

        // Create the MaquinariaObra
        MaquinariaObraDTO maquinariaObraDTO = maquinariaObraMapper.toDto(maquinariaObra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaquinariaObraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(maquinariaObraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaquinariaObra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaquinariaObra() throws Exception {
        // Initialize the database
        insertedMaquinariaObra = maquinariaObraRepository.saveAndFlush(maquinariaObra);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the maquinariaObra
        restMaquinariaObraMockMvc
            .perform(delete(ENTITY_API_URL_ID, maquinariaObra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return maquinariaObraRepository.count();
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

    protected MaquinariaObra getPersistedMaquinariaObra(MaquinariaObra maquinariaObra) {
        return maquinariaObraRepository.findById(maquinariaObra.getId()).orElseThrow();
    }

    protected void assertPersistedMaquinariaObraToMatchAllProperties(MaquinariaObra expectedMaquinariaObra) {
        assertMaquinariaObraAllPropertiesEquals(expectedMaquinariaObra, getPersistedMaquinariaObra(expectedMaquinariaObra));
    }

    protected void assertPersistedMaquinariaObraToMatchUpdatableProperties(MaquinariaObra expectedMaquinariaObra) {
        assertMaquinariaObraAllUpdatablePropertiesEquals(expectedMaquinariaObra, getPersistedMaquinariaObra(expectedMaquinariaObra));
    }
}
