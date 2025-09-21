package boundary;

import control.TreatmentControl;
import control.PatientRegistrationControl;
import entity.Patient;
import entity.Treatment;
import adt.ListInterface;
import java.util.Scanner;

public class TreatmentBoundary {
    private TreatmentControl treatmentControl;
    private PatientRegistrationControl patientControl;
    private Scanner sc;

    public TreatmentBoundary(TreatmentControl treatmentControl, PatientRegistrationControl patientControl) {
        this.treatmentControl = treatmentControl;
        this.patientControl = patientControl;
        this.sc = new Scanner(System.in);
    }

    public TreatmentBoundary() {
        this(new TreatmentControl(), new PatientRegistrationControl());
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n=== Treatment Management ===");
            System.out.println("1. Add Treatment to Patient");
            System.out.println("2. View Treatment History");
            System.out.println("3. Treatment Summary Reports");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = safeReadInt();

            switch (choice) {
                case 1 -> addTreatment();
                case 2 -> viewTreatmentHistory();
                case 3 -> showSummaryReports();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Try again!");
            }
        } while (choice != 0);
    }

    private void showSummaryReports() {
        int choice;
        do {
            System.out.println("\n=== Treatment Summary Reports ===");
            System.out.println("1. Patient Treatment Count Report");
            System.out.println("2. Treatment Coverage Report");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            choice = safeReadInt();

            switch (choice) {
                case 1 -> System.out.println(treatmentControl.generateTreatmentCountReport(patientControl.getAllPatients()));
                case 2 -> System.out.println(treatmentControl.generateTreatmentCoverageReport(patientControl.getAllPatients()));
                case 0 -> System.out.println("Back to Treatment Menu...");
                default -> System.out.println("Invalid option. Try again!");
            }
        } while (choice != 0);
    }

    private void addTreatment() {
        Patient patient = promptPatientById();
        if (patient == null) return;

        System.out.print("Enter diagnosis: ");
        String diagnosis = sc.nextLine().trim();
        System.out.print("Enter treatment description: ");
        String desc = sc.nextLine().trim();

        Treatment treatment = new Treatment(diagnosis, desc);
        treatmentControl.addTreatmentToPatient(patient, treatment);
        System.out.println(" Treatment added successfully.");
    }

    private void viewTreatmentHistory() {
        Patient patient = promptPatientById();
        if (patient == null) return;

        ListInterface<Treatment> treatments = treatmentControl.getTreatmentHistory(patient);
        if (treatments.isEmpty()) {
            System.out.println("No treatments found for this patient.");
            return;
        }

        System.out.println("\nTreatment History for " + patient.getName() + ":");
        for (int i = 1; i <= treatments.getNumberOfEntries(); i++) {
            System.out.println(i + ". " + treatments.getEntry(i));
        }
    }

    // Helper method for patient lookup
    private Patient promptPatientById() {
        System.out.print("Enter patient ID: ");
        String id = sc.nextLine().trim();
        Patient patient = patientControl.searchPatientById(id);
        if (patient == null) {
            System.out.println(" Patient not found!");
        }
        return patient;
    }

    // Safe integer input
    private int safeReadInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine(); // clear newline
        return val;
    }
}

