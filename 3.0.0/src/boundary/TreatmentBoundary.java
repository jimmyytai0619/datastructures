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
        int choice = -1;
        do {
            System.out.println("\n=== Treatment Management ===");
            System.out.println("1. Add Treatment to Patient");
            System.out.println("2. View Treatment History");
            System.out.println("3. Treatment Summary Reports");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

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
        choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> treatmentControl.generateTreatmentCountReport(patientControl.getAllPatients());
            case 2 -> treatmentControl.generateTreatmentCoverageReport(patientControl.getAllPatients());
            case 0 -> System.out.println("Back to Treatment Menu...");
            default -> System.out.println("Invalid option. Try again!");
        }
    } while (choice != 0);
}



    private void addTreatment() {
        System.out.print("Enter patient ID: ");
        String id = sc.nextLine();
        Patient patient = patientControl.searchPatientById(id);
        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Enter diagnosis: ");
        String diagnosis = sc.nextLine();
        System.out.print("Enter treatment description: ");
        String desc = sc.nextLine();

        Treatment treatment = new Treatment(diagnosis, desc);
        treatmentControl.addTreatmentToPatient(patient, treatment);
        System.out.println("Treatment added successfully.");
    }

    private void viewTreatmentHistory() {
        System.out.print("Enter patient ID: ");
        String id = sc.nextLine();
        Patient patient = patientControl.searchPatientById(id);
        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        ListInterface<Treatment> treatments = treatmentControl.getTreatmentHistory(patient);
        if (treatments.isEmpty()) {
            System.out.println("No treatments found for this patient.");
            return;
        }

        System.out.println("\nTreatment History for " + patient.getName() + ":");
        for (int i = 1; i <= treatments.getNumberOfEntries(); i++) {
            Treatment t = treatments.getEntry(i);
            System.out.println(i + ". " + t);
        }
    }
}
