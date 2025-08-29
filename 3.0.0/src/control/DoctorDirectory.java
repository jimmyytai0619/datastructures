package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.Doctor;
import entity.DutySlot;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DoctorDirectory {
    private final ListInterface<Doctor> doctors = new ArrayList<>();

    // Add an existing Doctor object
    public boolean add(Doctor d) {
        if (findById(d.getDoctorId()) != null) return false;
        doctors.add(d);
        return true;
    }

    // Register a new doctor
    public Doctor register(String id, String name, String specialization) {
        if (findById(id) != null) return null; // prevent duplicate
        Doctor d = new Doctor(id, name, specialization);
        doctors.add(d);
        return d;
    }

    // Find doctor by ID
    public Doctor findById(String id) {
        for (int i = 1; i <= doctors.getNumberOfEntries(); i++) {
            Doctor d = doctors.getEntry(i);
            if (d.getDoctorId().equalsIgnoreCase(id)) return d;
        }
        return null;
    }

    // Add a duty slot to a doctor
    public boolean addDutySlot(String doctorId, DayOfWeek day, LocalTime start, LocalTime end) {
        Doctor d = findById(doctorId);
        if (d == null || !start.isBefore(end)) return false;
        d.addDutySlot(new DutySlot(day, start, end));
        return true;
    }

    // Update doctor info
    public boolean update(String id, String name, String spec) {
        Doctor d = findById(id);
        if (d == null) return false;
        if (name != null && !name.isBlank()) d.setName(name);
        if (spec != null && !spec.isBlank()) d.setSpecialization(spec);
        return true;
    }

    // Remove doctor by ID
    public boolean remove(String id) {
        for (int i = 1; i <= doctors.getNumberOfEntries(); i++) {
            if (doctors.getEntry(i).getDoctorId().equalsIgnoreCase(id)) {
                doctors.remove(i);
                return true;
            }
        }
        return false;
    }

    // Get all doctors
    public ListInterface<Doctor> all() {
        return doctors;
    }

    // Helpers
    public int size() {
        return doctors.getNumberOfEntries();
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}


