package entity;

import java.io.Serializable;
import adt.ArrayList;
import adt.ListInterface;

/**
 * Unified Patient entity for the whole system.
 * - id / name / age / phone / email
 * - treatment history via our ADT ListInterface (1-based)
 */
public class Patient implements Serializable {
    private String id;
    private String name;
    private int age;
    private String contactNumber; 
    private String email;

    private final ListInterface<Treatment> treatments = new ArrayList<>();

    public Patient() {}

    public Patient(String name, int age, String id) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Patient(String id, String name, String contactNumber, String email) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public Patient(String id, String name, int age, String contactNumber, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    // getters / setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public void addTreatment(Treatment t) { treatments.add(t); }
    public ListInterface<Treatment> getTreatments() { return treatments; }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Age: " + age + " | Contact: " + contactNumber;
    }
}
