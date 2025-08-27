package control;

import adt.ListInterface;
import adt.ArrayList;
import entity.Doctor;
import entity.DutySchedule;
import entity.AvailabilityStatus;
import dao.DoctorDAO;
import utility.IDGenerator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DoctorControl {
    private ListInterface<Doctor> doctors;

    public DoctorControl() {
        // load persisted doctors if any
        doctors = DoctorDAO.loadDoctors();
        if (doctors.getNumberOfEntries() == 0) {
            loadSampleData(); // sample data to demonstrate features
            DoctorDAO.saveDoctors(doctors);
        }
    }

    // Add new doctor (validates uniqueness of doctorID assigned automatically)
    public Doctor addDoctor(String name, String specialization, String phone, double fee) {
        String id = IDGenerator.nextDoctorID();
        Doctor d = new Doctor(id, name.trim(), specialization.trim(), phone.trim(), fee);
        doctors.add(d);
        DoctorDAO.saveDoctors(doctors);
        return d;
    }

    // Update doctor (only allowed fields, must exist)
    public boolean updateDoctor(String doctorID, String name, String specialization, String phone, Double fee, AvailabilityStatus status) {
        int idx = findDoctorIndex(doctorID);
        if (idx < 1) return false;
        Doctor d = doctors.getEntry(idx);
        if (name != null && !name.isBlank()) d.setName(name);
        if (specialization != null && !specialization.isBlank()) d.setSpecialization(specialization);
        if (phone != null && !phone.isBlank()) d.setPhone(phone);
        if (fee != null) d.setConsultationFee(fee);
        if (status != null) d.setStatus(status);
        doctors.replace(idx, d);
        DoctorDAO.saveDoctors(doctors);
        return true;
    }

    // Remove doctor (soft remove is allowed; here we physically remove)
    public boolean removeDoctor(String doctorID) {
        int idx = findDoctorIndex(doctorID);
        if (idx < 1) return false;
        doctors.remove(idx);
        DoctorDAO.saveDoctors(doctors);
        return true;
    }

    // Assign schedule but validate: doctor exists, schedule no overlap for that doctor
    public boolean assignSchedule(DutySchedule s) {
        int idx = findDoctorIndex(s.getDoctorID());
        if (idx < 1) return false;
        Doctor d = doctors.getEntry(idx);

        // check overlapping with existing schedules of this doctor
        int n = d.getSchedules().getNumberOfEntries();
        for (int i = 1; i <= n; i++) {
            DutySchedule existing = d.getSchedules().getEntry(i);
            if (isOverlapping(existing, s)) {
                return false; // violation
            }
        }

        d.addSchedule(s);
        doctors.replace(idx, d);
        DoctorDAO.saveDoctors(doctors);
        return true;
    }

    private boolean isOverlapping(DutySchedule a, DutySchedule b) {
        if (!a.getDate().equals(b.getDate())) return false;
        return !(b.getEndTime().isBefore(a.getStartTime()) || b.getStartTime().isAfter(a.getEndTime()));
    }

    public Doctor findDoctorById(String id) {
        int idx = findDoctorIndex(id);
        if (idx >= 1) return doctors.getEntry(idx);
        return null;
    }

    public ListInterface<Doctor> listAllDoctors() { return doctors; }

    // find 1-based index
    private int findDoctorIndex(String id) {
        int n = doctors.getNumberOfEntries();
        for (int i = 1; i <= n; i++) {
            if (doctors.getEntry(i).getDoctorID().equals(id)) return i;
        }
        return -1;
    }

    // business helper: return list of doctors available on a given date
    public ListInterface<Doctor> doctorsAvailableOn(LocalDate date) {
        ListInterface<Doctor> result = new ArrayList<>();
        int n = doctors.getNumberOfEntries();
        for (int i = 1; i <= n; i++) {
            Doctor d = doctors.getEntry(i);
            if (d.getStatus() != AvailabilityStatus.ACTIVE) continue;
            boolean hasSchedule = false;
            int sN = d.getSchedules().getNumberOfEntries();
            for (int j = 1; j <= sN; j++) {
                if (d.getSchedules().getEntry(j).getDate().equals(date)) {
                    hasSchedule = true;
                    break;
                }
            }
            if (!hasSchedule) result.add(d);
        }
        return result;
    }

    // sample data for demonstration
    private void loadSampleData() {
        Doctor d1 = new Doctor(IDGenerator.nextDoctorID(), "Dr. Aaron Tan", "General", "012-555-0001", 30.0);
        Doctor d2 = new Doctor(IDGenerator.nextDoctorID(), "Dr. Mei Wong", "Paediatrics", "012-555-0002", 35.0);
        Doctor d3 = new Doctor(IDGenerator.nextDoctorID(), "Dr. Raj Kumar", "Orthopaedics", "012-555-0003", 45.0);

        d1.addSchedule(new DutySchedule(IDGenerator.nextScheduleID(), d1.getDoctorID(), java.time.LocalDate.now().plusDays(1),
                java.time.LocalTime.of(9,0), java.time.LocalTime.of(12,0), "Clinic A", "Morning"));

        d2.addSchedule(new DutySchedule(IDGenerator.nextScheduleID(), d2.getDoctorID(), java.time.LocalDate.now(),
                java.time.LocalTime.of(10,0), java.time.LocalTime.of(13,0), "Clinic B", "Morning"));

        doctors.add(d1);
        doctors.add(d2);
        doctors.add(d3);
    }
}
