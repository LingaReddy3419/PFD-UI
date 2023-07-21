package com.ada.pfd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ada.pfd.IntegrationTest;
import com.ada.pfd.domain.MOC;
import com.ada.pfd.repository.MOCRepository;
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
 * Integration tests for the {@link MOCResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MOCResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mocs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MOCRepository mOCRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMOCMockMvc;

    private MOC mOC;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MOC createEntity(EntityManager em) {
        MOC mOC = new MOC().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return mOC;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MOC createUpdatedEntity(EntityManager em) {
        MOC mOC = new MOC().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return mOC;
    }

    @BeforeEach
    public void initTest() {
        mOC = createEntity(em);
    }

    @Test
    @Transactional
    void createMOC() throws Exception {
        int databaseSizeBeforeCreate = mOCRepository.findAll().size();
        // Create the MOC
        restMOCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mOC)))
            .andExpect(status().isCreated());

        // Validate the MOC in the database
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeCreate + 1);
        MOC testMOC = mOCList.get(mOCList.size() - 1);
        assertThat(testMOC.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMOC.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createMOCWithExistingId() throws Exception {
        // Create the MOC with an existing ID
        mOC.setId(1L);

        int databaseSizeBeforeCreate = mOCRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMOCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mOC)))
            .andExpect(status().isBadRequest());

        // Validate the MOC in the database
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMOCS() throws Exception {
        // Initialize the database
        mOCRepository.saveAndFlush(mOC);

        // Get all the mOCList
        restMOCMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mOC.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getMOC() throws Exception {
        // Initialize the database
        mOCRepository.saveAndFlush(mOC);

        // Get the mOC
        restMOCMockMvc
            .perform(get(ENTITY_API_URL_ID, mOC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mOC.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingMOC() throws Exception {
        // Get the mOC
        restMOCMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMOC() throws Exception {
        // Initialize the database
        mOCRepository.saveAndFlush(mOC);

        int databaseSizeBeforeUpdate = mOCRepository.findAll().size();

        // Update the mOC
        MOC updatedMOC = mOCRepository.findById(mOC.getId()).get();
        // Disconnect from session so that the updates on updatedMOC are not directly saved in db
        em.detach(updatedMOC);
        updatedMOC.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restMOCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMOC.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMOC))
            )
            .andExpect(status().isOk());

        // Validate the MOC in the database
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeUpdate);
        MOC testMOC = mOCList.get(mOCList.size() - 1);
        assertThat(testMOC.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMOC.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingMOC() throws Exception {
        int databaseSizeBeforeUpdate = mOCRepository.findAll().size();
        mOC.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMOCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mOC.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mOC))
            )
            .andExpect(status().isBadRequest());

        // Validate the MOC in the database
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMOC() throws Exception {
        int databaseSizeBeforeUpdate = mOCRepository.findAll().size();
        mOC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMOCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mOC))
            )
            .andExpect(status().isBadRequest());

        // Validate the MOC in the database
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMOC() throws Exception {
        int databaseSizeBeforeUpdate = mOCRepository.findAll().size();
        mOC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMOCMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mOC)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MOC in the database
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMOCWithPatch() throws Exception {
        // Initialize the database
        mOCRepository.saveAndFlush(mOC);

        int databaseSizeBeforeUpdate = mOCRepository.findAll().size();

        // Update the mOC using partial update
        MOC partialUpdatedMOC = new MOC();
        partialUpdatedMOC.setId(mOC.getId());

        partialUpdatedMOC.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restMOCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMOC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMOC))
            )
            .andExpect(status().isOk());

        // Validate the MOC in the database
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeUpdate);
        MOC testMOC = mOCList.get(mOCList.size() - 1);
        assertThat(testMOC.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMOC.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateMOCWithPatch() throws Exception {
        // Initialize the database
        mOCRepository.saveAndFlush(mOC);

        int databaseSizeBeforeUpdate = mOCRepository.findAll().size();

        // Update the mOC using partial update
        MOC partialUpdatedMOC = new MOC();
        partialUpdatedMOC.setId(mOC.getId());

        partialUpdatedMOC.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restMOCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMOC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMOC))
            )
            .andExpect(status().isOk());

        // Validate the MOC in the database
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeUpdate);
        MOC testMOC = mOCList.get(mOCList.size() - 1);
        assertThat(testMOC.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMOC.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingMOC() throws Exception {
        int databaseSizeBeforeUpdate = mOCRepository.findAll().size();
        mOC.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMOCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mOC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mOC))
            )
            .andExpect(status().isBadRequest());

        // Validate the MOC in the database
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMOC() throws Exception {
        int databaseSizeBeforeUpdate = mOCRepository.findAll().size();
        mOC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMOCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mOC))
            )
            .andExpect(status().isBadRequest());

        // Validate the MOC in the database
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMOC() throws Exception {
        int databaseSizeBeforeUpdate = mOCRepository.findAll().size();
        mOC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMOCMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mOC)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MOC in the database
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMOC() throws Exception {
        // Initialize the database
        mOCRepository.saveAndFlush(mOC);

        int databaseSizeBeforeDelete = mOCRepository.findAll().size();

        // Delete the mOC
        restMOCMockMvc.perform(delete(ENTITY_API_URL_ID, mOC.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MOC> mOCList = mOCRepository.findAll();
        assertThat(mOCList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
