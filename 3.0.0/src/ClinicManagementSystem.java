import boundary.*;
import control.*;

import java.util.Scanner;



public class ClinicManagementSystem {
 
    private final PatientDirectory patientDir;
    private final DoctorDirectory  doctorDir;

  
    private final ConsultationModuleController consultationController;
    private final ReportController reportController;

    private final PatientRegistrationUI patientUI;
    private final DoctorManagementUI    doctorUI;
    private final ConsultationModuleUI  consultationUI;
    private final PharmacyUI pharmacyUI;
    private final TreatmentBoundary treatmentUI;

    private final Scanner scanner = new Scanner(System.in);

    public ClinicManagementSystem() {
    
        patientDir = new PatientDirectory();
        doctorDir  = new DoctorDirectory();

        consultationController = new ConsultationModuleController(patientDir, doctorDir);
        reportController = new ReportController(consultationController);

        patientUI = new PatientRegistrationUI(patientDir);

        doctorUI = new DoctorManagementUI(consultationController);

        consultationUI = new ConsultationModuleUI(consultationController);

        pharmacyUI = new PharmacyUI();
        treatmentUI = new TreatmentBoundary();
    }

    public void start() {
        while (true) {
        System.out.println("========================================");
        System.out.println("        CLINIC MANAGEMENT SYSTEM        ");
        System.out.println("========================================");

            showMainMenu();
            int c = readInt();
            switch (c) {
                case 1 -> patientUI.showMenu();                 
                case 2 -> pharmacyUI.runPharmacyManagement();   
                case 3 -> treatmentUI.run();                   
                case 4 -> consultationUI.run();                
                case 5 -> doctorUI.run();                       
                case 6 -> showSystemReport();                  
                case 0 -> { System.out.println("Bye!"); return; }
                default -> System.out.println("Invalid choice 0–6.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n[1] Patient Management");
        System.out.println("[2] Pharmacy Management");
        System.out.println("[3] Treatment Management");
        System.out.println("[4] Consultation Management");
        System.out.println("[5] Doctor Management");
        System.out.println("[6] System Reports");
        System.out.println("[0] Exit");
        System.out.print("Choose: ");
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }

    private void showSystemReport() {
        System.out.println(reportController.generateConsultationsPerDoctorReport());
        System.out.println(reportController.generateUpcomingAppointmentsPerPatientReport());
    }

    public static void main(String[] args) {
        new ClinicManagementSystem().start();
    }
}
