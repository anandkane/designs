package org.spearhead.residency.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spearhead.residency.data.entity.Apartment;
import org.spearhead.residency.data.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResidencyServiceITest {
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private ResidencyService residencyService;

    @Before
    public void setUp() throws Exception {
        seedData();
    }

    @Test
    public void getResidencyComposite() throws Exception {
        residencyService.getResidencyComposite();
    }

    private void seedData() {
        String[] wings = {"A", "B", "C"};
        PrimitiveIterator.OfInt randomBldgs = new Random().ints(4, 7).iterator();
        PrimitiveIterator.OfInt randomFloors = new Random().ints(8, 20).iterator();
        PrimitiveIterator.OfInt randomMembers = new Random().ints(0, 7).iterator();
        List<Apartment> apartments = new ArrayList<>();
        for (String wing : wings) {
            int buildingCount = randomBldgs.nextInt();
            int floorCount = randomFloors.nextInt();

            for (int i = 1; i <= buildingCount; i++) {
                for (int j = 1; j <= floorCount; j++) {
                    for (int k = 1; k <= 4; k++) {
                        int memberCount = randomMembers.nextInt();
                        apartments.add(new Apartment(wing, wing + i, j, j * 100 + k, memberCount));
                    }
                }
            }
        }
        apartmentRepository.save(apartments);
    }
}