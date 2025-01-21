package app.abelsc.com.web.rest;

import static app.abelsc.com.domain.EmpresaAsserts.*;
import static app.abelsc.com.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.abelsc.com.IntegrationTest;
import app.abelsc.com.domain.Empresa;
import app.abelsc.com.repository.EmpresaRepository;
import app.abelsc.com.service.dto.EmpresaDTO;
import app.abelsc.com.service.mapper.EmpresaMapper;
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
 * Integration tests for the {@link EmpresaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmpresaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_NIF = "AAAAAAAAAA";
    private static final String UPDATED_NIF = "BBBBBBBBBB";

    private static final String DEFAULT_CALLE = "AAAAAAAAAA";
    private static final String UPDATED_CALLE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCIA = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCIA = "BBBBBBBBBB";

    private static final String DEFAULT_POBLACION = "AAAAAAAAAA";
    private static final String UPDATED_POBLACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/empresas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaMapper empresaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpresaMockMvc;

    private Empresa empresa;

    private Empresa insertedEmpresa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createEntity() {
        return new Empresa()
            .nombre(DEFAULT_NOMBRE)
            .nif(DEFAULT_NIF)
            .calle(DEFAULT_CALLE)
            .telefono(DEFAULT_TELEFONO)
            .provincia(DEFAULT_PROVINCIA)
            .poblacion(DEFAULT_POBLACION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createUpdatedEntity() {
        return new Empresa()
            .nombre(UPDATED_NOMBRE)
            .nif(UPDATED_NIF)
            .calle(UPDATED_CALLE)
            .telefono(UPDATED_TELEFONO)
            .provincia(UPDATED_PROVINCIA)
            .poblacion(UPDATED_POBLACION);
    }

    @BeforeEach
    public void initTest() {
        empresa = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmpresa != null) {
            empresaRepository.delete(insertedEmpresa);
            insertedEmpresa = null;
        }
    }

    @Test
    @Transactional
    void createEmpresa() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);
        var returnedEmpresaDTO = om.readValue(
            restEmpresaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empresaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmpresaDTO.class
        );

        // Validate the Empresa in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmpresa = empresaMapper.toEntity(returnedEmpresaDTO);
        assertEmpresaUpdatableFieldsEquals(returnedEmpresa, getPersistedEmpresa(returnedEmpresa));

        insertedEmpresa = returnedEmpresa;
    }

    @Test
    @Transactional
    void createEmpresaWithExistingId() throws Exception {
        // Create the Empresa with an existing ID
        empresa.setId(1L);
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empresaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmpresas() throws Exception {
        // Initialize the database
        insertedEmpresa = empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].nif").value(hasItem(DEFAULT_NIF)))
            .andExpect(jsonPath("$.[*].calle").value(hasItem(DEFAULT_CALLE)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].provincia").value(hasItem(DEFAULT_PROVINCIA)))
            .andExpect(jsonPath("$.[*].poblacion").value(hasItem(DEFAULT_POBLACION)));
    }

    @Test
    @Transactional
    void getEmpresa() throws Exception {
        // Initialize the database
        insertedEmpresa = empresaRepository.saveAndFlush(empresa);

        // Get the empresa
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL_ID, empresa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empresa.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.nif").value(DEFAULT_NIF))
            .andExpect(jsonPath("$.calle").value(DEFAULT_CALLE))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.provincia").value(DEFAULT_PROVINCIA))
            .andExpect(jsonPath("$.poblacion").value(DEFAULT_POBLACION));
    }

    @Test
    @Transactional
    void getNonExistingEmpresa() throws Exception {
        // Get the empresa
        restEmpresaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmpresa() throws Exception {
        // Initialize the database
        insertedEmpresa = empresaRepository.saveAndFlush(empresa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empresa
        Empresa updatedEmpresa = empresaRepository.findById(empresa.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmpresa are not directly saved in db
        em.detach(updatedEmpresa);
        updatedEmpresa
            .nombre(UPDATED_NOMBRE)
            .nif(UPDATED_NIF)
            .calle(UPDATED_CALLE)
            .telefono(UPDATED_TELEFONO)
            .provincia(UPDATED_PROVINCIA)
            .poblacion(UPDATED_POBLACION);
        EmpresaDTO empresaDTO = empresaMapper.toDto(updatedEmpresa);

        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empresaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empresaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmpresaToMatchAllProperties(updatedEmpresa);
    }

    @Test
    @Transactional
    void putNonExistingEmpresa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empresa.setId(longCount.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empresaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpresa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empresa.setId(longCount.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpresa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empresa.setId(longCount.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empresaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empresa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        insertedEmpresa = empresaRepository.saveAndFlush(empresa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa.telefono(UPDATED_TELEFONO).provincia(UPDATED_PROVINCIA);

        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpresaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmpresa, empresa), getPersistedEmpresa(empresa));
    }

    @Test
    @Transactional
    void fullUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        insertedEmpresa = empresaRepository.saveAndFlush(empresa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa
            .nombre(UPDATED_NOMBRE)
            .nif(UPDATED_NIF)
            .calle(UPDATED_CALLE)
            .telefono(UPDATED_TELEFONO)
            .provincia(UPDATED_PROVINCIA)
            .poblacion(UPDATED_POBLACION);

        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpresaUpdatableFieldsEquals(partialUpdatedEmpresa, getPersistedEmpresa(partialUpdatedEmpresa));
    }

    @Test
    @Transactional
    void patchNonExistingEmpresa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empresa.setId(longCount.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empresaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpresa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empresa.setId(longCount.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpresa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empresa.setId(longCount.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(empresaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empresa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpresa() throws Exception {
        // Initialize the database
        insertedEmpresa = empresaRepository.saveAndFlush(empresa);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the empresa
        restEmpresaMockMvc
            .perform(delete(ENTITY_API_URL_ID, empresa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return empresaRepository.count();
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

    protected Empresa getPersistedEmpresa(Empresa empresa) {
        return empresaRepository.findById(empresa.getId()).orElseThrow();
    }

    protected void assertPersistedEmpresaToMatchAllProperties(Empresa expectedEmpresa) {
        assertEmpresaAllPropertiesEquals(expectedEmpresa, getPersistedEmpresa(expectedEmpresa));
    }

    protected void assertPersistedEmpresaToMatchUpdatableProperties(Empresa expectedEmpresa) {
        assertEmpresaAllUpdatablePropertiesEquals(expectedEmpresa, getPersistedEmpresa(expectedEmpresa));
    }
}
