package com.ou.ret.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ou.ret.domain.Extra} entity. This class is used
 * in {@link com.ou.ret.web.rest.ExtraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /extras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExtraCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter extraName;

    private LongFilter projectId;

    public ExtraCriteria() {}

    public ExtraCriteria(ExtraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.extraName = other.extraName == null ? null : other.extraName.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
    }

    @Override
    public ExtraCriteria copy() {
        return new ExtraCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getExtraName() {
        return extraName;
    }

    public StringFilter extraName() {
        if (extraName == null) {
            extraName = new StringFilter();
        }
        return extraName;
    }

    public void setExtraName(StringFilter extraName) {
        this.extraName = extraName;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public LongFilter projectId() {
        if (projectId == null) {
            projectId = new LongFilter();
        }
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExtraCriteria that = (ExtraCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(extraName, that.extraName) && Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, extraName, projectId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtraCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (extraName != null ? "extraName=" + extraName + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            "}";
    }
}
