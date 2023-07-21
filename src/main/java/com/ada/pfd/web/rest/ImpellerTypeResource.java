package com.ada.pfd.web.rest;

import com.ada.pfd.domain.ImpellerType;
import com.ada.pfd.repository.ImpellerTypeRepository;
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
 * REST controller for managing {@link com.ada.pfd.domain.ImpellerType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ImpellerTypeResource {

    private final Logger log = LoggerFactory.getLogger(ImpellerTypeResource.class);

    private static final String ENTITY_NAME = "impellerType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImpellerTypeRepository impellerTypeRepository;

    public ImpellerTypeResource(ImpellerTypeRepository impellerTypeRepository) {
        this.impellerTypeRepository = impellerTypeRepository;
    }

    /**
     * {@code POST  /impeller-types} : Create a new impellerType.
     *
     * @param impellerType the impellerType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new impellerType, or with status {@code 400 (Bad Request)} if the impellerType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/impeller-types")
    public ResponseEntity<ImpellerType> createImpellerType(@RequestBody ImpellerType impellerType) throws URISyntaxException {
        log.debug("REST request to save ImpellerType : {}", impellerType);
        if (impellerType.getId() != null) {
            throw new BadRequestAlertException("A new impellerType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImpellerType result = impellerTypeRepository.save(impellerType);
        return ResponseEntity
            .created(new URI("/api/impeller-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /impeller-types/:id} : Updates an existing impellerType.
     *
     * @param id the id of the impellerType to save.
     * @param impellerType the impellerType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated impellerType,
     * or with status {@code 400 (Bad Request)} if the impellerType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the impellerType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/impeller-types/{id}")
    public ResponseEntity<ImpellerType> updateImpellerType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImpellerType impellerType
    ) throws URISyntaxException {
        log.debug("REST request to update ImpellerType : {}, {}", id, impellerType);
        if (impellerType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, impellerType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!impellerTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImpellerType result = impellerTypeRepository.save(impellerType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, impellerType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /impeller-types/:id} : Partial updates given fields of an existing impellerType, field will ignore if it is null
     *
     * @param id the id of the impellerType to save.
     * @param impellerType the impellerType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated impellerType,
     * or with status {@code 400 (Bad Request)} if the impellerType is not valid,
     * or with status {@code 404 (Not Found)} if the impellerType is not found,
     * or with status {@code 500 (Internal Server Error)} if the impellerType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/impeller-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImpellerType> partialUpdateImpellerType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImpellerType impellerType
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImpellerType partially : {}, {}", id, impellerType);
        if (impellerType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, impellerType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!impellerTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImpellerType> result = impellerTypeRepository
            .findById(impellerType.getId())
            .map(existingImpellerType -> {
                if (impellerType.getTitle() != null) {
                    existingImpellerType.setTitle(impellerType.getTitle());
                }
                if (impellerType.getDescription() != null) {
                    existingImpellerType.setDescription(impellerType.getDescription());
                }

                return existingImpellerType;
            })
            .map(impellerTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, impellerType.getId().toString())
        );
    }

    /**
     * {@code GET  /impeller-types} : get all the impellerTypes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of impellerTypes in body.
     */
    @GetMapping("/impeller-types")
    public List<ImpellerType> getAllImpellerTypes(@RequestParam(required = false) String filter) {
        if ("reactor-is-null".equals(filter)) {
            log.debug("REST request to get all ImpellerTypes where reactor is null");
            return StreamSupport
                .stream(impellerTypeRepository.findAll().spliterator(), false)
                .filter(impellerType -> impellerType.getReactor() == null)
                .toList();
        }
        log.debug("REST request to get all ImpellerTypes");
        return impellerTypeRepository.findAll();
    }

    /**
     * {@code GET  /impeller-types/:id} : get the "id" impellerType.
     *
     * @param id the id of the impellerType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the impellerType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/impeller-types/{id}")
    public ResponseEntity<ImpellerType> getImpellerType(@PathVariable Long id) {
        log.debug("REST request to get ImpellerType : {}", id);
        Optional<ImpellerType> impellerType = impellerTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(impellerType);
    }

    /**
     * {@code DELETE  /impeller-types/:id} : delete the "id" impellerType.
     *
     * @param id the id of the impellerType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/impeller-types/{id}")
    public ResponseEntity<Void> deleteImpellerType(@PathVariable Long id) {
        log.debug("REST request to delete ImpellerType : {}", id);
        impellerTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
