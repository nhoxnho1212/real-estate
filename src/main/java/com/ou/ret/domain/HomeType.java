package com.ou.ret.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A HomeType.
 */
@Entity
@Table(name = "home_type")
public class HomeType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name")
    private String typeName;

    @OneToMany(mappedBy = "homeType")
    @JsonIgnoreProperties(value = { "extras", "homeType" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HomeType id(Long id) {
        this.id = id;
        return this;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public HomeType typeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public HomeType projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public HomeType addProject(Project project) {
        this.projects.add(project);
        project.setHomeType(this);
        return this;
    }

    public HomeType removeProject(Project project) {
        this.projects.remove(project);
        project.setHomeType(null);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.setHomeType(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setHomeType(this));
        }
        this.projects = projects;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HomeType)) {
            return false;
        }
        return id != null && id.equals(((HomeType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HomeType{" +
            "id=" + getId() +
            ", typeName='" + getTypeName() + "'" +
            "}";
    }
}
