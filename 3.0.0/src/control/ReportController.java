package control;

import adt.ListInterface;
import entity.Patient;
import entity.Doctor;
import entity.Appointment;
import entity.Consultation;

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
}
