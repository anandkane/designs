package org.spearhead.residency.service.composite;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spearhead.residency.service.visitor.ResidencyComponentVisitor;

public class SocietyComponent extends AbstractResidencyComponent {
    private String name;

    public SocietyComponent(String name) {
        super(ResidencyComponentType.SOCIETY);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SocietyComponent that = (SocietyComponent) o;
        return new EqualsBuilder()
                .append(name, that.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name).toHashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void accept(ResidencyComponentVisitor visitor) {
        visitor.visitSociety(this);
    }
}
