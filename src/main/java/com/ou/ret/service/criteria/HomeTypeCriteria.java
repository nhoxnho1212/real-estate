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
 * Criteria class for the {@link com.ou.ret.domain.HomeType} entity. This class is used
 * in {@link com.ou.ret.web.rest.HomeTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /home-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HomeTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter typeName;

    private LongFilter projectId;

    public HomeTypeCriteria() {}

    public HomeTypeCriteria(HomeTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.typeName = other.typeName == null ? null : other.typeName.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
    }

    @Override
    public HomeTypeCriteria copy() {
        return new HomeTypeCriteria(this);
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

    public StringFilter getTypeName() {
        return typeName;
    }

    public StringFilter typeName() {
        if (typeName == null) {
            typeName = new StringFilter();
        }
        return typeName;
    }

    public void setTypeName(StringFilter typeName) {
        this.typeName = typeName;
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
        final HomeTypeCriteria that = (HomeTypeCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(typeName, that.typeName) && Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeName, projectId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HomeTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (typeName != null ? "typeName=" + typeName + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            "}";
    }
}
