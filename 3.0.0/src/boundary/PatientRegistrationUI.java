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
        System.out.println("4. Search Patient by Name");
        System.out.println("5. Update Patient");
        System.out.println("6. Delete Patient");
        System.out.println("7. Clear All Patients");
        System.out.println("8. Show Reports");
        System.out.println("9. Back to Main Menu");
        System.out.print("Enter your choice: ");

        choice = readInt();
        switch (choice) {
            case 1 -> registerPatient();
            case 2 -> control.displayAllPatients();
            case 3 -> searchPatient();
            case 4 -> searchPatientByName();
            case 5 -> updatePatient();
            case 6 -> deletePatient();
            case 7 -> clearAllPatients();
            case 8 -> showReports();
            case 9 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid choice! Please try again.");
        }
    } while (choice != 9);
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

        boolean ok = control.registerPatient(newPatient);
        if (ok) {
            System.out.println("Patient registered successfully!");
        } else {
            System.out.println("Registration failed (duplicate ID).");
            return;
        }

        if (sharedDir != null) {
            Patient p = new Patient(id, name, age, contactNumber, "");
            boolean synced = sharedDir.add(p);
            if (synced) {
                System.out.println("Synced to shared directory");
            } else {
                System.out.println("Warning: duplicate ID in shared directory (sync skipped).");
            }
        }
    }

    // -------- Search Patient by ID --------
    private void searchPatient() {
        String id = safeInput("Enter Patient ID to search: ");
        PatientRecord patient = control.binarySearchPatientById(id);

        if (patient != null) {
            System.out.println("Patient Found: " + patient);
        } else {
            System.out.println("No patient found with ID: " + id);
        }
    }

    // -------- Search Patient by Name (contains) --------
    private void searchPatientByName() {
        String keyword = safeInput("Enter part of the name to search: ");
        boolean found = false;

        for (int i = 1; i <= control.getAllPatients().getNumberOfEntries(); i++) {
            PatientRecord p = control.getAllPatients().getEntry(i);
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println("Match: " + p);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No patient found with name containing: " + keyword);
        }
    }

    // -------- Update Patient --------
    private void updatePatient() {
        String id = safeInput("Enter Patient ID to update: ");
        PatientRecord existing = control.binarySearchPatientById(id);

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
            contactNumber = safeInput("Enter New Contact Number(" + existing.getContactNumber() + "):");
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
            System.out.println("Update failed (possible ID conflict).");
        }
    }

    // -------- Delete Patient --------
    private void deletePatient() {
        String id = safeInput("Enter Patient ID to delete: ");
        PatientRecord existing = control.binarySearchPatientById(id);

        if (existing == null) {
            System.out.println("No patient found with ID: " + id);
            return;
        }

        boolean ok = control.deletePatientById(id);
        if (ok) {
            System.out.println("Patient deleted successfully!");
        } else {
            System.out.println("Delete failed.");
        }
    }

    // -------- Clear All Patients --------
    private void clearAllPatients() {
        System.out.print("Are you sure you want to clear ALL patients? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("yes")) {
            control.clearAllPatients();
        } else {
            System.out.println("Clear operation cancelled.");
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
    while (true) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (!input.isEmpty()) {
            return input;
        }
        System.out.println("Input cannot be empty, please try again.");
    }
}


    private int readInt(String prompt) {
    while (true) {
        try {
            if (prompt != null) System.out.print(prompt);
            String input = scanner.nextLine().trim(); // 
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
