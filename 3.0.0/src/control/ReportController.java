package control;

import adt.ListInterface;
import entity.Patient;
import entity.Doctor;
import entity.Appointment;
import entity.Consultation;
import entity.DutySlot;  

public class ReportController {
    private final ConsultationModuleController controller;

    public ReportController(ConsultationModuleController controller) {
        this.controller = controller;
    }

    // Report 1: consultations per doctor
    public String generateConsultationsPerDoctorReport() {
        StringBuilder sb = new StringBuilder("=== Report: Consultations per Doctor ===\n");
        ListInterface<Doctor> ds = controller.getDoctors();
        ListInterface<Consultation> cs = controller.getConsultations();
        for (int i = 1; i <= ds.getNumberOfEntries(); i++) {
            Doctor d = ds.getEntry(i);
            int count = 0;
            for (int j = 1; j <= cs.getNumberOfEntries(); j++) {
                if (cs.getEntry(j).getDoctor().getDoctorId().equals(d.getDoctorId())) count++;
            }
            sb.append(d.getDoctorId()).append(" ").append(d.getName()).append(": ").append(count).append("\n");
        }
        if (ds.getNumberOfEntries() == 0) sb.append("(no doctors)\n");
        return sb.toString();
    }

    // Report 2: upcoming (not attended) appointments per patient
    public String generateUpcomingAppointmentsPerPatientReport() {
        StringBuilder sb = new StringBuilder("=== Report: Upcoming Appointments per Patient ===\n");
        ListInterface<Patient> ps = controller.getPatients();
        ListInterface<Appointment> as = controller.getAppointments();
        for (int i = 1; i <= ps.getNumberOfEntries(); i++) {
            Patient p = ps.getEntry(i);
            int count = 0;
            for (int j = 1; j <= as.getNumberOfEntries(); j++) {
                Appointment a = as.getEntry(j);
                boolean attended = false;
                try { attended = a.isAttended(); } catch (Exception ignore) {}
                if (a.getPatient().getId().equalsIgnoreCase(p.getId()) && !attended) count++;
            }
            sb.append(p.getId()).append(" ").append(p.getName()).append(": ").append(count).append("\n");
        }
        if (ps.getNumberOfEntries() == 0) sb.append("(no patients)\n");
        return sb.toString();
    }

    // Report 3: duty schedules per doctor
    public String generateDutySchedulesPerDoctorReport() {
        StringBuilder sb = new StringBuilder("=== Report: Duty Schedules per Doctor ===\n");
        ListInterface<Doctor> ds = controller.getDoctors();
        for (int i = 1; i <= ds.getNumberOfEntries(); i++) {
            Doctor d = ds.getEntry(i);
            sb.append("\nDoctor: ").append(d.getDoctorId()).append(" - ").append(d.getName())
              .append(" (").append(d.getSpecialization()).append(")\n");

            if (d.getScheduleNumberOfEntries() == 0) {
                sb.append("   (no duty schedules)\n");
            } else {
                for (int j = 1; j <= d.getScheduleNumberOfEntries(); j++) {
                    DutySlot slot = d.getScheduleEntry(j);
                    sb.append("   • ").append(slot).append("\n");
                }
            }
        }
        if (ds.getNumberOfEntries() == 0) sb.append("(no doctors)\n");
        return sb.toString();
    }
    
    // Report: Monthly Duty Roster
public String monthlyDutyRoster(int month, int year) {
    StringBuilder sb = new StringBuilder("=== Monthly Duty Roster " + month + "/" + year + " ===\n");
    var ds = controller.getDoctors();
    for (int i = 1; i <= ds.getNumberOfEntries(); i++) {
        Doctor d = ds.getEntry(i);
        sb.append(d).append("\n");
        for (int j = 1; j <= d.getScheduleNumberOfEntries(); j++) {
            sb.append("  - ").append(d.getScheduleEntry(j)).append("\n");
        }
    }
    if (ds.getNumberOfEntries() == 0) sb.append("(no doctors)\n");
    return sb.toString();
}

// Report: Availability Summary (next N days)
public String availabilitySummary(int days) {
    StringBuilder sb = new StringBuilder("=== Availability Summary (next " + days + " days) ===\n");
    var ds = controller.getDoctors();
    for (int i = 1; i <= ds.getNumberOfEntries(); i++) {
        Doctor d = ds.getEntry(i);
        sb.append(d).append(" has ").append(d.getScheduleNumberOfEntries()).append(" duty slots\n");
    }
    if (ds.getNumberOfEntries() == 0) sb.append("(no doctors)\n");
    return sb.toString();
}

// Report: Utilization Report (last N days)
public String utilizationReport(int days) {
    StringBuilder sb = new StringBuilder("=== Utilization Report (last " + days + " days) ===\n");
    var ds = controller.getDoctors();
    var cs = controller.getConsultations();
    for (int i = 1; i <= ds.getNumberOfEntries(); i++) {
        Doctor d = ds.getEntry(i);
        int count = 0;
        for (int j = 1; j <= cs.getNumberOfEntries(); j++) {
            if (cs.getEntry(j).getDoctor().getDoctorId().equals(d.getDoctorId())) {
                count++;
            }
        }
        sb.append(d).append(": ").append(count).append(" consultations\n");
    }
    if (ds.getNumberOfEntries() == 0) sb.append("(no doctors)\n");
    return sb.toString();
}

    
}

