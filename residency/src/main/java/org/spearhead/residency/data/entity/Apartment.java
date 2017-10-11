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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWing() {
        return wing;
    }

    public void setWing(String wing) {
        this.wing = wing;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("{").append("wing='").append(wing).append('\'').append(", building='")
                .append(building).append('\'').append(", floor=").append(floor).append(", number=").append(number)
                .append(", memberCount=").append(memberCount).append('}').toString();
    }
}
