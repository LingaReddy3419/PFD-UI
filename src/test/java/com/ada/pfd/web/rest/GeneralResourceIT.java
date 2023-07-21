package com.ada.pfd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ada.pfd.IntegrationTest;
import com.ada.pfd.domain.General;
import com.ada.pfd.repository.GeneralRepository;
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
 * Integration tests for the {@link GeneralResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeneralResourceIT {

    private static final String ENTITY_API_URL = "/api/generals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GeneralRepository generalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeneralMockMvc;

    private General general;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static General createEntity(EntityManager em) {
        General general = new General();
        return general;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static General createUpdatedEntity(EntityManager em) {
        General general = new General();
        return general;
    }

    @BeforeEach
    public void initTest() {
        general = createEntity(em);
    }

    @Test
    @Transactional
    void createGeneral() throws Exception {
        int databaseSizeBeforeCreate = generalRepository.findAll().size();
        // Create the General
        restGeneralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(general)))
            .andExpect(status().isCreated());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeCreate + 1);
        General testGeneral = generalList.get(generalList.size() - 1);
    }

    @Test
    @Transactional
    void createGeneralWithExistingId() throws Exception {
        // Create the General with an existing ID
        general.setId(1L);

        int databaseSizeBeforeCreate = generalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeneralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(general)))
            .andExpect(status().isBadRequest());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGenerals() throws Exception {
        // Initialize the database
        generalRepository.saveAndFlush(general);

        // Get all the generalList
        restGeneralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(general.getId().intValue())));
    }

    @Test
    @Transactional
    void getGeneral() throws Exception {
        // Initialize the database
        generalRepository.saveAndFlush(general);

        // Get the general
        restGeneralMockMvc
            .perform(get(ENTITY_API_URL_ID, general.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(general.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingGeneral() throws Exception {
        // Get the general
        restGeneralMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGeneral() throws Exception {
        // Initialize the database
        generalRepository.saveAndFlush(general);

        int databaseSizeBeforeUpdate = generalRepository.findAll().size();

        // Update the general
        General updatedGeneral = generalRepository.findById(general.getId()).get();
        // Disconnect from session so that the updates on updatedGeneral are not directly saved in db
        em.detach(updatedGeneral);

        restGeneralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGeneral.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGeneral))
            )
            .andExpect(status().isOk());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
        General testGeneral = generalList.get(generalList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, general.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isBadRequest());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isBadRequest());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(general)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeneralWithPatch() throws Exception {
        // Initialize the database
        generalRepository.saveAndFlush(general);

        int databaseSizeBeforeUpdate = generalRepository.findAll().size();

        // Update the general using partial update
        General partialUpdatedGeneral = new General();
        partialUpdatedGeneral.setId(general.getId());

        restGeneralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeneral.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGeneral))
            )
            .andExpect(status().isOk());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
        General testGeneral = generalList.get(generalList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateGeneralWithPatch() throws Exception {
        // Initialize the database
        generalRepository.saveAndFlush(general);

        int databaseSizeBeforeUpdate = generalRepository.findAll().size();

        // Update the general using partial update
        General partialUpdatedGeneral = new General();
        partialUpdatedGeneral.setId(general.getId());

        restGeneralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeneral.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGeneral))
            )
            .andExpect(status().isOk());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
        General testGeneral = generalList.get(generalList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, general.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isBadRequest());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isBadRequest());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(general)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGeneral() throws Exception {
        // Initialize the database
        generalRepository.saveAndFlush(general);

        int databaseSizeBeforeDelete = generalRepository.findAll().size();

        // Delete the general
        restGeneralMockMvc
            .perform(delete(ENTITY_API_URL_ID, general.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
