package com.ou.ret.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ou.ret.domain.HomeType} entity.
 */
public class HomeTypeDTO implements Serializable {

    private Long id;

    private String typeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HomeTypeDTO)) {
            return false;
        }

        HomeTypeDTO homeTypeDTO = (HomeTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, homeTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HomeTypeDTO{" +
            "id=" + getId() +
            ", typeName='" + getTypeName() + "'" +
            "}";
    }
}
