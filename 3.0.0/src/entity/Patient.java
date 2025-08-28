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
    private String phone;
    private String email;

    // Treatment history (used by Treatment module)
    private final ListInterface<Treatment> treatments = new ArrayList<>();

    public Patient() {}

    // Old-style ctor kept for compatibility if some legacy code uses it
    public Patient(String name, int age, String id) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Consultation-style ctor (no age)
    public Patient(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // Preferred unified ctor (has age)
    public Patient(String id, String name, int age, String phone, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
    }

    // -------- getters / setters --------
    public String getId() { return id; }
    public String getPatientId() { return id; } // alias for older code
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // -------- treatment history API --------
    public void addTreatment(Treatment t) { treatments.add(t); }
    public ListInterface<Treatment> getTreatments() { return treatments; }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Age: " + age;
    }
}
