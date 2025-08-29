package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.Appointment;
import entity.Consultation;
import entity.Doctor;
import entity.Patient;

import java.time.LocalDateTime;

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
}

