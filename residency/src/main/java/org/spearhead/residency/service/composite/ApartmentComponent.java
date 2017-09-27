package org.spearhead.residency.service.composite;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spearhead.residency.data.entity.Apartment;

public class ApartmentComponent extends AbstractResidencyComponent {
    private String wing;
    private String building;
    private int number;
    private int residentCount;

    public ApartmentComponent(Apartment apartment) {
        super(ResidencyComponentType.APARTMENT);
        this.wing = apartment.getWing();
        this.building = apartment.getBuilding();
        this.number = apartment.getNumber();
        this.residentCount = apartment.getMemberCount();
    }

    public String getWing() {
        return wing;
    }

    public String getBuilding() {
        return building;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int getResidentCount() {
        return residentCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ApartmentComponent that = (ApartmentComponent) o;
        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(number, that.number).append(wing, that.wing).append(building, that.building).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(wing).append(building).append(number).toHashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder(building).append("-").append(number).toString();
    }
}
