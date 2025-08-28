package boundary;

import control.ConsultationModuleController;
import control.ReportController;
import entity.Appointment;
import entity.Consultation;
import entity.Doctor;
import entity.Patient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class ConsultationModuleUI {
    private final ConsultationModuleController controller;
    private final ReportController reportController;
    private final Scanner scanner = new Scanner(System.in);

    public ConsultationModuleUI(ConsultationModuleController controller) {
        this.controller = controller;
        this.reportController = new ReportController(controller);
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Consultation Management (Module 3) ===");
            System.out.println("1. Register Patient");
            System.out.println("2. Register Doctor");
            System.out.println("3. Create Appointment");
            System.out.println("4. Conduct Consultation");
            System.out.println("5. Schedule Follow-up Appointment");
            System.out.println("6. List Patients");
            System.out.println("7. List Doctors");
            System.out.println("8. List Appointments");
            System.out.println("9. List Consultations");
            System.out.println("10. Report: Consultations per Doctor");
            System.out.println("11. Report: Upcoming Appointments per Patient");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> registerPatient();
                case "2" -> registerDoctor();
                case "3" -> createAppointment();
                case "4" -> conductConsultation();
                case "5" -> scheduleFollowUp();
                case "6" -> listPatients();
                case "7" -> listDoctors();
                case "8" -> listAppointments();
                case "9" -> listConsultations();
                case "10" -> System.out.println(reportController.generateConsultationsPerDoctorReport());
                case "11" -> System.out.println(reportController.generateUpcomingAppointmentsPerPatientReport());
                case "0" -> exit = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void registerPatient() {
        System.out.print("Patient ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        Patient p = controller.registerPatient(id, name, phone, email);
        System.out.println(p == null ? "Duplicate patient ID." : "Registered: " + id);
    }

    private void registerDoctor() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Specialization: ");
        String spec = scanner.nextLine().trim();
        Doctor d = controller.registerDoctor(id, name, spec);
        System.out.println(d == null ? "Duplicate doctor ID." : "Registered: " + id);
    }

    private void createAppointment() {
        System.out.print("Appointment ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Patient ID: ");
        String pid = scanner.nextLine().trim();
        System.out.print("Doctor ID: ");
        String did = scanner.nextLine().trim();
        LocalDate date = readDate("Date (YYYY-MM-DD): ");
        LocalTime time = readTime("Time (HH:MM): ");
        System.out.print("Purpose: ");
        String purpose = scanner.nextLine().trim();
        Appointment a = controller.createAppointment(id, pid, did, LocalDateTime.of(date, time), purpose);
        System.out.println(a == null ? "Invalid patient/doctor ID or duplicate appointment." : "Created: " + id);
    }

    private void conductConsultation() {
        System.out.print("Consultation ID: ");
        String cid = scanner.nextLine().trim();
        System.out.print("Appointment ID: ");
        String aid = scanner.nextLine().trim();
        System.out.print("Symptoms: ");
        String symptoms = scanner.nextLine().trim();
        System.out.print("Diagnosis: ");
        String diagnosis = scanner.nextLine().trim();
        System.out.print("Notes: ");
        String notes = scanner.nextLine().trim();
        Consultation c = controller.conductConsultation(cid, aid, symptoms, diagnosis, notes);
        System.out.println(c == null ? "Invalid appointment ID or duplicate consultation." : "Consultation recorded: " + cid);
    }

    private void scheduleFollowUp() {
        System.out.print("Existing Consultation ID: ");
        String cid = scanner.nextLine().trim();
        System.out.print("New Appointment ID: ");
        String aid = scanner.nextLine().trim();
        LocalDate date = readDate("Follow-up Date (YYYY-MM-DD): ");
        LocalTime time = readTime("Time (HH:MM): ");
        System.out.print("Purpose: ");
        String purpose = scanner.nextLine().trim();
        Appointment a = controller.scheduleFollowUp(aid, cid, LocalDateTime.of(date, time), purpose);
        System.out.println(a == null ? "Invalid consultation ID or duplicate appointment." : "Follow-up scheduled: " + aid);
    }

    private void listPatients() {
        var list = controller.getPatients();
        System.out.println("Patients:");
        if (list.getNumberOfEntries() == 0) { System.out.println("(none)"); return; }
        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            System.out.println("- " + list.getEntry(i));
        }
    }

    private void listDoctors() {
        var list = controller.getDoctors();
        System.out.println("Doctors:");
        if (list.getNumberOfEntries() == 0) { System.out.println("(none)"); return; }
        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            Doctor d = list.getEntry(i);
            System.out.println("- " + d.getDoctorId() + " " + d.getName() + " (" + d.getSpecialization() + ")");
        }
    }

    private void listAppointments() {
        var list = controller.getAppointments();
        System.out.println("Appointments:");
        if (list.getNumberOfEntries() == 0) { System.out.println("(none)"); return; }
        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            Appointment a = list.getEntry(i);
            String done = "";
            try { done = a.isAttended() ? " (done)" : ""; } catch (Exception ignore) {}
            System.out.println("- " + a.getAppointmentId() + ": " + a.getPatient().getName()
                    + " with " + a.getDoctor().getName() + " on " + a.getDateTime() + done);
        }
    }

    private void listConsultations() {
        var list = controller.getConsultations();
        System.out.println("Consultations:");
        if (list.getNumberOfEntries() == 0) { System.out.println("(none)"); return; }
        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            Consultation c = list.getEntry(i);
            System.out.println("- " + c.getConsultationId() + ": " + c.getPatient().getName()
                    + " with " + c.getDoctor().getName() + " on " + c.getDateTime());
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = scanner.nextLine().trim();
                return LocalDate.parse(s);
            } catch (Exception e) {
                System.out.println("Invalid date.");
            }
        }
    }

     
    private LocalTime readTime(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = scanner.nextLine().trim();
                return LocalTime.parse(s.length()==5 ? s + ":00" : s);
            } catch (Exception e) {
                System.out.println("Invalid time.");
            }
        }
    }
    
    
}

 

 
