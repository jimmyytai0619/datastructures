package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.Appointment;
import entity.Consultation;
import entity.Doctor;
import entity.Patient;
import java.time.LocalDateTime;
// NEW //
import java.time.Duration;

public class ConsultationModuleController {

    private final PatientDirectory patientDir;
    private final DoctorDirectory doctorDir;

    private final ListInterface<Appointment> appointments = new ArrayList<>();
    private final ListInterface<Consultation> consultations = new ArrayList<>();

    private int apptSeq = 1000;

    public ConsultationModuleController(PatientDirectory patientDir, DoctorDirectory doctorDir) {
        this.patientDir = patientDir;
        this.doctorDir = doctorDir;
    }

    // ---------- Appointment ID Generator ----------
    private String nextAppointmentId() {
        String id;
        do {
            id = "APT" + (++apptSeq);
        } while (findAppointmentById(id) != null);
        return id;
    }

    // ---------- Appointment ----------
    public Appointment createAppointmentAuto(String patientId, String doctorId,
                                             LocalDateTime dateTime, String purpose) {
        String id = nextAppointmentId();
        return createAppointment(id, patientId, doctorId, dateTime, purpose);
    }

    public Appointment createAppointment(String appointmentId, String patientId, String doctorId,
                                         LocalDateTime dateTime, String purpose) {
        if (findAppointmentById(appointmentId) != null) return null;
        Patient p = patientDir.findById(patientId);
        Doctor d = doctorDir.findById(doctorId);
        if (p == null || d == null) return null;
        Appointment a = new Appointment(appointmentId, p, d, dateTime, purpose);
        appointments.add(a);
        return a;
    }

    // ---------- Consultations ----------
    public Consultation conductConsultation(String consultationId, String appointmentId,
                                            String symptoms, String diagnosis, String notes) {
        if (findConsultationById(consultationId) != null) return null;
        Appointment appt = findAppointmentById(appointmentId);
        if (appt == null) return null;
        Consultation c = new Consultation(consultationId,
                                          appt.getPatient(),
                                          appt.getDoctor(),
                                          appt.getDateTime(),
                                          symptoms, diagnosis, notes);
        consultations.add(c);
        try { appt.setAttended(true); } catch (Exception ignore) {}
        return c;
    }

    public Appointment scheduleFollowUp(String newAppointmentId, String consultationId,
                                        LocalDateTime dateTime, String purpose) {
        if (findAppointmentById(newAppointmentId) != null) return null;
        Consultation c = findConsultationById(consultationId);
        if (c == null) return null;
        Appointment a = new Appointment(newAppointmentId, c.getPatient(), c.getDoctor(),
                                        dateTime, purpose);
        appointments.add(a);
        return a;
    }

    public Appointment scheduleFollowUpAuto(String consultationId,
                                            LocalDateTime dateTime, String purpose) {
        String id = nextAppointmentId();
        return scheduleFollowUp(id, consultationId, dateTime, purpose);
    }

    // ---------- Finder Methods ----------
    public Patient findPatientById(String id) { return patientDir.findById(id); }
    public Doctor findDoctorById(String id)   { return doctorDir.findById(id); }

    public Appointment findAppointmentById(String id) {
        for (int i = 1; i <= appointments.getNumberOfEntries(); i++) {
            Appointment a = appointments.getEntry(i);
            if (a.getAppointmentId().equalsIgnoreCase(id)) return a;
        }
        return null;
    }

