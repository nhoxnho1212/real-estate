package com.ou.ret.service.mapper;

import com.ou.ret.domain.*;
import com.ou.ret.service.dto.HomeTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HomeType} and its DTO {@link HomeTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HomeTypeMapper extends EntityMapper<HomeTypeDTO, HomeType> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HomeTypeDTO toDtoId(HomeType homeType);
}
