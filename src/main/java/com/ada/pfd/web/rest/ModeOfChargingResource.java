package com.ada.pfd.web.rest;

import com.ada.pfd.domain.ModeOfCharging;
import com.ada.pfd.repository.ModeOfChargingRepository;
import com.ada.pfd.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ada.pfd.domain.ModeOfCharging}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ModeOfChargingResource {

    private final Logger log = LoggerFactory.getLogger(ModeOfChargingResource.class);

    private static final String ENTITY_NAME = "modeOfCharging";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModeOfChargingRepository modeOfChargingRepository;

    public ModeOfChargingResource(ModeOfChargingRepository modeOfChargingRepository) {
        this.modeOfChargingRepository = modeOfChargingRepository;
    }

    /**
     * {@code POST  /mode-of-chargings} : Create a new modeOfCharging.
     *
     * @param modeOfCharging the modeOfCharging to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modeOfCharging, or with status {@code 400 (Bad Request)} if the modeOfCharging has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mode-of-chargings")
    public ResponseEntity<ModeOfCharging> createModeOfCharging(@RequestBody ModeOfCharging modeOfCharging) throws URISyntaxException {
        log.debug("REST request to save ModeOfCharging : {}", modeOfCharging);
        if (modeOfCharging.getId() != null) {
            throw new BadRequestAlertException("A new modeOfCharging cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ModeOfCharging result = modeOfChargingRepository.save(modeOfCharging);
        return ResponseEntity
            .created(new URI("/api/mode-of-chargings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mode-of-chargings/:id} : Updates an existing modeOfCharging.
     *
     * @param id the id of the modeOfCharging to save.
     * @param modeOfCharging the modeOfCharging to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modeOfCharging,
     * or with status {@code 400 (Bad Request)} if the modeOfCharging is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modeOfCharging couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mode-of-chargings/{id}")
    public ResponseEntity<ModeOfCharging> updateModeOfCharging(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ModeOfCharging modeOfCharging
    ) throws URISyntaxException {
        log.debug("REST request to update ModeOfCharging : {}, {}", id, modeOfCharging);
        if (modeOfCharging.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modeOfCharging.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modeOfChargingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ModeOfCharging result = modeOfChargingRepository.save(modeOfCharging);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modeOfCharging.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mode-of-chargings/:id} : Partial updates given fields of an existing modeOfCharging, field will ignore if it is null
     *
     * @param id the id of the modeOfCharging to save.
     * @param modeOfCharging the modeOfCharging to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modeOfCharging,
     * or with status {@code 400 (Bad Request)} if the modeOfCharging is not valid,
     * or with status {@code 404 (Not Found)} if the modeOfCharging is not found,
     * or with status {@code 500 (Internal Server Error)} if the modeOfCharging couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mode-of-chargings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ModeOfCharging> partialUpdateModeOfCharging(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ModeOfCharging modeOfCharging
    ) throws URISyntaxException {
        log.debug("REST request to partial update ModeOfCharging partially : {}, {}", id, modeOfCharging);
        if (modeOfCharging.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modeOfCharging.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modeOfChargingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ModeOfCharging> result = modeOfChargingRepository
            .findById(modeOfCharging.getId())
            .map(existingModeOfCharging -> {
                if (modeOfCharging.getTitle() != null) {
                    existingModeOfCharging.setTitle(modeOfCharging.getTitle());
                }
                if (modeOfCharging.getDescription() != null) {
                    existingModeOfCharging.setDescription(modeOfCharging.getDescription());
                }

                return existingModeOfCharging;
            })
            .map(modeOfChargingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modeOfCharging.getId().toString())
        );
    }

    /**
     * {@code GET  /mode-of-chargings} : get all the modeOfChargings.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modeOfChargings in body.
     */
    @GetMapping("/mode-of-chargings")
    public List<ModeOfCharging> getAllModeOfChargings(@RequestParam(required = false) String filter) {
        if ("general-is-null".equals(filter)) {
            log.debug("REST request to get all ModeOfChargings where general is null");
            return StreamSupport
                .stream(modeOfChargingRepository.findAll().spliterator(), false)
                .filter(modeOfCharging -> modeOfCharging.getGeneral() == null)
                .toList();
        }
        log.debug("REST request to get all ModeOfChargings");
        return modeOfChargingRepository.findAll();
    }

    /**
     * {@code GET  /mode-of-chargings/:id} : get the "id" modeOfCharging.
     *
     * @param id the id of the modeOfCharging to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modeOfCharging, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mode-of-chargings/{id}")
    public ResponseEntity<ModeOfCharging> getModeOfCharging(@PathVariable Long id) {
        log.debug("REST request to get ModeOfCharging : {}", id);
        Optional<ModeOfCharging> modeOfCharging = modeOfChargingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(modeOfCharging);
    }

    /**
     * {@code DELETE  /mode-of-chargings/:id} : delete the "id" modeOfCharging.
     *
     * @param id the id of the modeOfCharging to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mode-of-chargings/{id}")
    public ResponseEntity<Void> deleteModeOfCharging(@PathVariable Long id) {
        log.debug("REST request to delete ModeOfCharging : {}", id);
        modeOfChargingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
