package com.ou.ret.web.rest;

import com.ou.ret.repository.HomeTypeRepository;
import com.ou.ret.service.HomeTypeQueryService;
import com.ou.ret.service.HomeTypeService;
import com.ou.ret.service.criteria.HomeTypeCriteria;
import com.ou.ret.service.dto.HomeTypeDTO;
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
 * REST controller for managing {@link com.ou.ret.domain.HomeType}.
 */
@RestController
@RequestMapping("/api")
public class HomeTypeResource {

    private final Logger log = LoggerFactory.getLogger(HomeTypeResource.class);

    private static final String ENTITY_NAME = "homeType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HomeTypeService homeTypeService;

    private final HomeTypeRepository homeTypeRepository;

    private final HomeTypeQueryService homeTypeQueryService;

    public HomeTypeResource(
        HomeTypeService homeTypeService,
        HomeTypeRepository homeTypeRepository,
        HomeTypeQueryService homeTypeQueryService
    ) {
        this.homeTypeService = homeTypeService;
        this.homeTypeRepository = homeTypeRepository;
        this.homeTypeQueryService = homeTypeQueryService;
    }

    /**
     * {@code POST  /home-types} : Create a new homeType.
     *
     * @param homeTypeDTO the homeTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new homeTypeDTO, or with status {@code 400 (Bad Request)} if the homeType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/home-types")
    public ResponseEntity<HomeTypeDTO> createHomeType(@RequestBody HomeTypeDTO homeTypeDTO) throws URISyntaxException {
        log.debug("REST request to save HomeType : {}", homeTypeDTO);
        if (homeTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new homeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HomeTypeDTO result = homeTypeService.save(homeTypeDTO);
        return ResponseEntity
            .created(new URI("/api/home-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /home-types/:id} : Updates an existing homeType.
     *
     * @param id the id of the homeTypeDTO to save.
     * @param homeTypeDTO the homeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated homeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the homeTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the homeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/home-types/{id}")
    public ResponseEntity<HomeTypeDTO> updateHomeType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HomeTypeDTO homeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HomeType : {}, {}", id, homeTypeDTO);
        if (homeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, homeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!homeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HomeTypeDTO result = homeTypeService.save(homeTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, homeTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /home-types/:id} : Partial updates given fields of an existing homeType, field will ignore if it is null
     *
     * @param id the id of the homeTypeDTO to save.
     * @param homeTypeDTO the homeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated homeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the homeTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the homeTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the homeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/home-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HomeTypeDTO> partialUpdateHomeType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HomeTypeDTO homeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HomeType partially : {}, {}", id, homeTypeDTO);
        if (homeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, homeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!homeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HomeTypeDTO> result = homeTypeService.partialUpdate(homeTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, homeTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /home-types} : get all the homeTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of homeTypes in body.
     */
    @GetMapping("/home-types")
    public ResponseEntity<List<HomeTypeDTO>> getAllHomeTypes(HomeTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HomeTypes by criteria: {}", criteria);
        Page<HomeTypeDTO> page = homeTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /home-types/count} : count all the homeTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/home-types/count")
    public ResponseEntity<Long> countHomeTypes(HomeTypeCriteria criteria) {
        log.debug("REST request to count HomeTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(homeTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /home-types/:id} : get the "id" homeType.
     *
     * @param id the id of the homeTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the homeTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/home-types/{id}")
    public ResponseEntity<HomeTypeDTO> getHomeType(@PathVariable Long id) {
        log.debug("REST request to get HomeType : {}", id);
        Optional<HomeTypeDTO> homeTypeDTO = homeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(homeTypeDTO);
    }

    /**
     * {@code DELETE  /home-types/:id} : delete the "id" homeType.
     *
     * @param id the id of the homeTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/home-types/{id}")
    public ResponseEntity<Void> deleteHomeType(@PathVariable Long id) {
        log.debug("REST request to delete HomeType : {}", id);
        homeTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
