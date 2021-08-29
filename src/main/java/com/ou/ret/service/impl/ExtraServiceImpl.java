package com.ou.ret.service.impl;

import com.ou.ret.domain.Extra;
import com.ou.ret.repository.ExtraRepository;
import com.ou.ret.service.ExtraService;
import com.ou.ret.service.dto.ExtraDTO;
import com.ou.ret.service.mapper.ExtraMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Extra}.
 */
@Service
@Transactional
public class ExtraServiceImpl implements ExtraService {

    private final Logger log = LoggerFactory.getLogger(ExtraServiceImpl.class);

    private final ExtraRepository extraRepository;

    private final ExtraMapper extraMapper;

    public ExtraServiceImpl(ExtraRepository extraRepository, ExtraMapper extraMapper) {
        this.extraRepository = extraRepository;
        this.extraMapper = extraMapper;
    }

    @Override
    public ExtraDTO save(ExtraDTO extraDTO) {
        log.debug("Request to save Extra : {}", extraDTO);
        Extra extra = extraMapper.toEntity(extraDTO);
        extra = extraRepository.save(extra);
        return extraMapper.toDto(extra);
    }

    @Override
    public Optional<ExtraDTO> partialUpdate(ExtraDTO extraDTO) {
        log.debug("Request to partially update Extra : {}", extraDTO);

        return extraRepository
            .findById(extraDTO.getId())
            .map(
                existingExtra -> {
                    extraMapper.partialUpdate(existingExtra, extraDTO);

                    return existingExtra;
                }
            )
            .map(extraRepository::save)
            .map(extraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExtraDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Extras");
        return extraRepository.findAll(pageable).map(extraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExtraDTO> findOne(Long id) {
        log.debug("Request to get Extra : {}", id);
        return extraRepository.findById(id).map(extraMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Extra : {}", id);
        extraRepository.deleteById(id);
    }
}
