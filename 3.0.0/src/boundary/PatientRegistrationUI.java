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
            System.out.println("4. Update Patient");
            System.out.println("5. Delete Patient");
            System.out.println("6. Show Reports");
            System.out.println("7. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = readInt();
            switch (choice) {
                case 1 -> registerPatient();
                case 2 -> control.displayAllPatients();
                case 3 -> searchPatient();
                case 4 -> updatePatient();
                case 5 -> deletePatient();
                case 6 -> showReports();
                case 7 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 7);
    }

    // -------- Register Patient --------
    private void registerPatient() {
        String id = safeInput("Enter Patient ID: ");
        String name = safeInput("Enter Patient Name: ");
        int age = readInt("Enter Patient Age: ");
        String medicalHistory = safeInput("Enter Medical History: ");
        String allergies = safeInput("Enter Allergies: ");

        String contactNumber;
        while (true) {
            contactNumber = safeInput("Enter Contact Number: ");
            if (!contactNumber.matches("\\d+")) {
                System.out.println("Contact number must be digits only.");
            } else {
                break;
            }
        }

        PatientRecord newPatient = new PatientRecord(
                id, name, age, medicalHistory, allergies, contactNumber
        );
        control.registerPatient(newPatient);
        System.out.println("Patient registered successfully!");

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

    // -------- Search Patient --------
    private void searchPatient() {
        String id = safeInput("Enter Patient ID to search: ");
        PatientRecord patient = control.searchPatientById(id);

        if (patient != null) {
            System.out.println("Patient Found: " + patient);
        } else {
            System.out.println("No patient found with ID: " + id);
        }
    }

    // -------- Update Patient --------
    private void updatePatient() {
        String id = safeInput("Enter Patient ID to update: ");
        PatientRecord existing = control.searchPatientById(id);

        if (existing == null) {
            System.out.println("No patient found with ID: " + id);
            return;
        }

        System.out.println("Leave field blank to keep old value.");

        String name = safeInput("Enter New Name (" + existing.getName() + "): ");
        if (name.isEmpty()) name = existing.getName();

        int age = existing.getAge();
        while (true) {
            String ageInput = safeInput("Enter New Age (" + existing.getAge() + "): ");
            if (ageInput.isEmpty()) break;
            try {
                age = Integer.parseInt(ageInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, please enter a valid integer.");
            }
        }

        String medicalHistory = safeInput("Enter New Medical History (" + existing.getMedicalHistory() + "): ");
        if (medicalHistory.isEmpty()) medicalHistory = existing.getMedicalHistory();

        String allergies = safeInput("Enter New Allergies (" + existing.getAllergies() + "): ");
        if (allergies.isEmpty()) allergies = existing.getAllergies();

        String contactNumber;
        while (true) {
            contactNumber = safeInput("Enter New Contact Number (" + existing.getContactNumber() + "): ");
            if (contactNumber.isEmpty()) {
                contactNumber = existing.getContactNumber();
                break;
            } else if (!contactNumber.matches("\\d+")) {
                System.out.println("Contact number must be digits only.");
            } else {
                break;
            }
        }

        PatientRecord updated = new PatientRecord(
                id, name, age, medicalHistory, allergies, contactNumber
        );

        if (control.updatePatient(updated)) {
            System.out.println("Patient updated successfully!");
        } else {
            System.out.println("Update failed.");
        }
    }

    // -------- Delete Patient --------
    private void deletePatient() {
        String id = safeInput("Enter Patient ID to delete: ");
        boolean ok = control.deletePatientById(id);

        if (ok) {
            System.out.println("Patient deleted successfully!");
        } else {
            System.out.println("No patient found with ID: " + id);
        }
    }

    // -------- Show Reports --------
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

    // -------- Helpers --------
    private String safeInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                if (prompt != null) System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, please enter a valid integer.");
            }
        }
    }

    private int readInt() {
        return readInt(null);
    }
}
