package org.spearhead.residency.service;

import org.spearhead.residency.data.entity.Apartment;
import org.spearhead.residency.data.repository.ApartmentRepository;
import org.spearhead.residency.service.composite.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResidencyServiceImpl implements ResidencyService {
    private ApartmentRepository apartmentRepository;

    @Autowired
    public ResidencyServiceImpl(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public ResidencyComponent getResidencyComposite() {
        ResidencyComponent societyComponent = new SocietyComponent("Park Springs");
        List<Apartment> apartments = apartmentRepository.findAll();

        ResidencyComponent component;
        for (Apartment apartment : apartments) {
            component = societyComponent.addChildComponent(new WingComponent(apartment));
            component = component.addChildComponent(new BuildingComponent(apartment));
            component = component.addChildComponent(new FloorComponent(apartment));
            component.addChildComponent(new ApartmentComponent(apartment));
        }

        return societyComponent;
    }

    @Override
    public int getResidentCount(ResidencyComponent component) {
        return 0;
    }

    @Override
    public List<Apartment> getCrowdedAparments(ResidencyComponent component) {
        return null;
    }
}
