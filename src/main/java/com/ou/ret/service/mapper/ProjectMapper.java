package com.ou.ret.service.mapper;

import com.ou.ret.domain.*;
import com.ou.ret.service.dto.ProjectDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring", uses = { ExtraMapper.class, HomeTypeMapper.class })
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
    @Mapping(target = "extras", source = "extras", qualifiedByName = "extraNameSet")
    @Mapping(target = "homeType", source = "homeType", qualifiedByName = "id")
    ProjectDTO toDto(Project s);

    @Mapping(target = "removeExtra", ignore = true)
    Project toEntity(ProjectDTO projectDTO);
}
