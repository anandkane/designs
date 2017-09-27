package org.spearhead.residency.service.composite;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spearhead.residency.data.entity.Apartment;

public class WingComponent extends AbstractResidencyComponent {
    private String wing;

    public WingComponent(Apartment apartment) {
        super(ResidencyComponentType.WING);
        this.wing = apartment.getWing();
    }

    public String getWing() {
        return wing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WingComponent that = (WingComponent) o;
        return new EqualsBuilder()
                .append(wing, that.wing).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(wing).toHashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder().append(wing).append(" wing").toString();
    }
}
