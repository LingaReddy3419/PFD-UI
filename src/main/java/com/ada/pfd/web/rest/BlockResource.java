package com.ada.pfd.web.rest;

import com.ada.pfd.domain.Block;
import com.ada.pfd.repository.BlockRepository;
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
 * REST controller for managing {@link com.ada.pfd.domain.Block}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BlockResource {

    private final Logger log = LoggerFactory.getLogger(BlockResource.class);

    private static final String ENTITY_NAME = "block";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlockRepository blockRepository;

    public BlockResource(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    /**
     * {@code POST  /blocks} : Create a new block.
     *
     * @param block the block to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new block, or with status {@code 400 (Bad Request)} if the block has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/blocks")
    public ResponseEntity<Block> createBlock(@RequestBody Block block) throws URISyntaxException {
        log.debug("REST request to save Block : {}", block);
        if (block.getId() != null) {
            throw new BadRequestAlertException("A new block cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Block result = blockRepository.save(block);
        return ResponseEntity
            .created(new URI("/api/blocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /blocks/:id} : Updates an existing block.
     *
     * @param id the id of the block to save.
     * @param block the block to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated block,
     * or with status {@code 400 (Bad Request)} if the block is not valid,
     * or with status {@code 500 (Internal Server Error)} if the block couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/blocks/{id}")
    public ResponseEntity<Block> updateBlock(@PathVariable(value = "id", required = false) final Long id, @RequestBody Block block)
        throws URISyntaxException {
        log.debug("REST request to update Block : {}, {}", id, block);
        if (block.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, block.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Block result = blockRepository.save(block);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, block.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /blocks/:id} : Partial updates given fields of an existing block, field will ignore if it is null
     *
     * @param id the id of the block to save.
     * @param block the block to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated block,
     * or with status {@code 400 (Bad Request)} if the block is not valid,
     * or with status {@code 404 (Not Found)} if the block is not found,
     * or with status {@code 500 (Internal Server Error)} if the block couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/blocks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Block> partialUpdateBlock(@PathVariable(value = "id", required = false) final Long id, @RequestBody Block block)
        throws URISyntaxException {
        log.debug("REST request to partial update Block partially : {}, {}", id, block);
        if (block.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, block.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Block> result = blockRepository
            .findById(block.getId())
            .map(existingBlock -> {
                if (block.getTitle() != null) {
                    existingBlock.setTitle(block.getTitle());
                }
                if (block.getDescription() != null) {
                    existingBlock.setDescription(block.getDescription());
                }

                return existingBlock;
            })
            .map(blockRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, block.getId().toString())
        );
    }

    /**
     * {@code GET  /blocks} : get all the blocks.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blocks in body.
     */
    @GetMapping("/blocks")
    public List<Block> getAllBlocks(@RequestParam(required = false) String filter) {
        if ("reactor-is-null".equals(filter)) {
            log.debug("REST request to get all Blocks where reactor is null");
            return StreamSupport
                .stream(blockRepository.findAll().spliterator(), false)
                .filter(block -> block.getReactor() == null)
                .toList();
        }
        log.debug("REST request to get all Blocks");
        return blockRepository.findAll();
    }

    /**
     * {@code GET  /blocks/:id} : get the "id" block.
     *
     * @param id the id of the block to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the block, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/blocks/{id}")
    public ResponseEntity<Block> getBlock(@PathVariable Long id) {
        log.debug("REST request to get Block : {}", id);
        Optional<Block> block = blockRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(block);
    }

    /**
     * {@code DELETE  /blocks/:id} : delete the "id" block.
     *
     * @param id the id of the block to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/blocks/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        log.debug("REST request to delete Block : {}", id);
        blockRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
