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

	// === Unified fields for system-wide Patient ===
	private String phone;
	private String email;

	// Adapter getters to keep old/new modules compatible
	public String getPatientId(){ return getId(); } // alias for A-side code
	public String getPhone(){ return phone; }
	public String getEmail(){ return email; }
	public void setPhone(String p){ this.phone = p; }
	public void setEmail(String e){ this.email = e; }

	// Overloaded constructor used by Consultation module
	public Patient(String id, String name, String phone, String email){
		// try to set id & name via fields where possible
		try { java.lang.reflect.Field f = this.getClass().getDeclaredField("id"); f.setAccessible(true); f.set(this, id); } catch (Exception ignored) {}
		try { java.lang.reflect.Field f = this.getClass().getDeclaredField("name"); f.setAccessible(true); f.set(this, name); } catch (Exception ignored) {}
		this.phone = phone;
		this.email = email;
	}

}
