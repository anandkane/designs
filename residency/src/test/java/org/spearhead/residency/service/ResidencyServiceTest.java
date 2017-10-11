package org.spearhead.residency.service;

import org.junit.Before;
import org.junit.Test;
import org.spearhead.residency.data.entity.Apartment;
import org.spearhead.residency.data.repository.ApartmentRepository;
import org.spearhead.residency.service.composite.*;
import org.spearhead.residency.service.visitor.OverCrowdingVisitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PrimitiveIterator.OfInt;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.spearhead.residency.service.composite.ResidencyComponentType.*;

public class ResidencyServiceTest {
    private ApartmentRepository apartmentRepository;
    private Random random = new Random();
    private final OfInt randomMembers = random.ints(0, 7).iterator();

    @Before
    public void setup() {
        apartmentRepository = mock(ApartmentRepository.class);
    }

    @Test
    public void testSimpleComposite() {
        List<Apartment> apartments = getSimpleApartmentList(new String[]{"A"}, 1, 2, randomMembers);
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
        List<Apartment> apartments = getSimpleApartmentList(new String[]{"A"}, 1, 2, randomMembers);
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
        List<Apartment> apartments = getSimpleApartmentList(new String[]{"A"}, 1, 2, randomMembers);
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

    @Test
    public void testGetResidentCount() {
        List<Apartment> apartments = getSimpleApartmentList(new String[]{"A"}, 1, 2, randomMembers);
        for (Apartment apartment : apartments) {
            apartment.setMemberCount(1);
        }

        when(apartmentRepository.findAll()).thenReturn(apartments);
        ResidencyService service = new ResidencyServiceImpl(apartmentRepository);
        int residentCount = service.getResidentCount(service.getResidencyComposite());
        assertEquals(8, residentCount);
    }

    @Test
    public void testApartmentResidentCount() {
        // Test that individual apartments return right resident count
        Apartment apartment = new Apartment();
        apartment.setMemberCount(10);
        ApartmentComponent component = new ApartmentComponent(apartment);

        assertEquals(10, component.getResidentCount());
    }

    @Test
    public void testNonApartmentResidentCount() {
        // Test that components other than Apartment do not add to the resident count
        Apartment apartment = new Apartment();
        apartment.setMemberCount(10);

        ResidencyComponent component;
        component = new SocietyComponent("");
        assertEquals(0, component.getResidentCount());
        component = new WingComponent(apartment);
        assertEquals(0, component.getResidentCount());
        component = new BuildingComponent(apartment);
        assertEquals(0, component.getResidentCount());
        component = new FloorComponent(apartment);
        assertEquals(0, component.getResidentCount());
    }

    @Test
    public void testOverCrowdedApartments() {
        OfInt randomMembers = random.ints(0, 4).iterator();
        List<Apartment> apartments = getSimpleApartmentList(new String[]{"A"}, 1, 2, randomMembers);
        for (int i = 0; i < 4; i++) {
            apartments.get(i).setMemberCount(6);
        }
        when(apartmentRepository.findAll()).thenReturn(apartments);

        ResidencyService service = new ResidencyServiceImpl(apartmentRepository);
        ResidencyComponent component = service.getResidencyComposite();

        OverCrowdingVisitor visitor = new OverCrowdingVisitor(APARTMENT);
        Iterator<ResidencyComponent> iterator = component.iterator();
        while (iterator.hasNext()) {
            iterator.next().accept(visitor);
        }

        assertEquals(4, visitor.getApartments().size());
        assertEquals(0, visitor.getBuildings().size());
    }

    @Test
    public void testOverCrowdedBuildings() {
        OfInt randomMembers = random.ints(0, 4).iterator();
        List<Apartment> apartments = getSimpleApartmentList(new String[]{"A"}, 2, 2, randomMembers);
        for (Apartment apartment : apartments) {
            if (apartment.getBuilding().equals("A1")) {
                apartment.setMemberCount(6);
            }
        }

        when(apartmentRepository.findAll()).thenReturn(apartments);

        ResidencyService service = new ResidencyServiceImpl(apartmentRepository);
        ResidencyComponent component = service.getResidencyComposite();

        OverCrowdingVisitor visitor = new OverCrowdingVisitor(BUILDING);
        Iterator<ResidencyComponent> iterator = component.iterator();
        while (iterator.hasNext()) {
            iterator.next().accept(visitor);
        }

        assertEquals(1, visitor.getBuildings().size());
        assertEquals("A1", visitor.getBuildings().get(0).getBuilding());
        assertEquals(0, visitor.getApartments().size());
    }

    private void validateComponent(ResidencyComponent component, int expectedChildCount,
                                   ResidencyComponentType componentType) {
        assertNotNull(component);
        assertEquals(componentType, component.getComponentType());
        assertNotNull(component.getChildComponents());
        assertEquals(expectedChildCount, component.getChildComponents().size());
    }

    private List<Apartment> getSimpleApartmentList(String[] wings, int buildingCount, int floorCount,
                                                   OfInt randomMembers) {
        ArrayList<Apartment> apartments = new ArrayList<>();
        for (String wing : wings) {
            fillWingApartments(randomMembers, apartments, wing, buildingCount, floorCount);
        }
        return apartments;
    }

    private void fillWingApartments(OfInt randomMembers, ArrayList<Apartment> apartments, String wing, int buildingCount, int floorCount) {
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
