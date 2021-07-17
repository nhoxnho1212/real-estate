package com.ou.ret.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ou.ret.domain.Extra} entity.
 */
public class ExtraDTO implements Serializable {

    private Long id;

    private String extraName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtraName() {
        return extraName;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtraDTO)) {
            return false;
        }

        ExtraDTO extraDTO = (ExtraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, extraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtraDTO{" +
            "id=" + getId() +
            ", extraName='" + getExtraName() + "'" +
            "}";
    }
}
