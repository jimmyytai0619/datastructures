package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.Doctor;
import entity.DutySlot;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DoctorDirectory {
    private final ListInterface<Doctor> doctors = new ArrayList<>();

    public boolean add(Doctor d) {
        if (findById(d.getDoctorId()) != null) return false;
        doctors.add(d);
        return true;
    }

    public Doctor register(String id, String name, String specialization) {
        if (findById(id) != null) return null;
        Doctor d = new Doctor(id, name, specialization);
        doctors.add(d);
        return d;
    }

    public Doctor findById(String id) {
        for (int i = 1; i <= doctors.getNumberOfEntries(); i++) {
            Doctor d = doctors.getEntry(i);
            if (d.getDoctorId().equalsIgnoreCase(id)) return d;
        }
        return null;
    }

 
    public boolean addDutySlot(String doctorId, DayOfWeek day, LocalTime start, LocalTime end) {
        Doctor d = findById(doctorId);
        if (d == null || !start.isBefore(end)) return false;
        d.addDutySlot(new DutySlot(day, start, end));
        return true;
    }

    public ListInterface<Doctor> all() { return doctors; }
    public int size() { return doctors.getNumberOfEntries(); }
    public boolean isEmpty() { return size() == 0; }
}
