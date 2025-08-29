package boundary;

import control.PatientDirectory;           
import control.PatientRegistrationControl;    
import control.PatientRecordsControl;       
import entity.Patient;
import entity.PatientRecord;

import java.util.Scanner;


public class PatientRegistrationUI {
    private final PatientRegistrationControl control;
    private final PatientRecordsControl recordsControl;
    private final PatientDirectory sharedDir; 
    private final Scanner scanner;


    public PatientRegistrationUI(PatientDirectory dir) {
        this.sharedDir = dir;
        this.control = new PatientRegistrationControl(); 
        this.recordsControl = new PatientRecordsControl();
        this.scanner = new Scanner(System.in);
    }


    public PatientRegistrationUI() {
        this.sharedDir = null;
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

            choice = readInt();
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
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine().trim();

        int age = readInt("Enter Patient Age: ");

        System.out.print("Enter Treatment Type: ");
        String treatmentType = scanner.nextLine().trim();

        System.out.print("Enter Medical History: ");
        String medicalHistory = scanner.nextLine().trim();

        System.out.print("Enter Allergies: ");
        String allergies = scanner.nextLine().trim();

        System.out.print("Enter Contact Number: ");
        String contactNumber = scanner.nextLine().trim();

        
        PatientRecord newPatient = new PatientRecord(
                name, id, age, treatmentType, medicalHistory, allergies, contactNumber
        );
        control.registerPatient(newPatient);
        System.out.println("Patient registered successfully (legacy store)!");

   
        if (sharedDir != null) {
       
            Patient p = new Patient(id, name, age, contactNumber, "");
            boolean ok = sharedDir.add(p); 
            if (ok) {
                System.out.println("Synced to shared directory ✅");
            } else {
                System.out.println("Warning: duplicate ID in shared directory (sync skipped).");
            }
        }
    }

    private void searchPatient() {
        System.out.print("Enter Patient ID to search: ");
        String id = scanner.nextLine().trim();
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

        if (sharedDir != null) {
            System.out.println("-- Shared Directory Snapshot --");
            var list = sharedDir.all();
            System.out.println("Total (shared): " + list.getNumberOfEntries());
            if (list.getNumberOfEntries() > 0) {
                for (int i = 1; i <= list.getNumberOfEntries(); i++) {
                    System.out.println(" - " + list.getEntry(i));
                }
            }
        }
    }

    // ------- helpers -------
    private int readInt(String prompt) {
        while (true) {
            try {
                if (prompt != null) System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private int readInt() {
        return readInt(null);
    }
}
