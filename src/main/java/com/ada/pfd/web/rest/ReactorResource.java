package com.ada.pfd.web.rest;

import com.ada.pfd.domain.Reactor;
import com.ada.pfd.repository.*;
import com.ada.pfd.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ada.pfd.domain.Reactor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReactorResource {

    private final Logger log = LoggerFactory.getLogger(ReactorResource.class);

    private static final String ENTITY_NAME = "reactor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReactorRepository reactorRepository;

    private final UnitRepository unitRepository;
    private final MOCRepository mocRepository;
    private final BlockRepository blockRepository;
    private final ImpellerTypeRepository impellerTypeRepository;

    public ReactorResource(
        ReactorRepository reactorRepository,
        UnitRepository unitRepository,
        MOCRepository mocRepository,
        BlockRepository blockRepository,
        ImpellerTypeRepository impellerTypeRepository
    ) {
        this.reactorRepository = reactorRepository;
        this.unitRepository = unitRepository;
        this.mocRepository = mocRepository;
        this.blockRepository = blockRepository;
        this.impellerTypeRepository = impellerTypeRepository;
    }

    /**
     * {@code POST  /reactors} : Create a new reactor.
     *
     * @param reactor the reactor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reactor, or with status {@code 400 (Bad Request)} if the reactor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reactors")
    public ResponseEntity<Reactor> createReactor(@RequestBody Reactor reactor) throws URISyntaxException {
        log.debug("REST request to save Reactor : {}", reactor);
        if (reactor.getId() != null) {
            throw new BadRequestAlertException("A new reactor cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Reactor result = reactorRepository.save(reactor);
        return ResponseEntity
            .created(new URI("/api/reactors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reactors/:id} : Updates an existing reactor.
     *
     * @param id the id of the reactor to save.
     * @param reactor the reactor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reactor,
     * or with status {@code 400 (Bad Request)} if the reactor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reactor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reactors/{id}")
    public ResponseEntity<Reactor> updateReactor(@PathVariable(value = "id", required = false) final Long id, @RequestBody Reactor reactor)
        throws URISyntaxException {
        log.debug("REST request to update Reactor : {}, {}", id, reactor);
        if (reactor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reactor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reactorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Reactor result = reactorRepository.save(reactor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reactor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reactors/:id} : Partial updates given fields of an existing reactor, field will ignore if it is null
     *
     * @param id the id of the reactor to save.
     * @param reactor the reactor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reactor,
     * or with status {@code 400 (Bad Request)} if the reactor is not valid,
     * or with status {@code 404 (Not Found)} if the reactor is not found,
     * or with status {@code 500 (Internal Server Error)} if the reactor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reactors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Reactor> partialUpdateReactor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Reactor reactor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reactor partially : {}, {}", id, reactor);
        if (reactor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reactor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reactorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Reactor> result = reactorRepository
            .findById(reactor.getId())
            .map(existingReactor -> {
                if (reactor.getWorkingVolume() != null) {
                    existingReactor.setWorkingVolume(reactor.getWorkingVolume());
                }
                if (reactor.getVesselId() != null) {
                    existingReactor.setVesselId(reactor.getVesselId());
                }
                if (reactor.getBottomImpellerStirringVolume() != null) {
                    existingReactor.setBottomImpellerStirringVolume(reactor.getBottomImpellerStirringVolume());
                }
                if (reactor.getMinimumTempSensingVolume() != null) {
                    existingReactor.setMinimumTempSensingVolume(reactor.getMinimumTempSensingVolume());
                }

                return existingReactor;
            })
            .map(reactorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reactor.getId().toString())
        );
    }

    /**
     * {@code GET  /reactors} : get all the reactors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reactors in body.
     */
    @GetMapping("/reactors")
    public ResponseEntity<List<Reactor>> getAllReactors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Reactors");
        Page<Reactor> page = reactorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reactors/:id} : get the "id" reactor.
     *
     * @param id the id of the reactor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reactor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reactors/{id}")
    public ResponseEntity<Reactor> getReactor(@PathVariable Long id) {
        log.debug("REST request to get Reactor : {}", id);
        Optional<Reactor> reactor = reactorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reactor);
    }

    /**
     * {@code DELETE  /reactors/:id} : delete the "id" reactor.
     *
     * @param id the id of the reactor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reactors/{id}")
    public ResponseEntity<Void> deleteReactor(@PathVariable Long id) {
        log.debug("REST request to delete Reactor : {}", id);
        reactorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
