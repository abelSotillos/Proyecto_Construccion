package app.abelsc.com.web.rest;

import static app.abelsc.com.domain.MaquinariaAsserts.*;
import static app.abelsc.com.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.abelsc.com.IntegrationTest;
import app.abelsc.com.domain.Maquinaria;
import app.abelsc.com.domain.enumeration.EstadoMaquinaria;
import app.abelsc.com.repository.MaquinariaRepository;
import app.abelsc.com.service.dto.MaquinariaDTO;
import app.abelsc.com.service.mapper.MaquinariaMapper;
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
 * Integration tests for the {@link MaquinariaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaquinariaResourceIT {

    private static final String DEFAULT_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_MODELO = "BBBBBBBBBB";

    private static final EstadoMaquinaria DEFAULT_ESTADO = EstadoMaquinaria.USO;
    private static final EstadoMaquinaria UPDATED_ESTADO = EstadoMaquinaria.PARADA;

    private static final Long DEFAULT_PRECIO = 1L;
    private static final Long UPDATED_PRECIO = 2L;

    private static final String ENTITY_API_URL = "/api/maquinarias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaquinariaRepository maquinariaRepository;

    @Autowired
    private MaquinariaMapper maquinariaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaquinariaMockMvc;

    private Maquinaria maquinaria;

    private Maquinaria insertedMaquinaria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maquinaria createEntity() {
        return new Maquinaria().modelo(DEFAULT_MODELO).estado(DEFAULT_ESTADO).precio(DEFAULT_PRECIO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maquinaria createUpdatedEntity() {
        return new Maquinaria().modelo(UPDATED_MODELO).estado(UPDATED_ESTADO).precio(UPDATED_PRECIO);
    }

    @BeforeEach
    public void initTest() {
        maquinaria = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMaquinaria != null) {
            maquinariaRepository.delete(insertedMaquinaria);
            insertedMaquinaria = null;
        }
    }

    @Test
    @Transactional
    void createMaquinaria() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Maquinaria
        MaquinariaDTO maquinariaDTO = maquinariaMapper.toDto(maquinaria);
        var returnedMaquinariaDTO = om.readValue(
            restMaquinariaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maquinariaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaquinariaDTO.class
        );

        // Validate the Maquinaria in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaquinaria = maquinariaMapper.toEntity(returnedMaquinariaDTO);
        assertMaquinariaUpdatableFieldsEquals(returnedMaquinaria, getPersistedMaquinaria(returnedMaquinaria));

        insertedMaquinaria = returnedMaquinaria;
    }

    @Test
    @Transactional
    void createMaquinariaWithExistingId() throws Exception {
        // Create the Maquinaria with an existing ID
        maquinaria.setId(1L);
        MaquinariaDTO maquinariaDTO = maquinariaMapper.toDto(maquinaria);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaquinariaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maquinariaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Maquinaria in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaquinarias() throws Exception {
        // Initialize the database
        insertedMaquinaria = maquinariaRepository.saveAndFlush(maquinaria);

        // Get all the maquinariaList
        restMaquinariaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maquinaria.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.intValue())));
    }

    @Test
    @Transactional
    void getMaquinaria() throws Exception {
        // Initialize the database
        insertedMaquinaria = maquinariaRepository.saveAndFlush(maquinaria);

        // Get the maquinaria
        restMaquinariaMockMvc
            .perform(get(ENTITY_API_URL_ID, maquinaria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maquinaria.getId().intValue()))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMaquinaria() throws Exception {
        // Get the maquinaria
        restMaquinariaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaquinaria() throws Exception {
        // Initialize the database
        insertedMaquinaria = maquinariaRepository.saveAndFlush(maquinaria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maquinaria
        Maquinaria updatedMaquinaria = maquinariaRepository.findById(maquinaria.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaquinaria are not directly saved in db
        em.detach(updatedMaquinaria);
        updatedMaquinaria.modelo(UPDATED_MODELO).estado(UPDATED_ESTADO).precio(UPDATED_PRECIO);
        MaquinariaDTO maquinariaDTO = maquinariaMapper.toDto(updatedMaquinaria);

        restMaquinariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maquinariaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maquinariaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Maquinaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaquinariaToMatchAllProperties(updatedMaquinaria);
    }

    @Test
    @Transactional
    void putNonExistingMaquinaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinaria.setId(longCount.incrementAndGet());

        // Create the Maquinaria
        MaquinariaDTO maquinariaDTO = maquinariaMapper.toDto(maquinaria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaquinariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maquinariaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maquinariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maquinaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaquinaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinaria.setId(longCount.incrementAndGet());

        // Create the Maquinaria
        MaquinariaDTO maquinariaDTO = maquinariaMapper.toDto(maquinaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaquinariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maquinariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maquinaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaquinaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinaria.setId(longCount.incrementAndGet());

        // Create the Maquinaria
        MaquinariaDTO maquinariaDTO = maquinariaMapper.toDto(maquinaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaquinariaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maquinariaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maquinaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaquinariaWithPatch() throws Exception {
        // Initialize the database
        insertedMaquinaria = maquinariaRepository.saveAndFlush(maquinaria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maquinaria using partial update
        Maquinaria partialUpdatedMaquinaria = new Maquinaria();
        partialUpdatedMaquinaria.setId(maquinaria.getId());

        partialUpdatedMaquinaria.modelo(UPDATED_MODELO);

        restMaquinariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaquinaria.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaquinaria))
            )
            .andExpect(status().isOk());

        // Validate the Maquinaria in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaquinariaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMaquinaria, maquinaria),
            getPersistedMaquinaria(maquinaria)
        );
    }

    @Test
    @Transactional
    void fullUpdateMaquinariaWithPatch() throws Exception {
        // Initialize the database
        insertedMaquinaria = maquinariaRepository.saveAndFlush(maquinaria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maquinaria using partial update
        Maquinaria partialUpdatedMaquinaria = new Maquinaria();
        partialUpdatedMaquinaria.setId(maquinaria.getId());

        partialUpdatedMaquinaria.modelo(UPDATED_MODELO).estado(UPDATED_ESTADO).precio(UPDATED_PRECIO);

        restMaquinariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaquinaria.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaquinaria))
            )
            .andExpect(status().isOk());

        // Validate the Maquinaria in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaquinariaUpdatableFieldsEquals(partialUpdatedMaquinaria, getPersistedMaquinaria(partialUpdatedMaquinaria));
    }

    @Test
    @Transactional
    void patchNonExistingMaquinaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinaria.setId(longCount.incrementAndGet());

        // Create the Maquinaria
        MaquinariaDTO maquinariaDTO = maquinariaMapper.toDto(maquinaria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaquinariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maquinariaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(maquinariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maquinaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaquinaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinaria.setId(longCount.incrementAndGet());

        // Create the Maquinaria
        MaquinariaDTO maquinariaDTO = maquinariaMapper.toDto(maquinaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaquinariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(maquinariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maquinaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaquinaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maquinaria.setId(longCount.incrementAndGet());

        // Create the Maquinaria
        MaquinariaDTO maquinariaDTO = maquinariaMapper.toDto(maquinaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaquinariaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(maquinariaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maquinaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaquinaria() throws Exception {
        // Initialize the database
        insertedMaquinaria = maquinariaRepository.saveAndFlush(maquinaria);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the maquinaria
        restMaquinariaMockMvc
            .perform(delete(ENTITY_API_URL_ID, maquinaria.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return maquinariaRepository.count();
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

    protected Maquinaria getPersistedMaquinaria(Maquinaria maquinaria) {
        return maquinariaRepository.findById(maquinaria.getId()).orElseThrow();
    }

    protected void assertPersistedMaquinariaToMatchAllProperties(Maquinaria expectedMaquinaria) {
        assertMaquinariaAllPropertiesEquals(expectedMaquinaria, getPersistedMaquinaria(expectedMaquinaria));
    }

    protected void assertPersistedMaquinariaToMatchUpdatableProperties(Maquinaria expectedMaquinaria) {
        assertMaquinariaAllUpdatablePropertiesEquals(expectedMaquinaria, getPersistedMaquinaria(expectedMaquinaria));
    }
}
