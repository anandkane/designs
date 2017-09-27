package org.spearhead.residency.service.composite;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spearhead.residency.data.entity.Apartment;

public class BuildingComponent extends AbstractResidencyComponent {
    private String wing;
    private String building;

    public BuildingComponent(Apartment apartment) {
        super(ResidencyComponentType.BUILDING);
        this.wing = apartment.getWing();
        this.building = apartment.getBuilding();
    }

    public String getWing() {
        return wing;
    }

    public String getBuilding() {
        return building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BuildingComponent that = (BuildingComponent) o;
        return new EqualsBuilder()
                .append(wing, that.wing).append(building, that.building).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(wing).append(building).toHashCode();
    }

    @Override
    public String toString() {
        return building;
    }
}
