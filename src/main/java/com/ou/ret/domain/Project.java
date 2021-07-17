package com.ou.ret.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "rooms")
    private Integer rooms;

    @Column(name = "price")
    private Float price;

    @Column(name = "floor_space")
    private Float floorSpace;

    @Column(name = "attachment")
    private String attachment;

    @ManyToMany
    @JoinTable(
        name = "rel_project__extra",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "extra_id")
    )
    @JsonIgnoreProperties(value = { "projects" }, allowSetters = true)
    private Set<Extra> extras = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "projects" }, allowSetters = true)
    private HomeType homeType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project id(Long id) {
        this.id = id;
        return this;
    }

    public String getAddress() {
        return this.address;
    }

    public Project address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public Project city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getRooms() {
        return this.rooms;
    }

    public Project rooms(Integer rooms) {
        this.rooms = rooms;
        return this;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Float getPrice() {
        return this.price;
    }

    public Project price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getFloorSpace() {
        return this.floorSpace;
    }

    public Project floorSpace(Float floorSpace) {
        this.floorSpace = floorSpace;
        return this;
    }

    public void setFloorSpace(Float floorSpace) {
        this.floorSpace = floorSpace;
    }

    public String getAttachment() {
        return this.attachment;
    }

    public Project attachment(String attachment) {
        this.attachment = attachment;
        return this;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Set<Extra> getExtras() {
        return this.extras;
    }

    public Project extras(Set<Extra> extras) {
        this.setExtras(extras);
        return this;
    }

    public Project addExtra(Extra extra) {
        this.extras.add(extra);
        extra.getProjects().add(this);
        return this;
    }

    public Project removeExtra(Extra extra) {
        this.extras.remove(extra);
        extra.getProjects().remove(this);
        return this;
    }

    public void setExtras(Set<Extra> extras) {
        this.extras = extras;
    }

    public HomeType getHomeType() {
        return this.homeType;
    }

    public Project homeType(HomeType homeType) {
        this.setHomeType(homeType);
        return this;
    }

    public void setHomeType(HomeType homeType) {
        this.homeType = homeType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", rooms=" + getRooms() +
            ", price=" + getPrice() +
            ", floorSpace=" + getFloorSpace() +
            ", attachment='" + getAttachment() + "'" +
            "}";
    }
}
