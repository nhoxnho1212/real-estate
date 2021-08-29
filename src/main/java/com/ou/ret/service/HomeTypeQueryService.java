package com.ou.ret.service;

import com.ou.ret.domain.*; // for static metamodels
import com.ou.ret.domain.HomeType;
import com.ou.ret.repository.HomeTypeRepository;
import com.ou.ret.service.criteria.HomeTypeCriteria;
import com.ou.ret.service.dto.HomeTypeDTO;
import com.ou.ret.service.mapper.HomeTypeMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HomeType} entities in the database.
 * The main input is a {@link HomeTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HomeTypeDTO} or a {@link Page} of {@link HomeTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HomeTypeQueryService extends QueryService<HomeType> {

    private final Logger log = LoggerFactory.getLogger(HomeTypeQueryService.class);

    private final HomeTypeRepository homeTypeRepository;

    private final HomeTypeMapper homeTypeMapper;

    public HomeTypeQueryService(HomeTypeRepository homeTypeRepository, HomeTypeMapper homeTypeMapper) {
        this.homeTypeRepository = homeTypeRepository;
        this.homeTypeMapper = homeTypeMapper;
    }

    /**
     * Return a {@link List} of {@link HomeTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HomeTypeDTO> findByCriteria(HomeTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HomeType> specification = createSpecification(criteria);
        return homeTypeMapper.toDto(homeTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HomeTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HomeTypeDTO> findByCriteria(HomeTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HomeType> specification = createSpecification(criteria);
        return homeTypeRepository.findAll(specification, page).map(homeTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HomeTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HomeType> specification = createSpecification(criteria);
        return homeTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link HomeTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HomeType> createSpecification(HomeTypeCriteria criteria) {
        Specification<HomeType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HomeType_.id));
            }
            if (criteria.getTypeName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypeName(), HomeType_.typeName));
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjectId(), root -> root.join(HomeType_.projects, JoinType.LEFT).get(Project_.id))
                    );
            }
        }
        return specification;
    }
}
