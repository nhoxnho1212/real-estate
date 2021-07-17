package com.ou.ret.service;

import com.ou.ret.service.dto.ExtraDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ou.ret.domain.Extra}.
 */
public interface ExtraService {
    /**
     * Save a extra.
     *
     * @param extraDTO the entity to save.
     * @return the persisted entity.
     */
    ExtraDTO save(ExtraDTO extraDTO);

    /**
     * Partially updates a extra.
     *
     * @param extraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExtraDTO> partialUpdate(ExtraDTO extraDTO);

    /**
     * Get all the extras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExtraDTO> findAll(Pageable pageable);

    /**
     * Get the "id" extra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExtraDTO> findOne(Long id);

    /**
     * Delete the "id" extra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