    public Consultation findConsultationById(String id) {
        for (int i = 1; i <= consultations.getNumberOfEntries(); i++) {
            Consultation c = consultations.getEntry(i);
            if (c.getConsultationId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    // ---------- Expose Data for UI/Reports ----------
    public ListInterface<Appointment> getAppointments() { return appointments; }
    public ListInterface<Consultation> getConsultations() { return consultations; }
    public ListInterface<Patient> getPatients() { return patientDir.all(); }
    public ListInterface<Doctor> getDoctors() { return doctorDir.all(); }
    
    //NEW//
    public adt.ListInterface<Consultation> searchConsultationsByKeyword(String keyword) {
        adt.ListInterface<Consultation> out = new adt.ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return out;
        String k = keyword.toLowerCase();
        for (int i = 1; i <= consultations.getNumberOfEntries(); i++) {
            Consultation c = consultations.getEntry(i);
            String s1 = c.getSymptoms() == null ? "" : c.getSymptoms().toLowerCase();
            String s2 = c.getDiagnosis() == null ? "" : c.getDiagnosis().toLowerCase();
            String s3 = c.getNotes() == null ? "" : c.getNotes().toLowerCase();
            if (s1.contains(k) || s2.contains(k) || s3.contains(k)) out.add(c);
        }
        return out;
    }

    public String renderConsultationsTable() {
        int[] w = {12,10,14,10,14,19,16,16,16};
        String[] h = {"ConsultID","PatientID","PatientName","DoctorID","DoctorName","DateTime","Symptoms","Diagnosis","Notes"};
        StringBuilder sb = new StringBuilder();
        String sep = "+" + repeat("-", w[0]+2) + "+" + repeat("-", w[1]+2) + "+" + repeat("-", w[2]+2)
                   + "+" + repeat("-", w[3]+2) + "+" + repeat("-", w[4]+2) + "+" + repeat("-", w[5]+2)
                   + "+" + repeat("-", w[6]+2) + "+" + repeat("-", w[7]+2) + "+" + repeat("-", w[8]+2) + "+\n";
        sb.append(sep);
        sb.append("| ").append(cell(h[0], w[0])).append(" | ").append(cell(h[1], w[1])).append(" | ")
          .append(cell(h[2], w[2])).append(" | ").append(cell(h[3], w[3])).append(" | ")
          .append(cell(h[4], w[4])).append(" | ").append(cell(h[5], w[5])).append(" | ")
          .append(cell(h[6], w[6])).append(" | ").append(cell(h[7], w[7])).append(" | ")
          .append(cell(h[8], w[8])).append(" |\n");
        sb.append(sep);

        for (int i = 1; i <= consultations.getNumberOfEntries(); i++) {
            Consultation c = consultations.getEntry(i);
            String cid = nz(c.getConsultationId());
            String pid = c.getPatient()==null? "": nz(c.getPatient().getId());
            String pname = c.getPatient()==null? "": nz(c.getPatient().getName());
            String did = c.getDoctor()==null? "": nz(c.getDoctor().getDoctorId());
            String dname = c.getDoctor()==null? "": nz(c.getDoctor().getName());
            String dt = c.getDateTime()==null? "": c.getDateTime().toString();
            String sy = nz(c.getSymptoms());
            String dg = nz(c.getDiagnosis());
            String nt = nz(c.getNotes());

            sb.append("| ").append(cell(cid, w[0])).append(" | ").append(cell(pid, w[1])).append(" | ")
              .append(cell(pname, w[2])).append(" | ").append(cell(did, w[3])).append(" | ")
              .append(cell(dname, w[4])).append(" | ").append(cell(dt, w[5])).append(" | ")
              .append(cell(sy, w[6])).append(" | ").append(cell(dg, w[7])).append(" | ")
              .append(cell(nt, w[8])).append(" |\n");
        }
        sb.append(sep);
        return sb.toString();
    }

    private static String nz(String s) { return s == null ? "" : s.replace("\n"," ").trim(); }
    private static String repeat(String s, int n) { StringBuilder b=new StringBuilder(); for(int i=0;i<n;i++) b.append(s); return b.toString(); }
    private static String cell(String s, int w) {
        if (s == null) s = "";
        if (s.length() > w) s = s.substring(0, w-1) + "…";
        return String.format("%-" + w + "s", s);
    }


}

