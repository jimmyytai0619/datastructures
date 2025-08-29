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
            System.out.println("\n=== Consultation Management ===");
            System.out.println("1. Create Appointment");
            System.out.println("2. Conduct Consultation");
            System.out.println("3. Schedule Follow-up Appointment");
            System.out.println("4. List Appointments");
            System.out.println("5. List Consultations");
            System.out.println("6. Report: Consultations per Doctor");
            System.out.println("7. Report: Upcoming Appointments per Patient");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createAppointment();
                case "2" -> conductConsultation();
                case "3" -> scheduleFollowUp();
                case "4" -> listAppointments();
                case "5" -> listConsultations();
                case "6" -> System.out.println(reportController.generateConsultationsPerDoctorReport());
                case "7" -> System.out.println(reportController.generateUpcomingAppointmentsPerPatientReport());
                case "0" -> exit = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void createAppointment() {
        System.out.print("Patient ID: ");
        String pid = scanner.nextLine().trim();
        System.out.print("Doctor ID: ");
        String did = scanner.nextLine().trim();
        LocalDate date = readDate("Date (YYYY-MM-DD): ");
        LocalTime time = readTime("Time (HH:MM): ");
        System.out.print("Purpose: ");
        String purpose = scanner.nextLine().trim();
        Appointment a = controller.createAppointmentAuto(pid, did, LocalDateTime.of(date, time), purpose);
        System.out.println(a == null ? "Invalid patient/doctor ID." : "Created: " + a.getAppointmentId());
    }

    private void conductConsultation() {
        String aid = pickPendingAppointmentId();
        if (aid == null) return;
        System.out.print("Consultation ID: ");
        String cid = scanner.nextLine().trim();
        System.out.print("Symptoms: ");
        String symptoms = scanner.nextLine().trim();
        System.out.print("Diagnosis: ");
        String diagnosis = scanner.nextLine().trim();
        System.out.print("Notes: ");
        String notes = scanner.nextLine().trim();
        Consultation c = controller.conductConsultation(cid, aid, symptoms, diagnosis, notes);
        System.out.println(c == null ? "Invalid appointment or duplicate consultation." : "Consultation recorded: " + cid);
    }

    private void scheduleFollowUp() {
        System.out.print("Existing Consultation ID: ");
        String cid = scanner.nextLine().trim();
        LocalDate date = readDate("Follow-up Date (YYYY-MM-DD): ");
        LocalTime time = readTime("Time (HH:MM): ");
        System.out.print("Purpose: ");
        String purpose = scanner.nextLine().trim();
        Appointment a = controller.scheduleFollowUpAuto(cid, LocalDateTime.of(date, time), purpose);
        System.out.println(a == null ? "Invalid consultation ID." : "Follow-up scheduled: " + a.getAppointmentId());
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

    private String pickPendingAppointmentId() {
        var list = controller.getAppointments();
        int n = list.getNumberOfEntries();
        int count = 0;
        for (int i = 1; i <= n; i++) {
            var a = list.getEntry(i);
            boolean attended = false;
            try { attended = a.isAttended(); } catch (Exception ignore) {}
            if (!attended) count++;
        }
        if (count == 0) {
            System.out.println("(no pending appointments)");
            return null;
        }
        int[] map = new int[count + 1];
        int k = 0;
        System.out.println("\n=== Pending Appointments ===");
        for (int i = 1; i <= n; i++) {
            var a = list.getEntry(i);
            boolean attended = false;
            try { attended = a.isAttended(); } catch (Exception ignore) {}
            if (!attended) {
                k++;
                map[k] = i;
                System.out.println(k + ". " + a.getAppointmentId() + " | "
                        + a.getPatient().getName() + " with "
                        + a.getDoctor().getName() + " @ " + a.getDateTime());
            }
        }
        int sel = readInt("Choose (1-" + count + "): ");
        if (sel < 1 || sel > count) {
            System.out.println("Invalid selection.");
            return null;
        }
        return list.getEntry(map[sel]).getAppointmentId();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine().trim());
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
