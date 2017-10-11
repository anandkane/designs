package org.spearhead.residency.service;

import org.spearhead.residency.service.composite.ApartmentComponent;
import org.spearhead.residency.service.composite.ResidencyComponent;

import java.util.List;

public interface ResidencyService {
    ResidencyComponent getResidencyComposite();

    int getResidentCount(ResidencyComponent component);

    List<ApartmentComponent> getCrowdedApartments(ResidencyComponent component);
}
