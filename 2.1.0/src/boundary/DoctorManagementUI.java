package boundary;

import control.ConsultationModuleController;
import adt.MyIterator;
import entity.Doctor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Scanner;

public class DoctorManagementUI {
    private final ConsultationModuleController controller;
    private final Scanner scanner = new Scanner(System.in);

    public DoctorManagementUI(ConsultationModuleController controller) {
        this.controller = controller;
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n=== DOCTOR MANAGEMENT ===");
            System.out.println("1. Add Doctor");
            System.out.println("2. View Doctors");
            System.out.println("3. Add Duty Slot");
            System.out.println("4. View Doctor Schedule");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");
            choice = readInt();

            switch (choice) {
                case 1 -> addDoctor();
                case 2 -> viewDoctors();
                case 3 -> addDutySlot();
                case 4 -> viewSchedule();
                case 5 -> System.out.println("Returning...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 5);
    }

    private void addDoctor() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Specialization: ");
        String spec = scanner.nextLine().trim();

        Doctor d = controller.registerDoctor(id, name, spec);
        System.out.println(d == null ? "Duplicate doctor ID." : "Doctor added: " + d.getDoctorId());
    }

    private void viewDoctors() {
        System.out.println("\n=== Doctors ===");
        MyIterator<Doctor> it = controller.getDoctors().iterator();
        if (!it.hasNext()) {
            System.out.println("(none)");
            return;
        }
        while (it.hasNext()) {
            Doctor d = it.next();
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

    private void viewSchedule() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine().trim();
        System.out.println(controller.showSchedule(id));
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }
}
