package com.ou.ret.web.rest;

import com.ou.ret.repository.ExtraRepository;
import com.ou.ret.service.ExtraQueryService;
import com.ou.ret.service.ExtraService;
import com.ou.ret.service.criteria.ExtraCriteria;
import com.ou.ret.service.dto.ExtraDTO;
import com.ou.ret.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ou.ret.domain.Extra}.
 */
@RestController
@RequestMapping("/api")
public class ExtraResource {

    private final Logger log = LoggerFactory.getLogger(ExtraResource.class);

    private static final String ENTITY_NAME = "extra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtraService extraService;

    private final ExtraRepository extraRepository;

    private final ExtraQueryService extraQueryService;

    public ExtraResource(ExtraService extraService, ExtraRepository extraRepository, ExtraQueryService extraQueryService) {
        this.extraService = extraService;
        this.extraRepository = extraRepository;
        this.extraQueryService = extraQueryService;
    }

    /**
     * {@code POST  /extras} : Create a new extra.
     *
     * @param extraDTO the extraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extraDTO, or with status {@code 400 (Bad Request)} if the extra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/extras")
    public ResponseEntity<ExtraDTO> createExtra(@RequestBody ExtraDTO extraDTO) throws URISyntaxException {
        log.debug("REST request to save Extra : {}", extraDTO);
        if (extraDTO.getId() != null) {
            throw new BadRequestAlertException("A new extra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExtraDTO result = extraService.save(extraDTO);
        return ResponseEntity
            .created(new URI("/api/extras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /extras/:id} : Updates an existing extra.
     *
     * @param id the id of the extraDTO to save.
     * @param extraDTO the extraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extraDTO,
     * or with status {@code 400 (Bad Request)} if the extraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/extras/{id}")
    public ResponseEntity<ExtraDTO> updateExtra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExtraDTO extraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Extra : {}, {}", id, extraDTO);
        if (extraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExtraDTO result = extraService.save(extraDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, extraDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /extras/:id} : Partial updates given fields of an existing extra, field will ignore if it is null
     *
     * @param id the id of the extraDTO to save.
     * @param extraDTO the extraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extraDTO,
     * or with status {@code 400 (Bad Request)} if the extraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the extraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the extraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/extras/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExtraDTO> partialUpdateExtra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExtraDTO extraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Extra partially : {}, {}", id, extraDTO);
        if (extraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExtraDTO> result = extraService.partialUpdate(extraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, extraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /extras} : get all the extras.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extras in body.
     */
    @GetMapping("/extras")
    public ResponseEntity<List<ExtraDTO>> getAllExtras(ExtraCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Extras by criteria: {}", criteria);
        Page<ExtraDTO> page = extraQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /extras/count} : count all the extras.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/extras/count")
    public ResponseEntity<Long> countExtras(ExtraCriteria criteria) {
        log.debug("REST request to count Extras by criteria: {}", criteria);
        return ResponseEntity.ok().body(extraQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /extras/:id} : get the "id" extra.
     *
     * @param id the id of the extraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/extras/{id}")
    public ResponseEntity<ExtraDTO> getExtra(@PathVariable Long id) {
        log.debug("REST request to get Extra : {}", id);
        Optional<ExtraDTO> extraDTO = extraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(extraDTO);
    }

    /**
     * {@code DELETE  /extras/:id} : delete the "id" extra.
     *
     * @param id the id of the extraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/extras/{id}")
    public ResponseEntity<Void> deleteExtra(@PathVariable Long id) {
        log.debug("REST request to delete Extra : {}", id);
        extraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
