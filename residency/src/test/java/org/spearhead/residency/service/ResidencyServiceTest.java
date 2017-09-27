package org.spearhead.residency.service;

import org.junit.Before;
import org.junit.Test;
import org.spearhead.residency.data.entity.Apartment;
import org.spearhead.residency.data.repository.ApartmentRepository;
import org.spearhead.residency.service.composite.ResidencyComponent;
import org.spearhead.residency.service.composite.ResidencyComponentType;
import org.spearhead.residency.service.composite.SocietyComponent;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.spearhead.residency.service.composite.ResidencyComponentType.*;

public class ResidencyServiceTest {
    private ApartmentRepository apartmentRepository;


    @Before
    public void setup() {
        apartmentRepository = mock(ApartmentRepository.class);
    }

    @Test
    public void getResidencyComposite() {
        ArrayList<Apartment> apartments = getComplexApartments();
        when(apartmentRepository.findAll()).thenReturn(apartments);

        ResidencyService service = new ResidencyServiceImpl(apartmentRepository);
        ResidencyComponent residencyComposite = service.getResidencyComposite();
        Iterator<ResidencyComponent> iterator = residencyComposite.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }
    }

    @Test
    public void testSimpleComposite() {
        List<Apartment> apartments = getSimpleApartmentList(new String[]{"A"}, 1, 2);
        when(apartmentRepository.findAll()).thenReturn(apartments);

        ResidencyService service = new ResidencyServiceImpl(apartmentRepository);
        ResidencyComponent component = service.getResidencyComposite();

        validateComponent(component, 1, SOCIETY);
        validateComponent((component = component.getChildComponents().get(0)), 1, WING);
        validateComponent((component = component.getChildComponents().get(0)), 2, BUILDING);

        // Validate floor components
        for (int i = 0; i < 2; i++) {
            ResidencyComponent floorComponent = component.getChildComponents().get(i);
            validateComponent(floorComponent, 4, FLOOR);
            for (int j = 0; j < 4; j++) {
                validateComponent(floorComponent.getChildComponents().get(j), 0, APARTMENT);
            }
        }
    }

    @Test
    public void testSimpleIteration() {
        List<Apartment> apartments = getSimpleApartmentList(new String[]{"A"}, 1, 2);
        when(apartmentRepository.findAll()).thenReturn(apartments);

        ResidencyService service = new ResidencyServiceImpl(apartmentRepository);
        ResidencyComponent residencyComposite = service.getResidencyComposite();
        Iterator<ResidencyComponent> iterator = residencyComposite.iterator();

        assertTrue(iterator.hasNext());
        validateComponent(iterator.next(), 1, SOCIETY);

        assertTrue(iterator.hasNext());
        validateComponent(iterator.next(), 1, WING);

        assertTrue(iterator.hasNext());
        validateComponent(iterator.next(), 2, BUILDING);

        for (int i = 0; i < 2; i++) {
            ResidencyComponent floorComponent = iterator.next();
            validateComponent(floorComponent, 4, FLOOR);

            for (int j = 0; j < 4; j++) {
                validateComponent(iterator.next(), 0, APARTMENT);
            }
        }

        assertFalse(iterator.hasNext());

        iterator = residencyComposite.iterator();
        int iterationCount = 0;
        while (iterator.hasNext()) {
            iterator.next();
            iterationCount++;
        }
        assertEquals(13, iterationCount);
    }

    @Test
    public void testArbitraryNodeIteration() {
        List<Apartment> apartments = getSimpleApartmentList(new String[]{"A"}, 1, 2);
        when(apartmentRepository.findAll()).thenReturn(apartments);

        ResidencyService service = new ResidencyServiceImpl(apartmentRepository);
        ResidencyComponent residencyComposite = service.getResidencyComposite();
        ResidencyComponent building = residencyComposite.getChildComponents().get(0).getChildComponents().get(0);

        Iterator<ResidencyComponent> iterator = building.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        validateComponent(iterator.next(), 2, BUILDING);
        for (int i = 0; i < 2; i++) {
            ResidencyComponent floorComponent = iterator.next();
            validateComponent(floorComponent, 4, FLOOR);

            for (int j = 0; j < 4; j++) {
                validateComponent(iterator.next(), 0, APARTMENT);
            }
        }
    }

    @Test
    public void testSingleNodeIteration() {
        SocietyComponent component = new SocietyComponent("Test Name");
        Iterator<ResidencyComponent> iterator = component.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    private void validateComponent(ResidencyComponent component, int expectedChildCount,
                                   ResidencyComponentType componentType) {
        assertNotNull(component);
        assertEquals(componentType, component.getComponentType());
        assertNotNull(component.getChildComponents());
        assertEquals(expectedChildCount, component.getChildComponents().size());
    }

    private List<Apartment> getSimpleApartmentList(String[] wings, int buildingCount, int floorCount) {
        PrimitiveIterator.OfInt randomMembers = new Random().ints(0, 7).iterator();
        ArrayList<Apartment> apartments = new ArrayList<>();
        for (String wing : wings) {
            fillWingApartments(randomMembers, apartments, wing, buildingCount, floorCount);
        }
        return apartments;
    }

    private ArrayList<Apartment> getComplexApartments() {
        String[] wings = {"A", "B", "C"};
        PrimitiveIterator.OfInt randomBldgs = new Random().ints(1, 3).iterator();
        PrimitiveIterator.OfInt randomFloors = new Random().ints(4, 5).iterator();
        PrimitiveIterator.OfInt randomMembers = new Random().ints(0, 7).iterator();
        ArrayList<Apartment> apartments = new ArrayList<>();
        for (String wing : wings) {
            int buildingCount = randomBldgs.nextInt();
            int floorCount = randomFloors.nextInt();

            fillWingApartments(randomMembers, apartments, wing, buildingCount, floorCount);
        }

        return apartments;
    }

    private void fillWingApartments(PrimitiveIterator.OfInt randomMembers, ArrayList<Apartment> apartments, String wing, int buildingCount, int floorCount) {
        for (int i = 1; i <= buildingCount; i++) {
            for (int j = 1; j <= floorCount; j++) {
                for (int k = 1; k <= 4; k++) {
                    int memberCount = randomMembers.nextInt();
                    apartments.add(new Apartment(wing, wing + i, j, j * 100 + k, memberCount));
                }
            }
        }
    }
}
