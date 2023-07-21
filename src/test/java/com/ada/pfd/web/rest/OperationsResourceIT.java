package com.ada.pfd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ada.pfd.IntegrationTest;
import com.ada.pfd.domain.Operations;
import com.ada.pfd.repository.OperationsRepository;
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
 * Integration tests for the {@link OperationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperationsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperationsRepository operationsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperationsMockMvc;

    private Operations operations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operations createEntity(EntityManager em) {
        Operations operations = new Operations().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return operations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operations createUpdatedEntity(EntityManager em) {
        Operations operations = new Operations().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return operations;
    }

    @BeforeEach
    public void initTest() {
        operations = createEntity(em);
    }

    @Test
    @Transactional
    void createOperations() throws Exception {
        int databaseSizeBeforeCreate = operationsRepository.findAll().size();
        // Create the Operations
        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isCreated());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeCreate + 1);
        Operations testOperations = operationsList.get(operationsList.size() - 1);
        assertThat(testOperations.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOperations.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createOperationsWithExistingId() throws Exception {
        // Create the Operations with an existing ID
        operations.setId(1L);

        int databaseSizeBeforeCreate = operationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isBadRequest());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOperations() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        // Get all the operationsList
        restOperationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operations.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getOperations() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        // Get the operations
        restOperationsMockMvc
            .perform(get(ENTITY_API_URL_ID, operations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operations.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingOperations() throws Exception {
        // Get the operations
        restOperationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperations() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();

        // Update the operations
        Operations updatedOperations = operationsRepository.findById(operations.getId()).get();
        // Disconnect from session so that the updates on updatedOperations are not directly saved in db
        em.detach(updatedOperations);
        updatedOperations.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restOperationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOperations.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOperations))
            )
            .andExpect(status().isOk());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
        Operations testOperations = operationsList.get(operationsList.size() - 1);
        assertThat(testOperations.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOperations.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operations.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operations)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperationsWithPatch() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();

        // Update the operations using partial update
        Operations partialUpdatedOperations = new Operations();
        partialUpdatedOperations.setId(operations.getId());

        partialUpdatedOperations.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restOperationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperations))
            )
            .andExpect(status().isOk());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
        Operations testOperations = operationsList.get(operationsList.size() - 1);
        assertThat(testOperations.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOperations.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateOperationsWithPatch() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();

        // Update the operations using partial update
        Operations partialUpdatedOperations = new Operations();
        partialUpdatedOperations.setId(operations.getId());

        partialUpdatedOperations.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restOperationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperations))
            )
            .andExpect(status().isOk());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
        Operations testOperations = operationsList.get(operationsList.size() - 1);
        assertThat(testOperations.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOperations.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operations))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperations() throws Exception {
        int databaseSizeBeforeUpdate = operationsRepository.findAll().size();
        operations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operations))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operations in the database
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperations() throws Exception {
        // Initialize the database
        operationsRepository.saveAndFlush(operations);

        int databaseSizeBeforeDelete = operationsRepository.findAll().size();

        // Delete the operations
        restOperationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, operations.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operations> operationsList = operationsRepository.findAll();
        assertThat(operationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
