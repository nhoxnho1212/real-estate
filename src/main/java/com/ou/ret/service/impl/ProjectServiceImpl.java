package com.ou.ret.service.impl;

import com.ou.ret.domain.Project;
import com.ou.ret.domain.User;
import com.ou.ret.repository.ProjectRepository;
import com.ou.ret.repository.UserRepository;
import com.ou.ret.service.ProjectService;
import com.ou.ret.service.dto.ProjectDTO;
import com.ou.ret.service.mapper.ProjectMapper;
import com.ou.ret.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Project}.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public ProjectDTO save(ProjectDTO projectDTO) {
        log.debug("Request to save Project : {}", projectDTO);
        Project project = projectMapper.toEntity(projectDTO);
        if (Objects.isNull(projectDTO.getUserDTO()) || Objects.isNull(projectDTO.getUserDTO().getId())) {
            throw new BadRequestAlertException("Invalid user ID", "project", "idinvalid");
        }
        Optional<User> user = userRepository.findById(projectDTO.getUserDTO().getId());
        if (user.isEmpty()) {
            throw new BadRequestAlertException("Invalid user ID", "project", "idinvalid");
        }
        project.setUser(user.get());
        project = projectRepository.save(project);
        return projectMapper.toDto(project);
    }

    @Override
    public Optional<ProjectDTO> partialUpdate(ProjectDTO projectDTO) {
        log.debug("Request to partially update Project : {}", projectDTO);

        return projectRepository
            .findById(projectDTO.getId())
            .map(
                existingProject -> {
                    projectMapper.partialUpdate(existingProject, projectDTO);

                    return existingProject;
                }
            )
            .map(projectRepository::save)
            .map(projectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectRepository.findAll(pageable).map(projectMapper::toDto);
    }

    public Page<ProjectDTO> findAllWithEagerRelationships(Pageable pageable) {
        return projectRepository.findAllWithEagerRelationships(pageable).map(projectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectDTO> findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findOneWithEagerRelationships(id).map(projectMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
    }

    @Override
    public List<String> findAllCities() {
        List<Project> projects = projectRepository.findAll();
        List<String> cities = new ArrayList<>();

        projects.forEach(element -> {
            cities.add(element.getCity());
        });

        return cities.stream().distinct().collect(Collectors.toList());
    }
}
