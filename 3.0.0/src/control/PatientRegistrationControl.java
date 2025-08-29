package boundary;

import control.PatientRegistrationControl;
import control.PatientRecordsControl;
import entity.PatientRecord;
import java.util.Scanner;

public class PatientRegistrationUI {
    private final PatientRegistrationControl control;
    private final PatientRecordsControl recordsControl;
    private final Scanner scanner;

    public PatientRegistrationUI() {
        this.control = new PatientRegistrationControl();
        this.recordsControl = new PatientRecordsControl();
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("\n=== PATIENT MANAGEMENT ===");
            System.out.println("1. Register New Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Search Patient by ID");
            System.out.println("4. Show Reports");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> registerPatient();
                case 2 -> control.displayAllPatients();
                case 3 -> searchPatient();
                case 4 -> showReports();
                case 5 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);
    }

    private void registerPatient() {
    System.out.print("Enter Patient ID: ");
    String id = scanner.nextLine();    
        
    System.out.print("Enter Patient Name: ");
    String name = scanner.nextLine();

    System.out.print("Enter Patient Age: ");
    int age = Integer.parseInt(scanner.nextLine());

    System.out.print("Enter Treatment Type: ");
    String treatmentType = scanner.nextLine();

    System.out.print("Enter Medical History: ");
    String medicalHistory = scanner.nextLine();

    System.out.print("Enter Allergies: ");
    String allergies = scanner.nextLine();

    System.out.print("Enter Contact Number: ");
    String contactNumber = scanner.nextLine();

    PatientRecord newPatient = new PatientRecord(
            id,
            name,
            age,
            treatmentType,
            medicalHistory,
            allergies,
            contactNumber
    );

    control.registerPatient(newPatient);
    System.out.println("Patient registered successfully!");
}


    private void searchPatient() {
        System.out.print("Enter Patient ID to search: ");
        String id = scanner.nextLine();
        PatientRecord patient = control.searchPatientById(id);

        if (patient != null) {
            System.out.println("Patient Found: " + patient);
        } else {
            System.out.println("No patient found with ID: " + id);
        }
    }

    private void showReports() {
        System.out.println("\n=== SUMMARY REPORTS ===");
        recordsControl.generateTotalPatientsReport();
        recordsControl.generateAgeGroupDistributionReport();
    }
}
