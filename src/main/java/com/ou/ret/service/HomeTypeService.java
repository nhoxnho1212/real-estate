package com.ou.ret.service;

import com.ou.ret.service.dto.HomeTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ou.ret.domain.HomeType}.
 */
public interface HomeTypeService {
    /**
     * Save a homeType.
     *
     * @param homeTypeDTO the entity to save.
     * @return the persisted entity.
     */
    HomeTypeDTO save(HomeTypeDTO homeTypeDTO);

    /**
     * Partially updates a homeType.
     *
     * @param homeTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HomeTypeDTO> partialUpdate(HomeTypeDTO homeTypeDTO);

    /**
     * Get all the homeTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HomeTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" homeType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HomeTypeDTO> findOne(Long id);

    /**
     * Delete the "id" homeType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
