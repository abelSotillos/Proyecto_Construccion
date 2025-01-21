package app.abelsc.com.web.rest;

import static app.abelsc.com.domain.MaterialAsserts.*;
import static app.abelsc.com.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.abelsc.com.IntegrationTest;
import app.abelsc.com.domain.Material;
import app.abelsc.com.domain.enumeration.UnidadMedida;
import app.abelsc.com.repository.MaterialRepository;
import app.abelsc.com.service.dto.MaterialDTO;
import app.abelsc.com.service.mapper.MaterialMapper;
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
 * Integration tests for the {@link MaterialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Long DEFAULT_PRECIO = 1L;
    private static final Long UPDATED_PRECIO = 2L;

    private static final Long DEFAULT_STOCK = 1L;
    private static final Long UPDATED_STOCK = 2L;

    private static final UnidadMedida DEFAULT_UNIDAD_MEDIDA = UnidadMedida.SACO;
    private static final UnidadMedida UPDATED_UNIDAD_MEDIDA = UnidadMedida.PAQUETE;

    private static final String ENTITY_API_URL = "/api/materials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialMockMvc;

    private Material material;

    private Material insertedMaterial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createEntity() {
        return new Material()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precio(DEFAULT_PRECIO)
            .stock(DEFAULT_STOCK)
            .unidadMedida(DEFAULT_UNIDAD_MEDIDA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createUpdatedEntity() {
        return new Material()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .stock(UPDATED_STOCK)
            .unidadMedida(UPDATED_UNIDAD_MEDIDA);
    }

    @BeforeEach
    public void initTest() {
        material = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMaterial != null) {
            materialRepository.delete(insertedMaterial);
            insertedMaterial = null;
        }
    }

    @Test
    @Transactional
    void createMaterial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);
        var returnedMaterialDTO = om.readValue(
            restMaterialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaterialDTO.class
        );

        // Validate the Material in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaterial = materialMapper.toEntity(returnedMaterialDTO);
        assertMaterialUpdatableFieldsEquals(returnedMaterial, getPersistedMaterial(returnedMaterial));

        insertedMaterial = returnedMaterial;
    }

    @Test
    @Transactional
    void createMaterialWithExistingId() throws Exception {
        // Create the Material with an existing ID
        material.setId(1L);
        MaterialDTO materialDTO = materialMapper.toDto(material);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaterials() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get all the materialList
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK.intValue())))
            .andExpect(jsonPath("$.[*].unidadMedida").value(hasItem(DEFAULT_UNIDAD_MEDIDA.toString())));
    }

    @Test
    @Transactional
    void getMaterial() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        // Get the material
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL_ID, material.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(material.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.intValue()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK.intValue()))
            .andExpect(jsonPath("$.unidadMedida").value(DEFAULT_UNIDAD_MEDIDA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMaterial() throws Exception {
        // Get the material
        restMaterialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterial() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the material
        Material updatedMaterial = materialRepository.findById(material.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaterial are not directly saved in db
        em.detach(updatedMaterial);
        updatedMaterial
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .stock(UPDATED_STOCK)
            .unidadMedida(UPDATED_UNIDAD_MEDIDA);
        MaterialDTO materialDTO = materialMapper.toDto(updatedMaterial);

        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaterialToMatchAllProperties(updatedMaterial);
    }

    @Test
    @Transactional
    void putNonExistingMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(materialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial.nombre(UPDATED_NOMBRE).stock(UPDATED_STOCK).unidadMedida(UPDATED_UNIDAD_MEDIDA);

        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterial))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMaterial, material), getPersistedMaterial(material));
    }

    @Test
    @Transactional
    void fullUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .stock(UPDATED_STOCK)
            .unidadMedida(UPDATED_UNIDAD_MEDIDA);

        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaterial))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaterialUpdatableFieldsEquals(partialUpdatedMaterial, getPersistedMaterial(partialUpdatedMaterial));
    }

    @Test
    @Transactional
    void patchNonExistingMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, materialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(materialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        material.setId(longCount.incrementAndGet());

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(materialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Material in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterial() throws Exception {
        // Initialize the database
        insertedMaterial = materialRepository.saveAndFlush(material);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the material
        restMaterialMockMvc
            .perform(delete(ENTITY_API_URL_ID, material.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return materialRepository.count();
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

    protected Material getPersistedMaterial(Material material) {
        return materialRepository.findById(material.getId()).orElseThrow();
    }

    protected void assertPersistedMaterialToMatchAllProperties(Material expectedMaterial) {
        assertMaterialAllPropertiesEquals(expectedMaterial, getPersistedMaterial(expectedMaterial));
    }

    protected void assertPersistedMaterialToMatchUpdatableProperties(Material expectedMaterial) {
        assertMaterialAllUpdatablePropertiesEquals(expectedMaterial, getPersistedMaterial(expectedMaterial));
    }
}
