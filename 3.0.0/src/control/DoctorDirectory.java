package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.Doctor;
import entity.DutySlot;

import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorDirectory {
    private final ListInterface<Doctor> doctors = new ArrayList<>();
    private boolean sortedById = false; 

    // Add an existing Doctor object
    public boolean add(Doctor d) {
        if (findById(d.getDoctorId()) != null) return false;
        doctors.add(d);
        sortedById = false; 
        return true;
    }

    // Register a new doctor
    public Doctor register(String id, String name, String specialization) {
        if (findById(id) != null) return null; // prevent duplicate
        Doctor d = new Doctor(id, name, specialization);
        doctors.add(d);
        sortedById = false;
        return d;
    }

    public Doctor findById(String id) {
        for (int i = 1; i <= doctors.getNumberOfEntries(); i++) {
            Doctor d = doctors.getEntry(i);
            if (d.getDoctorId().equalsIgnoreCase(id)) return d;
        }
        return null;
    }

    // Add a duty slot to a doctor
    public boolean addDutySlot(String doctorId, LocalDate date, LocalTime start, LocalTime end) {
        Doctor d = findById(doctorId);
        if (d == null || !start.isBefore(end)) return false;
        d.addDutySlot(new DutySlot(date, start, end));
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

    // --- Sorting by ID (for binary search) ---
    private void ensureSortedById() {
        if (sortedById) return;

        int n = doctors.getNumberOfEntries();
        for (int i = 1; i <= n - 1; i++) {
            for (int j = i + 1; j <= n; j++) {
                Doctor d1 = doctors.getEntry(i);
                Doctor d2 = doctors.getEntry(j);
                if (d1.getDoctorId().compareToIgnoreCase(d2.getDoctorId()) > 0) {
                    // swap
                    doctors.replace(i, d2);
                    doctors.replace(j, d1);
                }
            }
        }

        sortedById = true;
    }

    // --- Binary Search by Doctor ID ---
    public Doctor binarySearchById(String id) {
        ensureSortedById(); 

        int left = 1;
        int right = doctors.getNumberOfEntries();

        while (left <= right) {
            int mid = (left + right) / 2;
            Doctor midDoctor = doctors.getEntry(mid);
            int cmp = midDoctor.getDoctorId().compareToIgnoreCase(id);

            if (cmp == 0) {
                return midDoctor; 
            } else if (cmp < 0) {
                left = mid + 1; 
            } else {
                right = mid - 1; 
            }
        }

        return null; 
    }

    // --- Sorting by Name ---
    public void sortByName() {
        int n = doctors.getNumberOfEntries();
        for (int i = 1; i <= n - 1; i++) {
            for (int j = 1; j <= n - i; j++) {
                Doctor d1 = doctors.getEntry(j);
                Doctor d2 = doctors.getEntry(j + 1);
                if (d1.getName().compareToIgnoreCase(d2.getName()) > 0) {
                    doctors.replace(j, d2);
                    doctors.replace(j + 1, d1);
                }
            }
        }
        sortedById = false; 
    }

    // --- Sorting by Specialization ---
    public void sortBySpecialization() {
        int n = doctors.getNumberOfEntries();
        for (int i = 1; i <= n - 1; i++) {
            for (int j = 1; j <= n - i; j++) {
                Doctor d1 = doctors.getEntry(j);
                Doctor d2 = doctors.getEntry(j + 1);
                if (d1.getSpecialization().compareToIgnoreCase(d2.getSpecialization()) > 0) {
                    doctors.replace(j, d2);
                    doctors.replace(j + 1, d1);
                }
            }
        }
        sortedById = false;
    }
}
