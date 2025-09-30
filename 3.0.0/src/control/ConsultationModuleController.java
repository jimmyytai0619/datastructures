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

    public String exportConsultationsCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("consultationId,patientId,patientName,doctorId,doctorName,datetime,symptoms,diagnosis,notes\n");
        for (int i = 1; i <= consultations.getNumberOfEntries(); i++) {
            Consultation c = consultations.getEntry(i);
            String pid = c.getPatient()==null? "": c.getPatient().getId();
            String pname = c.getPatient()==null? "": c.getPatient().getName();
            String did = c.getDoctor()==null? "": c.getDoctor().getDoctorId();
            String dname = c.getDoctor()==null? "": c.getDoctor().getName();
            String dt = c.getDateTime()==null? "": c.getDateTime().toString();
            String sy = c.getSymptoms()==null? "": c.getSymptoms().replace(",", " ");
            String dg = c.getDiagnosis()==null? "": c.getDiagnosis().replace(",", " ");
            String nt = c.getNotes()==null? "": c.getNotes().replace(",", " ");
            sb.append(c.getConsultationId()).append(",")
              .append(pid).append(",")
              .append(pname).append(",")
              .append(did).append(",")
              .append(dname).append(",")
              .append(dt).append(",")
              .append(sy).append(",")
              .append(dg).append(",")
              .append(nt).append("\n");
        }
        return sb.toString();
    }

    public String exportAppointmentsCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("appointmentId,patientId,patientName,doctorId,doctorName,datetime,attended,purpose\n");
        for (int i = 1; i <= appointments.getNumberOfEntries(); i++) {
            Appointment a = appointments.getEntry(i);
            String pid = a.getPatient()==null? "": a.getPatient().getId();
            String pname = a.getPatient()==null? "": a.getPatient().getName();
            String did = a.getDoctor()==null? "": a.getDoctor().getDoctorId();
            String dname = a.getDoctor()==null? "": a.getDoctor().getName();
            String dt = a.getDateTime()==null? "": a.getDateTime().toString();
            boolean attended = false;
            try { attended = a.isAttended(); } catch (Exception ignore) {}
            String pur = a.getPurpose()==null? "": a.getPurpose().replace(",", " ");
            sb.append(a.getAppointmentId()).append(",")
              .append(pid).append(",")
              .append(pname).append(",")
              .append(did).append(",")
              .append(dname).append(",")
              .append(dt).append(",")
              .append(attended).append(",")
              .append(pur).append("\n");
        }
        return sb.toString();
    }

}

