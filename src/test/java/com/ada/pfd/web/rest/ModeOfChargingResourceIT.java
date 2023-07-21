package com.ada.pfd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ada.pfd.IntegrationTest;
import com.ada.pfd.domain.ModeOfCharging;
import com.ada.pfd.repository.ModeOfChargingRepository;
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
 * Integration tests for the {@link ModeOfChargingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ModeOfChargingResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mode-of-chargings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ModeOfChargingRepository modeOfChargingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModeOfChargingMockMvc;

    private ModeOfCharging modeOfCharging;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModeOfCharging createEntity(EntityManager em) {
        ModeOfCharging modeOfCharging = new ModeOfCharging().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return modeOfCharging;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModeOfCharging createUpdatedEntity(EntityManager em) {
        ModeOfCharging modeOfCharging = new ModeOfCharging().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return modeOfCharging;
    }

    @BeforeEach
    public void initTest() {
        modeOfCharging = createEntity(em);
    }

    @Test
    @Transactional
    void createModeOfCharging() throws Exception {
        int databaseSizeBeforeCreate = modeOfChargingRepository.findAll().size();
        // Create the ModeOfCharging
        restModeOfChargingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modeOfCharging))
            )
            .andExpect(status().isCreated());

        // Validate the ModeOfCharging in the database
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeCreate + 1);
        ModeOfCharging testModeOfCharging = modeOfChargingList.get(modeOfChargingList.size() - 1);
        assertThat(testModeOfCharging.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testModeOfCharging.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createModeOfChargingWithExistingId() throws Exception {
        // Create the ModeOfCharging with an existing ID
        modeOfCharging.setId(1L);

        int databaseSizeBeforeCreate = modeOfChargingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModeOfChargingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modeOfCharging))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModeOfCharging in the database
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllModeOfChargings() throws Exception {
        // Initialize the database
        modeOfChargingRepository.saveAndFlush(modeOfCharging);

        // Get all the modeOfChargingList
        restModeOfChargingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modeOfCharging.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getModeOfCharging() throws Exception {
        // Initialize the database
        modeOfChargingRepository.saveAndFlush(modeOfCharging);

        // Get the modeOfCharging
        restModeOfChargingMockMvc
            .perform(get(ENTITY_API_URL_ID, modeOfCharging.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(modeOfCharging.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingModeOfCharging() throws Exception {
        // Get the modeOfCharging
        restModeOfChargingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingModeOfCharging() throws Exception {
        // Initialize the database
        modeOfChargingRepository.saveAndFlush(modeOfCharging);

        int databaseSizeBeforeUpdate = modeOfChargingRepository.findAll().size();

        // Update the modeOfCharging
        ModeOfCharging updatedModeOfCharging = modeOfChargingRepository.findById(modeOfCharging.getId()).get();
        // Disconnect from session so that the updates on updatedModeOfCharging are not directly saved in db
        em.detach(updatedModeOfCharging);
        updatedModeOfCharging.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restModeOfChargingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedModeOfCharging.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedModeOfCharging))
            )
            .andExpect(status().isOk());

        // Validate the ModeOfCharging in the database
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeUpdate);
        ModeOfCharging testModeOfCharging = modeOfChargingList.get(modeOfChargingList.size() - 1);
        assertThat(testModeOfCharging.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testModeOfCharging.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingModeOfCharging() throws Exception {
        int databaseSizeBeforeUpdate = modeOfChargingRepository.findAll().size();
        modeOfCharging.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModeOfChargingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modeOfCharging.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(modeOfCharging))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModeOfCharging in the database
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModeOfCharging() throws Exception {
        int databaseSizeBeforeUpdate = modeOfChargingRepository.findAll().size();
        modeOfCharging.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeOfChargingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(modeOfCharging))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModeOfCharging in the database
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModeOfCharging() throws Exception {
        int databaseSizeBeforeUpdate = modeOfChargingRepository.findAll().size();
        modeOfCharging.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeOfChargingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modeOfCharging)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ModeOfCharging in the database
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModeOfChargingWithPatch() throws Exception {
        // Initialize the database
        modeOfChargingRepository.saveAndFlush(modeOfCharging);

        int databaseSizeBeforeUpdate = modeOfChargingRepository.findAll().size();

        // Update the modeOfCharging using partial update
        ModeOfCharging partialUpdatedModeOfCharging = new ModeOfCharging();
        partialUpdatedModeOfCharging.setId(modeOfCharging.getId());

        partialUpdatedModeOfCharging.description(UPDATED_DESCRIPTION);

        restModeOfChargingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModeOfCharging.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModeOfCharging))
            )
            .andExpect(status().isOk());

        // Validate the ModeOfCharging in the database
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeUpdate);
        ModeOfCharging testModeOfCharging = modeOfChargingList.get(modeOfChargingList.size() - 1);
        assertThat(testModeOfCharging.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testModeOfCharging.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateModeOfChargingWithPatch() throws Exception {
        // Initialize the database
        modeOfChargingRepository.saveAndFlush(modeOfCharging);

        int databaseSizeBeforeUpdate = modeOfChargingRepository.findAll().size();

        // Update the modeOfCharging using partial update
        ModeOfCharging partialUpdatedModeOfCharging = new ModeOfCharging();
        partialUpdatedModeOfCharging.setId(modeOfCharging.getId());

        partialUpdatedModeOfCharging.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restModeOfChargingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModeOfCharging.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModeOfCharging))
            )
            .andExpect(status().isOk());

        // Validate the ModeOfCharging in the database
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeUpdate);
        ModeOfCharging testModeOfCharging = modeOfChargingList.get(modeOfChargingList.size() - 1);
        assertThat(testModeOfCharging.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testModeOfCharging.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingModeOfCharging() throws Exception {
        int databaseSizeBeforeUpdate = modeOfChargingRepository.findAll().size();
        modeOfCharging.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModeOfChargingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, modeOfCharging.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(modeOfCharging))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModeOfCharging in the database
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModeOfCharging() throws Exception {
        int databaseSizeBeforeUpdate = modeOfChargingRepository.findAll().size();
        modeOfCharging.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeOfChargingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(modeOfCharging))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModeOfCharging in the database
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModeOfCharging() throws Exception {
        int databaseSizeBeforeUpdate = modeOfChargingRepository.findAll().size();
        modeOfCharging.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModeOfChargingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(modeOfCharging))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ModeOfCharging in the database
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModeOfCharging() throws Exception {
        // Initialize the database
        modeOfChargingRepository.saveAndFlush(modeOfCharging);

        int databaseSizeBeforeDelete = modeOfChargingRepository.findAll().size();

        // Delete the modeOfCharging
        restModeOfChargingMockMvc
            .perform(delete(ENTITY_API_URL_ID, modeOfCharging.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ModeOfCharging> modeOfChargingList = modeOfChargingRepository.findAll();
        assertThat(modeOfChargingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
