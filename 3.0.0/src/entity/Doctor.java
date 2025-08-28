package entity;

import java.io.Serializable;
import adt.ArrayList;
import adt.ListInterface;

public class Doctor implements Serializable {
    private String doctorId;
    private String name;
    private String specialization;

    private final ListInterface<DutySlot> schedule = new ArrayList<>();

    public Doctor(String doctorId, String name, String specialization) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
    }

    // Getters
    public String getDoctorId() { return doctorId; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }

    // Setters 
    public void setName(String name) {
        this.name = name;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    // Duty schedule management
    public void addDutySlot(DutySlot slot) {
        schedule.add(slot);
    }

    public int getScheduleNumberOfEntries() {
        return schedule.getNumberOfEntries();
    }

    public DutySlot getScheduleEntry(int i) {
        return schedule.getEntry(i); // 1-based
    }

    public ListInterface<DutySlot> getSchedule() {
        return schedule;
    }

    public int getScheduleLength() {
        return getScheduleNumberOfEntries();
    }

    @Override
    public String toString() {
        return doctorId + " - " + name + " (" + specialization + ")";
    }
}
