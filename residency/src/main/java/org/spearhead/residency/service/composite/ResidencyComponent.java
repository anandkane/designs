package org.spearhead.residency.service.composite;

import java.util.List;

public interface ResidencyComponent extends Iterable<ResidencyComponent> {
    ResidencyComponent addChildComponent(ResidencyComponent component);
    List<ResidencyComponent> getChildComponents();
    int getResidentCount();
    ResidencyComponentType getComponentType();
}
