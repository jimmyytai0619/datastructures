package entity;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String name;
    private int age;
    private String id;
    private List<Treatment> treatments;

    public Patient(String name, int age, String id) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.treatments = new ArrayList<>();
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getId() {
        return id;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    // Add treatment
    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    @Override
    public String toString() {
        return "Patient ID: " + id + ", Name: " + name + ", Age: " + age;
    }
}
