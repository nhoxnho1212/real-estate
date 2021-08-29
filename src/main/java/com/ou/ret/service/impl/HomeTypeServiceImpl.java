package com.ou.ret.service.impl;

import com.ou.ret.domain.HomeType;
import com.ou.ret.repository.HomeTypeRepository;
import com.ou.ret.service.HomeTypeService;
import com.ou.ret.service.dto.HomeTypeDTO;
import com.ou.ret.service.mapper.HomeTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HomeType}.
 */
@Service
@Transactional
public class HomeTypeServiceImpl implements HomeTypeService {

    private final Logger log = LoggerFactory.getLogger(HomeTypeServiceImpl.class);

    private final HomeTypeRepository homeTypeRepository;

    private final HomeTypeMapper homeTypeMapper;

    public HomeTypeServiceImpl(HomeTypeRepository homeTypeRepository, HomeTypeMapper homeTypeMapper) {
        this.homeTypeRepository = homeTypeRepository;
        this.homeTypeMapper = homeTypeMapper;
    }

    @Override
    public HomeTypeDTO save(HomeTypeDTO homeTypeDTO) {
        log.debug("Request to save HomeType : {}", homeTypeDTO);
        HomeType homeType = homeTypeMapper.toEntity(homeTypeDTO);
        homeType = homeTypeRepository.save(homeType);
        return homeTypeMapper.toDto(homeType);
    }

    @Override
    public Optional<HomeTypeDTO> partialUpdate(HomeTypeDTO homeTypeDTO) {
        log.debug("Request to partially update HomeType : {}", homeTypeDTO);

        return homeTypeRepository
            .findById(homeTypeDTO.getId())
            .map(
                existingHomeType -> {
                    homeTypeMapper.partialUpdate(existingHomeType, homeTypeDTO);

                    return existingHomeType;
                }
            )
            .map(homeTypeRepository::save)
            .map(homeTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HomeTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HomeTypes");
        return homeTypeRepository.findAll(pageable).map(homeTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HomeTypeDTO> findOne(Long id) {
        log.debug("Request to get HomeType : {}", id);
        return homeTypeRepository.findById(id).map(homeTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HomeType : {}", id);
        homeTypeRepository.deleteById(id);
    }
}
