package org.spearhead.residency.service.visitor;

import org.spearhead.residency.service.composite.*;

public interface ResidencyComponentVisitor {
    void visitSociety(SocietyComponent component);
    void visitWing(WingComponent component);
    void visitBuilding(BuildingComponent component);
    void visitFloor(FloorComponent component);
    void visitApartment(ApartmentComponent component);
}
