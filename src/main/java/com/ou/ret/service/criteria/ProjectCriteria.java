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
 * Criteria class for the {@link com.ou.ret.domain.Project} entity. This class is used
 * in {@link com.ou.ret.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter address;

    private StringFilter city;

    private IntegerFilter rooms;

    private FloatFilter price;

    private FloatFilter floorSpace;

    private StringFilter attachment;

    private LongFilter extraId;

    private LongFilter homeTypeId;

    public ProjectCriteria() {}

    public ProjectCriteria(ProjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.rooms = other.rooms == null ? null : other.rooms.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.floorSpace = other.floorSpace == null ? null : other.floorSpace.copy();
        this.attachment = other.attachment == null ? null : other.attachment.copy();
        this.extraId = other.extraId == null ? null : other.extraId.copy();
        this.homeTypeId = other.homeTypeId == null ? null : other.homeTypeId.copy();
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
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

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public IntegerFilter getRooms() {
        return rooms;
    }

    public IntegerFilter rooms() {
        if (rooms == null) {
            rooms = new IntegerFilter();
        }
        return rooms;
    }

    public void setRooms(IntegerFilter rooms) {
        this.rooms = rooms;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public FloatFilter price() {
        if (price == null) {
            price = new FloatFilter();
        }
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public FloatFilter getFloorSpace() {
        return floorSpace;
    }

    public FloatFilter floorSpace() {
        if (floorSpace == null) {
            floorSpace = new FloatFilter();
        }
        return floorSpace;
    }

    public void setFloorSpace(FloatFilter floorSpace) {
        this.floorSpace = floorSpace;
    }

    public StringFilter getAttachment() {
        return attachment;
    }

    public StringFilter attachment() {
        if (attachment == null) {
            attachment = new StringFilter();
        }
        return attachment;
    }

    public void setAttachment(StringFilter attachment) {
        this.attachment = attachment;
    }

    public LongFilter getExtraId() {
        return extraId;
    }

    public LongFilter extraId() {
        if (extraId == null) {
            extraId = new LongFilter();
        }
        return extraId;
    }

    public void setExtraId(LongFilter extraId) {
        this.extraId = extraId;
    }

    public LongFilter getHomeTypeId() {
        return homeTypeId;
    }

    public LongFilter homeTypeId() {
        if (homeTypeId == null) {
            homeTypeId = new LongFilter();
        }
        return homeTypeId;
    }

    public void setHomeTypeId(LongFilter homeTypeId) {
        this.homeTypeId = homeTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjectCriteria that = (ProjectCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(address, that.address) &&
            Objects.equals(city, that.city) &&
            Objects.equals(rooms, that.rooms) &&
            Objects.equals(price, that.price) &&
            Objects.equals(floorSpace, that.floorSpace) &&
            Objects.equals(attachment, that.attachment) &&
            Objects.equals(extraId, that.extraId) &&
            Objects.equals(homeTypeId, that.homeTypeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, city, rooms, price, floorSpace, attachment, extraId, homeTypeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (rooms != null ? "rooms=" + rooms + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (floorSpace != null ? "floorSpace=" + floorSpace + ", " : "") +
            (attachment != null ? "attachment=" + attachment + ", " : "") +
            (extraId != null ? "extraId=" + extraId + ", " : "") +
            (homeTypeId != null ? "homeTypeId=" + homeTypeId + ", " : "") +
            "}";
    }
}
