package com.ou.ret.service.mapper;

import com.ou.ret.domain.*;
import com.ou.ret.service.dto.ExtraDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Extra} and its DTO {@link ExtraDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExtraMapper extends EntityMapper<ExtraDTO, Extra> {
    @Named("extraNameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "extraName", source = "extraName")
    Set<ExtraDTO> toDtoExtraNameSet(Set<Extra> extra);
}
