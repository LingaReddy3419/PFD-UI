package com.ada.pfd.web.rest;

import static com.ada.pfd.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ada.pfd.IntegrationTest;
import com.ada.pfd.domain.Reactor;
import com.ada.pfd.repository.ReactorRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ReactorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReactorResourceIT {

    private static final BigDecimal DEFAULT_WORKING_VOLUME = new BigDecimal(1);
    private static final BigDecimal UPDATED_WORKING_VOLUME = new BigDecimal(2);

    private static final String DEFAULT_VESSEL_ID = "AAAAAAAAAA";
    private static final String UPDATED_VESSEL_ID = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BOTTOM_IMPELLER_STIRRING_VOLUME = new BigDecimal(1);
    private static final BigDecimal UPDATED_BOTTOM_IMPELLER_STIRRING_VOLUME = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MINIMUM_TEMP_SENSING_VOLUME = new BigDecimal(1);
    private static final BigDecimal UPDATED_MINIMUM_TEMP_SENSING_VOLUME = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/reactors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReactorRepository reactorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReactorMockMvc;

    private Reactor reactor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reactor createEntity(EntityManager em) {
        Reactor reactor = new Reactor()
            .workingVolume(DEFAULT_WORKING_VOLUME)
            .vesselId(DEFAULT_VESSEL_ID)
            .bottomImpellerStirringVolume(DEFAULT_BOTTOM_IMPELLER_STIRRING_VOLUME)
            .minimumTempSensingVolume(DEFAULT_MINIMUM_TEMP_SENSING_VOLUME);
        return reactor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reactor createUpdatedEntity(EntityManager em) {
        Reactor reactor = new Reactor()
            .workingVolume(UPDATED_WORKING_VOLUME)
            .vesselId(UPDATED_VESSEL_ID)
            .bottomImpellerStirringVolume(UPDATED_BOTTOM_IMPELLER_STIRRING_VOLUME)
            .minimumTempSensingVolume(UPDATED_MINIMUM_TEMP_SENSING_VOLUME);
        return reactor;
    }

    @BeforeEach
    public void initTest() {
        reactor = createEntity(em);
    }

    @Test
    @Transactional
    void createReactor() throws Exception {
        int databaseSizeBeforeCreate = reactorRepository.findAll().size();
        // Create the Reactor
        restReactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reactor)))
            .andExpect(status().isCreated());

        // Validate the Reactor in the database
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeCreate + 1);
        Reactor testReactor = reactorList.get(reactorList.size() - 1);
        assertThat(testReactor.getWorkingVolume()).isEqualByComparingTo(DEFAULT_WORKING_VOLUME);
        assertThat(testReactor.getVesselId()).isEqualTo(DEFAULT_VESSEL_ID);
        assertThat(testReactor.getBottomImpellerStirringVolume()).isEqualByComparingTo(DEFAULT_BOTTOM_IMPELLER_STIRRING_VOLUME);
        assertThat(testReactor.getMinimumTempSensingVolume()).isEqualByComparingTo(DEFAULT_MINIMUM_TEMP_SENSING_VOLUME);
    }

    @Test
    @Transactional
    void createReactorWithExistingId() throws Exception {
        // Create the Reactor with an existing ID
        reactor.setId(1L);

        int databaseSizeBeforeCreate = reactorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reactor)))
            .andExpect(status().isBadRequest());

        // Validate the Reactor in the database
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReactors() throws Exception {
        // Initialize the database
        reactorRepository.saveAndFlush(reactor);

        // Get all the reactorList
        restReactorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reactor.getId().intValue())))
            .andExpect(jsonPath("$.[*].workingVolume").value(hasItem(sameNumber(DEFAULT_WORKING_VOLUME))))
            .andExpect(jsonPath("$.[*].vesselId").value(hasItem(DEFAULT_VESSEL_ID)))
            .andExpect(jsonPath("$.[*].bottomImpellerStirringVolume").value(hasItem(sameNumber(DEFAULT_BOTTOM_IMPELLER_STIRRING_VOLUME))))
            .andExpect(jsonPath("$.[*].minimumTempSensingVolume").value(hasItem(sameNumber(DEFAULT_MINIMUM_TEMP_SENSING_VOLUME))));
    }

    @Test
    @Transactional
    void getReactor() throws Exception {
        // Initialize the database
        reactorRepository.saveAndFlush(reactor);

        // Get the reactor
        restReactorMockMvc
            .perform(get(ENTITY_API_URL_ID, reactor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reactor.getId().intValue()))
            .andExpect(jsonPath("$.workingVolume").value(sameNumber(DEFAULT_WORKING_VOLUME)))
            .andExpect(jsonPath("$.vesselId").value(DEFAULT_VESSEL_ID))
            .andExpect(jsonPath("$.bottomImpellerStirringVolume").value(sameNumber(DEFAULT_BOTTOM_IMPELLER_STIRRING_VOLUME)))
            .andExpect(jsonPath("$.minimumTempSensingVolume").value(sameNumber(DEFAULT_MINIMUM_TEMP_SENSING_VOLUME)));
    }

    @Test
    @Transactional
    void getNonExistingReactor() throws Exception {
        // Get the reactor
        restReactorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReactor() throws Exception {
        // Initialize the database
        reactorRepository.saveAndFlush(reactor);

        int databaseSizeBeforeUpdate = reactorRepository.findAll().size();

        // Update the reactor
        Reactor updatedReactor = reactorRepository.findById(reactor.getId()).get();
        // Disconnect from session so that the updates on updatedReactor are not directly saved in db
        em.detach(updatedReactor);
        updatedReactor
            .workingVolume(UPDATED_WORKING_VOLUME)
            .vesselId(UPDATED_VESSEL_ID)
            .bottomImpellerStirringVolume(UPDATED_BOTTOM_IMPELLER_STIRRING_VOLUME)
            .minimumTempSensingVolume(UPDATED_MINIMUM_TEMP_SENSING_VOLUME);

        restReactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReactor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReactor))
            )
            .andExpect(status().isOk());

        // Validate the Reactor in the database
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeUpdate);
        Reactor testReactor = reactorList.get(reactorList.size() - 1);
        assertThat(testReactor.getWorkingVolume()).isEqualByComparingTo(UPDATED_WORKING_VOLUME);
        assertThat(testReactor.getVesselId()).isEqualTo(UPDATED_VESSEL_ID);
        assertThat(testReactor.getBottomImpellerStirringVolume()).isEqualByComparingTo(UPDATED_BOTTOM_IMPELLER_STIRRING_VOLUME);
        assertThat(testReactor.getMinimumTempSensingVolume()).isEqualByComparingTo(UPDATED_MINIMUM_TEMP_SENSING_VOLUME);
    }

    @Test
    @Transactional
    void putNonExistingReactor() throws Exception {
        int databaseSizeBeforeUpdate = reactorRepository.findAll().size();
        reactor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reactor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reactor in the database
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReactor() throws Exception {
        int databaseSizeBeforeUpdate = reactorRepository.findAll().size();
        reactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reactor in the database
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReactor() throws Exception {
        int databaseSizeBeforeUpdate = reactorRepository.findAll().size();
        reactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reactor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reactor in the database
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReactorWithPatch() throws Exception {
        // Initialize the database
        reactorRepository.saveAndFlush(reactor);

        int databaseSizeBeforeUpdate = reactorRepository.findAll().size();

        // Update the reactor using partial update
        Reactor partialUpdatedReactor = new Reactor();
        partialUpdatedReactor.setId(reactor.getId());

        partialUpdatedReactor.minimumTempSensingVolume(UPDATED_MINIMUM_TEMP_SENSING_VOLUME);

        restReactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReactor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReactor))
            )
            .andExpect(status().isOk());

        // Validate the Reactor in the database
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeUpdate);
        Reactor testReactor = reactorList.get(reactorList.size() - 1);
        assertThat(testReactor.getWorkingVolume()).isEqualByComparingTo(DEFAULT_WORKING_VOLUME);
        assertThat(testReactor.getVesselId()).isEqualTo(DEFAULT_VESSEL_ID);
        assertThat(testReactor.getBottomImpellerStirringVolume()).isEqualByComparingTo(DEFAULT_BOTTOM_IMPELLER_STIRRING_VOLUME);
        assertThat(testReactor.getMinimumTempSensingVolume()).isEqualByComparingTo(UPDATED_MINIMUM_TEMP_SENSING_VOLUME);
    }

    @Test
    @Transactional
    void fullUpdateReactorWithPatch() throws Exception {
        // Initialize the database
        reactorRepository.saveAndFlush(reactor);

        int databaseSizeBeforeUpdate = reactorRepository.findAll().size();

        // Update the reactor using partial update
        Reactor partialUpdatedReactor = new Reactor();
        partialUpdatedReactor.setId(reactor.getId());

        partialUpdatedReactor
            .workingVolume(UPDATED_WORKING_VOLUME)
            .vesselId(UPDATED_VESSEL_ID)
            .bottomImpellerStirringVolume(UPDATED_BOTTOM_IMPELLER_STIRRING_VOLUME)
            .minimumTempSensingVolume(UPDATED_MINIMUM_TEMP_SENSING_VOLUME);

        restReactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReactor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReactor))
            )
            .andExpect(status().isOk());

        // Validate the Reactor in the database
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeUpdate);
        Reactor testReactor = reactorList.get(reactorList.size() - 1);
        assertThat(testReactor.getWorkingVolume()).isEqualByComparingTo(UPDATED_WORKING_VOLUME);
        assertThat(testReactor.getVesselId()).isEqualTo(UPDATED_VESSEL_ID);
        assertThat(testReactor.getBottomImpellerStirringVolume()).isEqualByComparingTo(UPDATED_BOTTOM_IMPELLER_STIRRING_VOLUME);
        assertThat(testReactor.getMinimumTempSensingVolume()).isEqualByComparingTo(UPDATED_MINIMUM_TEMP_SENSING_VOLUME);
    }

    @Test
    @Transactional
    void patchNonExistingReactor() throws Exception {
        int databaseSizeBeforeUpdate = reactorRepository.findAll().size();
        reactor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reactor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reactor in the database
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReactor() throws Exception {
        int databaseSizeBeforeUpdate = reactorRepository.findAll().size();
        reactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reactor in the database
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReactor() throws Exception {
        int databaseSizeBeforeUpdate = reactorRepository.findAll().size();
        reactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reactor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reactor in the database
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReactor() throws Exception {
        // Initialize the database
        reactorRepository.saveAndFlush(reactor);

        int databaseSizeBeforeDelete = reactorRepository.findAll().size();

        // Delete the reactor
        restReactorMockMvc
            .perform(delete(ENTITY_API_URL_ID, reactor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reactor> reactorList = reactorRepository.findAll();
        assertThat(reactorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
