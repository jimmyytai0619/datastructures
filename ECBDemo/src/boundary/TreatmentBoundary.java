package boundary;

import entity.Patient;
import entity.Treatment;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TreatmentBoundary {
    private Scanner scanner = new Scanner(System.in);

    // store patients here
    private List<Patient> patients = new ArrayList<>();

    public void run() {
        int choice;
        do {
            System.out.println("\n--- Treatment Management System ---");
            System.out.println("1. Register Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Add Diagnosis/Treatment");
            System.out.println("4. View Treatment History");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> registerPatient();
                case 2 -> viewPatients();
                case 3 -> addTreatment();
                case 4 -> viewTreatmentHistory();
                case 5 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 5);
    }

    private void registerPatient() {
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Patient Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine();

        Patient patient = new Patient(name, age, id);
        patients.add(patient);

        System.out.println("Patient registered successfully.");
    }

    private void viewPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
            return;
        }
        System.out.println("\n--- Registered Patients ---");
        for (Patient p : patients) {
            System.out.println(p);
        }
    }

    private void addTreatment() {
        if (patients.isEmpty()) {
            System.out.println("No patients found. Please register first.");
            return;
        }

        System.out.print("Enter Patient ID to add treatment: ");
        String id = scanner.nextLine();
        Patient patient = findPatientById(id);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.print("Enter Diagnosis: ");
        String diagnosis = scanner.nextLine();
        System.out.print("Enter Treatment Description: ");
        String description = scanner.nextLine();

        Treatment treatment = new Treatment(diagnosis, description);
        patient.addTreatment(treatment);

        System.out.println("Treatment added for " + patient.getName());
    }

    private void viewTreatmentHistory() {
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }

        System.out.print("Enter Patient ID to view history: ");
        String id = scanner.nextLine();
        Patient patient = findPatientById(id);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("\n--- Treatment History for " + patient.getName() + " ---");

        if (patient.getTreatments().getNumberOfEntries() == 0) {
            System.out.println("No treatments recorded.");
            return;
        }

        for (int i = 1; i <= patient.getTreatments().getNumberOfEntries(); i++) {
            Treatment t = patient.getTreatments().getEntry(i);
            System.out.println(t);
        }
    }


    private Patient findPatientById(String id) {
        for (Patient p : patients) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new TreatmentBoundary().run();
    }
}
