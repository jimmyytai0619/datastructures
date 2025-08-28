package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.Patient;
import entity.Doctor;
import entity.Appointment;
import entity.Consultation;
import entity.DutySlot;
import java.time.LocalDateTime;

/**
 * ConsultationModule controller
 * 
 * @author yizhe
 */
public class ConsultationModuleController {


    private final ListInterface<Patient> patients = new ArrayList<>();
    private final ListInterface<Doctor> doctors = new ArrayList<>();
    private final ListInterface<Appointment> appointments = new ArrayList<>();
    private final ListInterface<Consultation> consultations = new ArrayList<>();

    // ---------- CRUD / Register ----------
    public Patient registerPatient(String id, String name, String phone, String email) {
        if (findPatientById(id) != null) return null;
        Patient p = new Patient(id, name, phone, email);
        patients.add(p);
        return p;
    }

    public Doctor registerDoctor(String id, String name, String specialization) {
        if (findDoctorById(id) != null) return null;
        Doctor d = new Doctor(id, name, specialization);
        doctors.add(d);
        return d;
    }

    public Appointment createAppointment(String appointmentId, String patientId, String doctorId,
                                         LocalDateTime dateTime, String purpose) {
        if (findAppointmentById(appointmentId) != null) return null;
        Patient p = findPatientById(patientId);
        Doctor d = findDoctorById(doctorId);
        if (p == null || d == null) return null;
        Appointment a = new Appointment(appointmentId, p, d, dateTime, purpose);
        appointments.add(a);
        return a;
    }

    public Consultation conductConsultation(String consultationId, String appointmentId,
                                            String symptoms, String diagnosis, String notes) {
        if (findConsultationById(consultationId) != null) return null;
        Appointment appt = findAppointmentById(appointmentId);
        if (appt == null) return null;
        Consultation c = new Consultation(consultationId, appt.getPatient(), appt.getDoctor(),
                                          appt.getDateTime(), symptoms, diagnosis, notes);
        consultations.add(c);
        try { appt.setAttended(true); } catch (Exception ignore) {}
        return c;
    }

    public Appointment scheduleFollowUp(String newAppointmentId, String consultationId,
                                        LocalDateTime dateTime, String purpose) {
        if (findAppointmentById(newAppointmentId) != null) return null;
        Consultation c = findConsultationById(consultationId);
        if (c == null) return null;
        Appointment a = new Appointment(newAppointmentId, c.getPatient(), c.getDoctor(), dateTime, purpose);
        appointments.add(a);
        return a;
    }

    // ---------- Doctor duty slots ----------
    public boolean addDutySlot(String doctorId,
                               java.time.DayOfWeek day,
                               java.time.LocalTime start,
                               java.time.LocalTime end) {
        Doctor d = findDoctorById(doctorId);
        if (d == null || !start.isBefore(end)) return false;
        d.addDutySlot(new DutySlot(day, start, end));
        return true;
    }

    public String listDoctors() {
        StringBuilder sb = new StringBuilder("=== Doctors ===\n");
        for (int i = 1; i <= doctors.getNumberOfEntries(); i++) {
            sb.append(doctors.getEntry(i)).append("\n");
        }
        return sb.toString();
    }

    public String showSchedule(String doctorId) {
        Doctor d = findDoctorById(doctorId);
        if (d == null) return "Doctor not found.";
        StringBuilder sb = new StringBuilder("Schedule for ").append(d.getName()).append("\n");
        int n = d.getScheduleNumberOfEntries();
        if (n == 0) sb.append("(no duty slots)\n");
        else for (int i = 1; i <= n; i++) sb.append("- ").append(d.getScheduleEntry(i)).append("\n");
        return sb.toString();
    }

    // ---------- Finders ----------
    public Patient findPatientById(String id) {
        for (int i = 1; i <= patients.getNumberOfEntries(); i++) {
            Patient p = patients.getEntry(i);
            if (p.getId().equalsIgnoreCase(id) || p.getPatientId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    public Doctor findDoctorById(String id) {
        for (int i = 1; i <= doctors.getNumberOfEntries(); i++) {
            Doctor d = doctors.getEntry(i);
            if (d.getDoctorId().equalsIgnoreCase(id)) return d;
        }
        return null;
    }

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

     //find doctor by ID
    public Doctor findDoctor(String id) {
        for (int i = 1; i <= doctors.getNumberOfEntries(); i++) {
            Doctor d = doctors.getEntry(i);
            if (d.getDoctorId().equals(id)) return d;
        }
        return null;
    }

    //remove doctor by ID
    public boolean removeDoctor(String id) {
        for (int i = 1; i <= doctors.getNumberOfEntries(); i++) {
            Doctor d = doctors.getEntry(i);
            if (d.getDoctorId().equals(id)) {
                doctors.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public boolean updateDoctor(String id, String newName, String newSpec) {
    Doctor d = findDoctorById(id);
    if (d == null) return false;
    if (newName != null && !newName.isBlank()) d.setName(newName);
    if (newSpec != null && !newSpec.isBlank()) d.setSpecialization(newSpec);
    return true;
}
    
    // ---------- Getters for UI/Reports ----------
    public ListInterface<Patient> getPatients() { return patients; }
    public ListInterface<Doctor> getDoctors() { return doctors; }
    public ListInterface<Appointment> getAppointments() { return appointments; }
    public ListInterface<Consultation> getConsultations() { return consultations; }
}
