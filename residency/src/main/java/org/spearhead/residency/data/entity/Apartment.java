package org.spearhead.residency.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String wing;
    private String building;
    private int floor;
    private int number;
    private int memberCount;

    public Apartment() {
    }

    public Apartment(String wing, String building, int floor, int number, int memberCount) {
        this.wing = wing;
        this.building = building;
        this.floor = floor;
        this.number = number;
        this.memberCount = memberCount;
    }

    public String getWing() {
        return wing;
    }

    public String getBuilding() {
        return building;
    }

    public int getFloor() {
        return floor;
    }

    public int getNumber() {
        return number;
    }

    public int getMemberCount() {
        return memberCount;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("{").append("wing='").append(wing).append('\'').append(", building='")
                .append(building).append('\'').append(", floor=").append(floor).append(", number=").append(number)
                .append(", memberCount=").append(memberCount).append('}').toString();
    }
}
