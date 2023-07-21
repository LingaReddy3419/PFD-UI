package com.ada.pfd.web.rest;

import com.ada.pfd.domain.Operations;
import com.ada.pfd.repository.OperationsRepository;
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
 * REST controller for managing {@link com.ada.pfd.domain.Operations}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OperationsResource {

    private final Logger log = LoggerFactory.getLogger(OperationsResource.class);

    private static final String ENTITY_NAME = "operations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperationsRepository operationsRepository;

    public OperationsResource(OperationsRepository operationsRepository) {
        this.operationsRepository = operationsRepository;
    }

    /**
     * {@code POST  /operations} : Create a new operations.
     *
     * @param operations the operations to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operations, or with status {@code 400 (Bad Request)} if the operations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operations")
    public ResponseEntity<Operations> createOperations(@RequestBody Operations operations) throws URISyntaxException {
        log.debug("REST request to save Operations : {}", operations);
        if (operations.getId() != null) {
            throw new BadRequestAlertException("A new operations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Operations result = operationsRepository.save(operations);
        return ResponseEntity
            .created(new URI("/api/operations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operations/:id} : Updates an existing operations.
     *
     * @param id the id of the operations to save.
     * @param operations the operations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operations,
     * or with status {@code 400 (Bad Request)} if the operations is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operations/{id}")
    public ResponseEntity<Operations> updateOperations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Operations operations
    ) throws URISyntaxException {
        log.debug("REST request to update Operations : {}, {}", id, operations);
        if (operations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Operations result = operationsRepository.save(operations);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operations.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /operations/:id} : Partial updates given fields of an existing operations, field will ignore if it is null
     *
     * @param id the id of the operations to save.
     * @param operations the operations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operations,
     * or with status {@code 400 (Bad Request)} if the operations is not valid,
     * or with status {@code 404 (Not Found)} if the operations is not found,
     * or with status {@code 500 (Internal Server Error)} if the operations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/operations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Operations> partialUpdateOperations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Operations operations
    ) throws URISyntaxException {
        log.debug("REST request to partial update Operations partially : {}, {}", id, operations);
        if (operations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operations.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Operations> result = operationsRepository
            .findById(operations.getId())
            .map(existingOperations -> {
                if (operations.getTitle() != null) {
                    existingOperations.setTitle(operations.getTitle());
                }
                if (operations.getDescription() != null) {
                    existingOperations.setDescription(operations.getDescription());
                }

                return existingOperations;
            })
            .map(operationsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operations.getId().toString())
        );
    }

    /**
     * {@code GET  /operations} : get all the operations.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operations in body.
     */
    @GetMapping("/operations")
    public List<Operations> getAllOperations(@RequestParam(required = false) String filter) {
        if ("general-is-null".equals(filter)) {
            log.debug("REST request to get all Operationss where general is null");
            return StreamSupport
                .stream(operationsRepository.findAll().spliterator(), false)
                .filter(operations -> operations.getGeneral() == null)
                .toList();
        }
        log.debug("REST request to get all Operations");
        return operationsRepository.findAll();
    }

    /**
     * {@code GET  /operations/:id} : get the "id" operations.
     *
     * @param id the id of the operations to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operations, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operations/{id}")
    public ResponseEntity<Operations> getOperations(@PathVariable Long id) {
        log.debug("REST request to get Operations : {}", id);
        Optional<Operations> operations = operationsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(operations);
    }

    /**
     * {@code DELETE  /operations/:id} : delete the "id" operations.
     *
     * @param id the id of the operations to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operations/{id}")
    public ResponseEntity<Void> deleteOperations(@PathVariable Long id) {
        log.debug("REST request to delete Operations : {}", id);
        operationsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
