package entity;

import adt.ArrayList;
import adt.ListInterface;

public class Patient {
    private String name;
    private int age;
    private String id;
    private ListInterface<Treatment> treatments;

    public Patient(String name, int age, String id) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.treatments = new ArrayList<>();
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getId() { return id; }
    public ListInterface<Treatment> getTreatments() { return treatments; }

    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Age: %d", id, name, age);
    }
}
