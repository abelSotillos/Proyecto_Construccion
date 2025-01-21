package app.abelsc.com.web.rest;

import static app.abelsc.com.domain.ObraAsserts.*;
import static app.abelsc.com.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.abelsc.com.IntegrationTest;
import app.abelsc.com.domain.Obra;
import app.abelsc.com.domain.enumeration.EstadoObra;
import app.abelsc.com.repository.ObraRepository;
import app.abelsc.com.service.dto.ObraDTO;
import app.abelsc.com.service.mapper.ObraMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ObraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ObraResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_COSTE = 1L;
    private static final Long UPDATED_COSTE = 2L;

    private static final EstadoObra DEFAULT_ESTADO = EstadoObra.TERMINADO;
    private static final EstadoObra UPDATED_ESTADO = EstadoObra.INICIADO;

    private static final Long DEFAULT_COSTE_PAGADO = 1L;
    private static final Long UPDATED_COSTE_PAGADO = 2L;

    private static final String ENTITY_API_URL = "/api/obras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private ObraMapper obraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restObraMockMvc;

    private Obra obra;

    private Obra insertedObra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Obra createEntity() {
        return new Obra()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .coste(DEFAULT_COSTE)
            .estado(DEFAULT_ESTADO)
            .costePagado(DEFAULT_COSTE_PAGADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Obra createUpdatedEntity() {
        return new Obra()
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .coste(UPDATED_COSTE)
            .estado(UPDATED_ESTADO)
            .costePagado(UPDATED_COSTE_PAGADO);
    }

    @BeforeEach
    public void initTest() {
        obra = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedObra != null) {
            obraRepository.delete(insertedObra);
            insertedObra = null;
        }
    }

    @Test
    @Transactional
    void createObra() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Obra
        ObraDTO obraDTO = obraMapper.toDto(obra);
        var returnedObraDTO = om.readValue(
            restObraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ObraDTO.class
        );

        // Validate the Obra in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedObra = obraMapper.toEntity(returnedObraDTO);
        assertObraUpdatableFieldsEquals(returnedObra, getPersistedObra(returnedObra));

        insertedObra = returnedObra;
    }

    @Test
    @Transactional
    void createObraWithExistingId() throws Exception {
        // Create the Obra with an existing ID
        obra.setId(1L);
        ObraDTO obraDTO = obraMapper.toDto(obra);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restObraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Obra in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllObras() throws Exception {
        // Initialize the database
        insertedObra = obraRepository.saveAndFlush(obra);

        // Get all the obraList
        restObraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(obra.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].coste").value(hasItem(DEFAULT_COSTE.intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].costePagado").value(hasItem(DEFAULT_COSTE_PAGADO.intValue())));
    }

    @Test
    @Transactional
    void getObra() throws Exception {
        // Initialize the database
        insertedObra = obraRepository.saveAndFlush(obra);

        // Get the obra
        restObraMockMvc
            .perform(get(ENTITY_API_URL_ID, obra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(obra.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.coste").value(DEFAULT_COSTE.intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.costePagado").value(DEFAULT_COSTE_PAGADO.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingObra() throws Exception {
        // Get the obra
        restObraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingObra() throws Exception {
        // Initialize the database
        insertedObra = obraRepository.saveAndFlush(obra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the obra
        Obra updatedObra = obraRepository.findById(obra.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedObra are not directly saved in db
        em.detach(updatedObra);
        updatedObra
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .coste(UPDATED_COSTE)
            .estado(UPDATED_ESTADO)
            .costePagado(UPDATED_COSTE_PAGADO);
        ObraDTO obraDTO = obraMapper.toDto(updatedObra);

        restObraMockMvc
            .perform(put(ENTITY_API_URL_ID, obraDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraDTO)))
            .andExpect(status().isOk());

        // Validate the Obra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedObraToMatchAllProperties(updatedObra);
    }

    @Test
    @Transactional
    void putNonExistingObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obra.setId(longCount.incrementAndGet());

        // Create the Obra
        ObraDTO obraDTO = obraMapper.toDto(obra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObraMockMvc
            .perform(put(ENTITY_API_URL_ID, obraDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Obra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obra.setId(longCount.incrementAndGet());

        // Create the Obra
        ObraDTO obraDTO = obraMapper.toDto(obra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(obraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Obra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obra.setId(longCount.incrementAndGet());

        // Create the Obra
        ObraDTO obraDTO = obraMapper.toDto(obra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(obraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Obra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateObraWithPatch() throws Exception {
        // Initialize the database
        insertedObra = obraRepository.saveAndFlush(obra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the obra using partial update
        Obra partialUpdatedObra = new Obra();
        partialUpdatedObra.setId(obra.getId());

        partialUpdatedObra.nombre(UPDATED_NOMBRE).direccion(UPDATED_DIRECCION).fechaFin(UPDATED_FECHA_FIN).estado(UPDATED_ESTADO);

        restObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedObra))
            )
            .andExpect(status().isOk());

        // Validate the Obra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertObraUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedObra, obra), getPersistedObra(obra));
    }

    @Test
    @Transactional
    void fullUpdateObraWithPatch() throws Exception {
        // Initialize the database
        insertedObra = obraRepository.saveAndFlush(obra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the obra using partial update
        Obra partialUpdatedObra = new Obra();
        partialUpdatedObra.setId(obra.getId());

        partialUpdatedObra
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .coste(UPDATED_COSTE)
            .estado(UPDATED_ESTADO)
            .costePagado(UPDATED_COSTE_PAGADO);

        restObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedObra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedObra))
            )
            .andExpect(status().isOk());

        // Validate the Obra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertObraUpdatableFieldsEquals(partialUpdatedObra, getPersistedObra(partialUpdatedObra));
    }

    @Test
    @Transactional
    void patchNonExistingObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obra.setId(longCount.incrementAndGet());

        // Create the Obra
        ObraDTO obraDTO = obraMapper.toDto(obra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, obraDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(obraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Obra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obra.setId(longCount.incrementAndGet());

        // Create the Obra
        ObraDTO obraDTO = obraMapper.toDto(obra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(obraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Obra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamObra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        obra.setId(longCount.incrementAndGet());

        // Create the Obra
        ObraDTO obraDTO = obraMapper.toDto(obra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restObraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(obraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Obra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteObra() throws Exception {
        // Initialize the database
        insertedObra = obraRepository.saveAndFlush(obra);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the obra
        restObraMockMvc
            .perform(delete(ENTITY_API_URL_ID, obra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return obraRepository.count();
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

    protected Obra getPersistedObra(Obra obra) {
        return obraRepository.findById(obra.getId()).orElseThrow();
    }

    protected void assertPersistedObraToMatchAllProperties(Obra expectedObra) {
        assertObraAllPropertiesEquals(expectedObra, getPersistedObra(expectedObra));
    }

    protected void assertPersistedObraToMatchUpdatableProperties(Obra expectedObra) {
        assertObraAllUpdatablePropertiesEquals(expectedObra, getPersistedObra(expectedObra));
    }
}
