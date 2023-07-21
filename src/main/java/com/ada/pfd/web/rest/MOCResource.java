package com.ada.pfd.web.rest;

import com.ada.pfd.domain.MOC;
import com.ada.pfd.repository.MOCRepository;
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
 * REST controller for managing {@link com.ada.pfd.domain.MOC}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MOCResource {

    private final Logger log = LoggerFactory.getLogger(MOCResource.class);

    private static final String ENTITY_NAME = "mOC";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MOCRepository mOCRepository;

    public MOCResource(MOCRepository mOCRepository) {
        this.mOCRepository = mOCRepository;
    }

    /**
     * {@code POST  /mocs} : Create a new mOC.
     *
     * @param mOC the mOC to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mOC, or with status {@code 400 (Bad Request)} if the mOC has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mocs")
    public ResponseEntity<MOC> createMOC(@RequestBody MOC mOC) throws URISyntaxException {
        log.debug("REST request to save MOC : {}", mOC);
        if (mOC.getId() != null) {
            throw new BadRequestAlertException("A new mOC cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MOC result = mOCRepository.save(mOC);
        return ResponseEntity
            .created(new URI("/api/mocs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mocs/:id} : Updates an existing mOC.
     *
     * @param id the id of the mOC to save.
     * @param mOC the mOC to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mOC,
     * or with status {@code 400 (Bad Request)} if the mOC is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mOC couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mocs/{id}")
    public ResponseEntity<MOC> updateMOC(@PathVariable(value = "id", required = false) final Long id, @RequestBody MOC mOC)
        throws URISyntaxException {
        log.debug("REST request to update MOC : {}, {}", id, mOC);
        if (mOC.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mOC.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mOCRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MOC result = mOCRepository.save(mOC);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mOC.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mocs/:id} : Partial updates given fields of an existing mOC, field will ignore if it is null
     *
     * @param id the id of the mOC to save.
     * @param mOC the mOC to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mOC,
     * or with status {@code 400 (Bad Request)} if the mOC is not valid,
     * or with status {@code 404 (Not Found)} if the mOC is not found,
     * or with status {@code 500 (Internal Server Error)} if the mOC couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mocs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MOC> partialUpdateMOC(@PathVariable(value = "id", required = false) final Long id, @RequestBody MOC mOC)
        throws URISyntaxException {
        log.debug("REST request to partial update MOC partially : {}, {}", id, mOC);
        if (mOC.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mOC.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mOCRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MOC> result = mOCRepository
            .findById(mOC.getId())
            .map(existingMOC -> {
                if (mOC.getTitle() != null) {
                    existingMOC.setTitle(mOC.getTitle());
                }
                if (mOC.getDescription() != null) {
                    existingMOC.setDescription(mOC.getDescription());
                }

                return existingMOC;
            })
            .map(mOCRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mOC.getId().toString())
        );
    }

    /**
     * {@code GET  /mocs} : get all the mOCS.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mOCS in body.
     */
    @GetMapping("/mocs")
    public List<MOC> getAllMOCS(@RequestParam(required = false) String filter) {
        if ("reactor-is-null".equals(filter)) {
            log.debug("REST request to get all MOCs where reactor is null");
            return StreamSupport.stream(mOCRepository.findAll().spliterator(), false).filter(mOC -> mOC.getReactor() == null).toList();
        }
        log.debug("REST request to get all MOCS");
        return mOCRepository.findAll();
    }

    /**
     * {@code GET  /mocs/:id} : get the "id" mOC.
     *
     * @param id the id of the mOC to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mOC, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mocs/{id}")
    public ResponseEntity<MOC> getMOC(@PathVariable Long id) {
        log.debug("REST request to get MOC : {}", id);
        Optional<MOC> mOC = mOCRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mOC);
    }

    /**
     * {@code DELETE  /mocs/:id} : delete the "id" mOC.
     *
     * @param id the id of the mOC to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mocs/{id}")
    public ResponseEntity<Void> deleteMOC(@PathVariable Long id) {
        log.debug("REST request to delete MOC : {}", id);
        mOCRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
