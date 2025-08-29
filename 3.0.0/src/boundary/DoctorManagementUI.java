package boundary;

import control.ConsultationModuleController;
import control.ReportController;
import entity.Doctor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Scanner;

public class DoctorManagementUI {
    private final ConsultationModuleController controller;
    private final Scanner scanner = new Scanner(System.in);
    private final ReportController reportGen;

    public DoctorManagementUI(ConsultationModuleController controller) {
        this.controller = controller;
        this.reportGen = new ReportController(controller);
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n=== DOCTOR MANAGEMENT ===");
            System.out.println("1. Add Doctor");
            System.out.println("2. View Doctors");
            System.out.println("3. Add Duty Slot");
            System.out.println("4. Update Doctor");
            System.out.println("5. Remove Doctor");
            System.out.println("6. View Doctor Schedule");
            System.out.println("7. View Doctors Report");
            System.out.println("8. Back");
            System.out.print("Enter choice: ");
            choice = readInt();

            switch (choice) {
                case 1 -> addDoctor();
                case 2 -> viewDoctors();
                case 3 -> addDutySlot();
                case 4 -> updateDoctor();
                case 5 -> removeDoctor();
                case 6 -> viewSchedule();
                case 7 -> reports();
                case 8 -> System.out.println("Returning...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 8);
    }

    private void addDoctor() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Specialization: ");
        String spec = scanner.nextLine().trim();
        Doctor d = controller.registerDoctor(id, name, spec);
        System.out.println(d == null ? "Duplicate doctor ID." : "Doctor added: " + id);
    }

    private void viewDoctors() {
        var list = controller.getDoctors();
        if (list.getNumberOfEntries() == 0) {
            System.out.println("(no doctors)");
            return;
        }
        System.out.println("\n=== Doctors ===");
        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            Doctor d = list.getEntry(i);
            System.out.println(d.getDoctorId() + " - " + d.getName() + " (" + d.getSpecialization() + ")");
        }
    }

    private void addDutySlot() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("DayOfWeek (e.g., MONDAY): ");
        DayOfWeek day;
        try {
            day = DayOfWeek.valueOf(scanner.nextLine().trim().toUpperCase());
        } catch (Exception e) {
            System.out.println("Invalid day.");
            return;
        }
        System.out.print("Start (HH:MM): ");
        LocalTime start;
        try {
            String s = scanner.nextLine().trim();
            start = LocalTime.parse(s.length() == 5 ? s + ":00" : s);
        } catch (Exception e) {
            System.out.println("Invalid time.");
            return;
        }
        System.out.print("End (HH:MM): ");
        LocalTime end;
        try {
            String s = scanner.nextLine().trim();
            end = LocalTime.parse(s.length() == 5 ? s + ":00" : s);
        } catch (Exception e) {
            System.out.println("Invalid time.");
            return;
        }
        boolean ok = controller.addDutySlot(id, day, start, end);
        System.out.println(ok ? "Duty slot added." : "Failed (check doctor ID or time range).");
    }

    private void updateDoctor() {
        System.out.print("Doctor ID to update: ");
        String id = scanner.nextLine().trim();
        Doctor d = controller.findDoctorById(id);
        if (d == null) {
            System.out.println("Doctor not found.");
            return;
        }
        System.out.print("New name (leave blank to keep \"" + d.getName() + "\"): ");
        String name = scanner.nextLine().trim();
        System.out.print("New specialization (leave blank to keep \"" + d.getSpecialization() + "\"): ");
        String spec = scanner.nextLine().trim();
        boolean updated = controller.updateDoctor(id, name, spec);
        System.out.println(updated ? "Doctor updated." : "Update failed.");
    }

    private void removeDoctor() {
        System.out.print("Doctor ID to remove: ");
        String id = scanner.nextLine().trim();
        boolean ok = controller.removeDoctor(id);
        System.out.println(ok ? "Doctor removed." : "Doctor not found.");
    }

    private void viewSchedule() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine().trim();
        System.out.println(controller.showSchedule(id));
    }

    private void reports() {
        System.out.println("\n=== Doctor Reports ===");
        System.out.println("1. Monthly Duty Roster");
        System.out.println("2. Availability Summary (next N days)");
        System.out.println("3. Utilization Report (last N days)");
        System.out.print("Choose: ");
        int c = readInt();
        switch (c) {
            case 1 -> {
                System.out.print("Month (1-12): ");
                int m = readInt();
                System.out.print("Year: ");
                int y = readInt();
                System.out.println(reportGen.monthlyDutyRoster(m, y));
            }
            case 2 -> {
                System.out.print("Look ahead days: ");
                int n = readInt();
                System.out.println(reportGen.availabilitySummary(n));
            }
            case 3 -> {
                System.out.print("Look back days: ");
                int n = readInt();
                System.out.println(reportGen.utilizationReport(n));
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }
}
