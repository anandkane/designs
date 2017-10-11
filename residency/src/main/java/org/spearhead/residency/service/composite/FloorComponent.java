package org.spearhead.residency.service.composite;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spearhead.residency.data.entity.Apartment;
import org.spearhead.residency.service.visitor.ResidencyComponentVisitor;

public class FloorComponent extends AbstractResidencyComponent {
    private String wing;
    private String building;
    private int floor;

    public FloorComponent(Apartment apartment) {
        super(ResidencyComponentType.FLOOR);
        this.wing = apartment.getWing();
        this.building = apartment.getBuilding();
        this.floor = apartment.getFloor();
    }

    public String getWing() {
        return wing;
    }

    public String getBuilding() {
        return building;
    }

    public int getFloor() {
        return floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FloorComponent that = (FloorComponent) o;
        return new EqualsBuilder()
                .append(floor, that.floor).append(wing, that.wing).append(building, that.building).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(wing).append(building).append(floor).toHashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder(building).append("-").append("Floor ").append(floor).toString();
    }

    @Override
    public void accept(ResidencyComponentVisitor visitor) {
        visitor.visitFloor(this);
    }
}
