package com.ou.ret.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Extra.
 */
@Entity
@Table(name = "extra")
public class Extra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "extra_name")
    private String extraName;

    @ManyToMany(mappedBy = "extras")
    @JsonIgnoreProperties(value = { "extras", "homeType" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Extra id(Long id) {
        this.id = id;
        return this;
    }

    public String getExtraName() {
        return this.extraName;
    }

    public Extra extraName(String extraName) {
        this.extraName = extraName;
        return this;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public Extra projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public Extra addProject(Project project) {
        this.projects.add(project);
        project.getExtras().add(this);
        return this;
    }

    public Extra removeProject(Project project) {
        this.projects.remove(project);
        project.getExtras().remove(this);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.removeExtra(this));
        }
        if (projects != null) {
            projects.forEach(i -> i.addExtra(this));
        }
        this.projects = projects;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Extra)) {
            return false;
        }
        return id != null && id.equals(((Extra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Extra{" +
            "id=" + getId() +
            ", extraName='" + getExtraName() + "'" +
            "}";
    }
}
