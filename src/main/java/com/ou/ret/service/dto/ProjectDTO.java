package com.ou.ret.service.dto;

import com.ou.ret.domain.ProjectStatus;
import com.ou.ret.domain.User;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.ou.ret.domain.Project} entity.
 */
public class ProjectDTO implements Serializable {

    private Long id;

    private String address;

    private String city;

    private Integer rooms;

    private Float price;

    private Float floorSpace;

    private String attachment;

    private Set<ExtraDTO> extras = new HashSet<>();

    private HomeTypeDTO homeType;

    private String owner;

    private String ownerDesciption;

    private ProjectStatus projectStatus;

    private Float totalSquare;

    private String desciption;

    private UserDTO userDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getFloorSpace() {
        return floorSpace;
    }

    public void setFloorSpace(Float floorSpace) {
        this.floorSpace = floorSpace;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Set<ExtraDTO> getExtras() {
        return extras;
    }

    public void setExtras(Set<ExtraDTO> extras) {
        this.extras = extras;
    }

    public HomeTypeDTO getHomeType() {
        return homeType;
    }

    public void setHomeType(HomeTypeDTO homeType) {
        this.homeType = homeType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerDesciption() {
        return ownerDesciption;
    }

    public void setOwnerDesciption(String ownerDesciption) {
        this.ownerDesciption = ownerDesciption;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Float getTotalSquare() {
        return totalSquare;
    }

    public void setTotalSquare(Float totalSquare) {
        this.totalSquare = totalSquare;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectDTO)) {
            return false;
        }

        ProjectDTO projectDTO = (ProjectDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, projectDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectDTO{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", rooms=" + getRooms() +
            ", price=" + getPrice() +
            ", floorSpace=" + getFloorSpace() +
            ", attachment='" + getAttachment() + "'" +
            ", extras=" + getExtras() +
            ", homeType=" + getHomeType() +
            "}";
    }
}
