package control;

import entity.Doctor;
import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorController {
    private final DoctorDirectory doctorDir;

    public DoctorController(DoctorDirectory doctorDir) {
        this.doctorDir = doctorDir;
    }

    public Doctor registerDoctor(String id, String name, String specialization) {
        return doctorDir.register(id, name, specialization);
    }

    public boolean addDutySlot(String doctorId, LocalDate date, LocalTime start, LocalTime end) {
        return doctorDir.addDutySlot(doctorId, date, start, end);
    }

    public boolean updateDoctor(String id, String name, String spec) {
        return doctorDir.update(id, name, spec);
    }

    public boolean removeDoctor(String id) {
        return doctorDir.remove(id);
    }

    public Doctor findDoctorById(String id) {
        return doctorDir.findById(id);
    }

    public Doctor findDoctorByIdBinary(String id) {
        return doctorDir.binarySearchById(id);
    }

    public String showSchedule(String doctorId) {
        Doctor d = doctorDir.findById(doctorId);
        if (d == null) return "Doctor not found.";
        StringBuilder sb = new StringBuilder("Schedule for ").append(d.getName()).append("\n");
        int n = d.getScheduleNumberOfEntries();
        if (n == 0) sb.append("(no duty slots)\n");
        else for (int i = 1; i <= n; i++) sb.append("- ").append(d.getScheduleEntry(i)).append("\n");
        return sb.toString();
    }

    public adt.ListInterface<Doctor> getDoctors() {
        return doctorDir.all();
    }

    // --- Sorting features ---
    public void sortDoctorsByName() {
        doctorDir.sortByName();
    }

    public void sortDoctorsBySpecialization() {
        doctorDir.sortBySpecialization();
    }
}
