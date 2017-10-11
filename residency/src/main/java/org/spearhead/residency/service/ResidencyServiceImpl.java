package org.spearhead.residency.service;

import org.spearhead.residency.data.entity.Apartment;
import org.spearhead.residency.data.repository.ApartmentRepository;
import org.spearhead.residency.service.composite.*;
import org.spearhead.residency.service.visitor.OverCrowdingVisitor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

import static org.spearhead.residency.service.composite.ResidencyComponentType.APARTMENT;

@Service
public class ResidencyServiceImpl implements ResidencyService, ApplicationContextAware {
    private ApartmentRepository apartmentRepository;
    private ApplicationContext applicationContext;

    @Autowired
    public ResidencyServiceImpl(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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
        return component.getResidentCount();
    }

    @Override
    public List<ApartmentComponent> getCrowdedApartments(ResidencyComponent component) {
        OverCrowdingVisitor visitor = applicationContext.getBean(OverCrowdingVisitor.class, APARTMENT);
        Iterator<ResidencyComponent> iterator = component.iterator();
        while (iterator.hasNext()) {
            iterator.next().accept(visitor);
        }

        return visitor.getApartments();
    }
}
