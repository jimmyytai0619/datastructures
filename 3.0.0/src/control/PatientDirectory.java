package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.Patient;

public class PatientDirectory {
    private final ListInterface<Patient> patients = new ArrayList<>();

    public boolean add(Patient p) {
        if (findById(p.getId()) != null) return false;
        patients.add(p);
        return true;
    }

    public Patient register(String id, String name, int age, String phone, String email) {
        if (findById(id) != null) return null;
        Patient p = new Patient(id, name, age, phone, email);
        patients.add(p);
        return p;
    }

    public Patient findById(String id) {
        for (int i = 1; i <= patients.getNumberOfEntries(); i++) {
            Patient p = patients.getEntry(i);
            if (p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    public ListInterface<Patient> all() { return patients; }
    public int size() { return patients.getNumberOfEntries(); }
    public boolean isEmpty() { return size() == 0; }
}
