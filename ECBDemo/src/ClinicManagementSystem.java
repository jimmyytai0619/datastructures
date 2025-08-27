import boundary.*;
import control.*;
import java.util.Scanner;

/**
 * Main Application class for Clinic Management System
 * 
 * @author hugol
 */
public class ClinicManagementSystem {
    private PatientRegistrationUI patientUI;
    private PharmacyUI pharmacyUI;
    private TreatmentBoundary treatmentUI;
    private Scanner scanner;
    
    public ClinicManagementSystem() {
        patientUI = new PatientRegistrationUI();
        pharmacyUI = new PharmacyUI();
        treatmentUI = new TreatmentBoundary();
        scanner = new Scanner(System.in);
    }
    
public void start() {
    System.out.println("========================================");
    System.out.println("        CLINIC MANAGEMENT SYSTEM        ");
    System.out.println("        TAR UMT On-Campus Clinic        ");
    System.out.println("========================================");
    
    while (true) {
        showMainMenu();
        int choice = getChoice();
        
        switch (choice) {
            case 1 -> runPatientManagement();
            case 2 -> runPharmacyManagement();
            case 3 -> runTreatmentManagement();
            case 4 -> showSystemReport();
            case 0 -> {
                System.out.println("\nThank you for using Clinic Management System!");
                System.out.println("Goodbye!");
                return;
            }
            default -> System.out.println("\nInvalid choice. Please enter 0–4.");
        }
    }
}

private void showMainMenu() {
    System.out.println("\n========================================");
    System.out.println("                MAIN MENU               ");
    System.out.println("========================================");
    
    System.out.println("[1] Patient Management");
    System.out.println("[2] Pharmacy Management");
    System.out.println("[3] Treatment Management");
    System.out.println("[4] System Reports");
    
    System.out.println("----------------------------------------");
    System.out.println("[0] Exit System");
    System.out.println("========================================");
    
    System.out.print("Please choose a module: ");
}

    
    private int getChoice() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        } finally {
            scanner.nextLine();
        }
    }
    
    private void runPatientManagement() {
        patientUI.showMenu();
    }
    
    private void runPharmacyManagement() {
        pharmacyUI.runPharmacyManagement();
    }
    
    private void runTreatmentManagement() {
        treatmentUI.run();
    }
    
    private void showSystemReport() {
        System.out.println("\n=== SYSTEM OVERVIEW ===");
        System.out.println("System report feature coming soon...");
    }
    
    public static void main(String[] args) {
        ClinicManagementSystem app = new ClinicManagementSystem();
        app.start();
    }
}