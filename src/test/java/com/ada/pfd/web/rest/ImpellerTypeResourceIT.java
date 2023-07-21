package com.ada.pfd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ada.pfd.IntegrationTest;
import com.ada.pfd.domain.ImpellerType;
import com.ada.pfd.repository.ImpellerTypeRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ImpellerTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImpellerTypeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/impeller-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImpellerTypeRepository impellerTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImpellerTypeMockMvc;

    private ImpellerType impellerType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImpellerType createEntity(EntityManager em) {
        ImpellerType impellerType = new ImpellerType().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return impellerType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImpellerType createUpdatedEntity(EntityManager em) {
        ImpellerType impellerType = new ImpellerType().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return impellerType;
    }

    @BeforeEach
    public void initTest() {
        impellerType = createEntity(em);
    }

    @Test
    @Transactional
    void createImpellerType() throws Exception {
        int databaseSizeBeforeCreate = impellerTypeRepository.findAll().size();
        // Create the ImpellerType
        restImpellerTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(impellerType)))
            .andExpect(status().isCreated());

        // Validate the ImpellerType in the database
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ImpellerType testImpellerType = impellerTypeList.get(impellerTypeList.size() - 1);
        assertThat(testImpellerType.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testImpellerType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createImpellerTypeWithExistingId() throws Exception {
        // Create the ImpellerType with an existing ID
        impellerType.setId(1L);

        int databaseSizeBeforeCreate = impellerTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImpellerTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(impellerType)))
            .andExpect(status().isBadRequest());

        // Validate the ImpellerType in the database
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImpellerTypes() throws Exception {
        // Initialize the database
        impellerTypeRepository.saveAndFlush(impellerType);

        // Get all the impellerTypeList
        restImpellerTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(impellerType.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getImpellerType() throws Exception {
        // Initialize the database
        impellerTypeRepository.saveAndFlush(impellerType);

        // Get the impellerType
        restImpellerTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, impellerType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(impellerType.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingImpellerType() throws Exception {
        // Get the impellerType
        restImpellerTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImpellerType() throws Exception {
        // Initialize the database
        impellerTypeRepository.saveAndFlush(impellerType);

        int databaseSizeBeforeUpdate = impellerTypeRepository.findAll().size();

        // Update the impellerType
        ImpellerType updatedImpellerType = impellerTypeRepository.findById(impellerType.getId()).get();
        // Disconnect from session so that the updates on updatedImpellerType are not directly saved in db
        em.detach(updatedImpellerType);
        updatedImpellerType.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restImpellerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImpellerType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedImpellerType))
            )
            .andExpect(status().isOk());

        // Validate the ImpellerType in the database
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeUpdate);
        ImpellerType testImpellerType = impellerTypeList.get(impellerTypeList.size() - 1);
        assertThat(testImpellerType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImpellerType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingImpellerType() throws Exception {
        int databaseSizeBeforeUpdate = impellerTypeRepository.findAll().size();
        impellerType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImpellerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, impellerType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(impellerType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpellerType in the database
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImpellerType() throws Exception {
        int databaseSizeBeforeUpdate = impellerTypeRepository.findAll().size();
        impellerType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpellerTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(impellerType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpellerType in the database
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImpellerType() throws Exception {
        int databaseSizeBeforeUpdate = impellerTypeRepository.findAll().size();
        impellerType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpellerTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(impellerType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImpellerType in the database
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImpellerTypeWithPatch() throws Exception {
        // Initialize the database
        impellerTypeRepository.saveAndFlush(impellerType);

        int databaseSizeBeforeUpdate = impellerTypeRepository.findAll().size();

        // Update the impellerType using partial update
        ImpellerType partialUpdatedImpellerType = new ImpellerType();
        partialUpdatedImpellerType.setId(impellerType.getId());

        partialUpdatedImpellerType.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restImpellerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImpellerType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImpellerType))
            )
            .andExpect(status().isOk());

        // Validate the ImpellerType in the database
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeUpdate);
        ImpellerType testImpellerType = impellerTypeList.get(impellerTypeList.size() - 1);
        assertThat(testImpellerType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImpellerType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateImpellerTypeWithPatch() throws Exception {
        // Initialize the database
        impellerTypeRepository.saveAndFlush(impellerType);

        int databaseSizeBeforeUpdate = impellerTypeRepository.findAll().size();

        // Update the impellerType using partial update
        ImpellerType partialUpdatedImpellerType = new ImpellerType();
        partialUpdatedImpellerType.setId(impellerType.getId());

        partialUpdatedImpellerType.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restImpellerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImpellerType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImpellerType))
            )
            .andExpect(status().isOk());

        // Validate the ImpellerType in the database
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeUpdate);
        ImpellerType testImpellerType = impellerTypeList.get(impellerTypeList.size() - 1);
        assertThat(testImpellerType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImpellerType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingImpellerType() throws Exception {
        int databaseSizeBeforeUpdate = impellerTypeRepository.findAll().size();
        impellerType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImpellerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, impellerType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(impellerType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpellerType in the database
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImpellerType() throws Exception {
        int databaseSizeBeforeUpdate = impellerTypeRepository.findAll().size();
        impellerType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpellerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(impellerType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpellerType in the database
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImpellerType() throws Exception {
        int databaseSizeBeforeUpdate = impellerTypeRepository.findAll().size();
        impellerType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpellerTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(impellerType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImpellerType in the database
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImpellerType() throws Exception {
        // Initialize the database
        impellerTypeRepository.saveAndFlush(impellerType);

        int databaseSizeBeforeDelete = impellerTypeRepository.findAll().size();

        // Delete the impellerType
        restImpellerTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, impellerType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ImpellerType> impellerTypeList = impellerTypeRepository.findAll();
        assertThat(impellerTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
