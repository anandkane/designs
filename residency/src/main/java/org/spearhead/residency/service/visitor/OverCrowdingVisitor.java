package org.spearhead.residency.service.visitor;

import org.spearhead.residency.service.composite.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("OverCrowdingVisitor")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OverCrowdingVisitor implements ResidencyComponentVisitor {
    private List<ApartmentComponent> apartments = new ArrayList<>();
    private List<BuildingComponent> buildings = new ArrayList<>();

    private ResidencyComponentType[] targetTypes;

    public OverCrowdingVisitor(ResidencyComponentType... targetTypes) {
        this.targetTypes = Arrays.copyOf(targetTypes, targetTypes.length);
    }

    public List<ApartmentComponent> getApartments() {
        return apartments;
    }

    public List<BuildingComponent> getBuildings() {
        return buildings;
    }

    @Override
    public void visitSociety(SocietyComponent component) {

    }

    @Override
    public void visitWing(WingComponent component) {

    }

    @Override
    public void visitBuilding(BuildingComponent component) {
        if (Arrays.binarySearch(targetTypes, ResidencyComponentType.BUILDING) < 0) {
            return;
        }

        int floorCount = component.getChildComponents().size();
        int idealCount = floorCount * 4 * 4;

        int actualCount = component.getResidentCount();
        int diff = actualCount - idealCount;
        if (diff > 0) {
            int percent = diff * 100 / idealCount;
            if (percent > 10) {
                buildings.add(component);
            }
        }
    }

    @Override
    public void visitFloor(FloorComponent component) {

    }

    @Override
    public void visitApartment(ApartmentComponent component) {
        if (Arrays.binarySearch(targetTypes, ResidencyComponentType.APARTMENT) < 0) {
            return;
        }

        if (component.getResidentCount() >= 6) {
            apartments.add(component);
        }
    }
}
