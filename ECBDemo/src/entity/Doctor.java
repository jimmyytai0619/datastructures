package entity;

import java.io.Serializable;
import adt.ListInterface;
import adt.ArrayList;

public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String doctorID;
    private String name;
    private String specialization;
    private String phone;
    private double consultationFee;
    private AvailabilityStatus status;
    private ListInterface<DutySchedule> schedules;

    public Doctor(String doctorID, String name, String specialization, String phone, double consultationFee) {
        this.doctorID = doctorID;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.consultationFee = consultationFee;
        this.status = AvailabilityStatus.ACTIVE;
        this.schedules = new ArrayList<>();
    }

    // getters and setters
    public String getDoctorID() { return doctorID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }
    public AvailabilityStatus getStatus() { return status; }
    public void setStatus(AvailabilityStatus status) { this.status = status; }
    public ListInterface<DutySchedule> getSchedules() { return schedules; }

    // entity-level convenience
    public void addSchedule(DutySchedule s) { schedules.add(s); }

    // remove schedule by scheduleID (1-based ADT indexing)
    public boolean removeScheduleById(String scheduleID) {
        int n = schedules.getNumberOfEntries();
        for (int i = 1; i <= n; i++) {
            DutySchedule ds = schedules.getEntry(i);
            if (ds.getScheduleID().equals(scheduleID)) {
                schedules.remove(i);
                return true;
            }
        }
        return false;
    }

    // find schedule in this doctor's schedules
    public DutySchedule findScheduleById(String scheduleID) {
        int n = schedules.getNumberOfEntries();
        for (int i = 1; i <= n; i++) {
            DutySchedule ds = schedules.getEntry(i);
            if (ds.getScheduleID().equals(scheduleID)) return ds;
        }
        return null;
    }

    @Override
    public String toString() {
        return doctorID + " | " + name + " | " + specialization + " | " +
               "Phone:" + phone + " | Fee:" + consultationFee + " | " + status;
    }
}
