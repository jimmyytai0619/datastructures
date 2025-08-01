/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import control.PatientRegistrationControl;
import entity.PatientRecord;
import java.util.Scanner;

public class PatientRegistrationUI {
    private final PatientRegistrationControl control;
    private final Scanner scanner;
    
    public PatientRegistrationUI() {
        this.control = new PatientRegistrationControl();
        this.scanner = new Scanner(System.in);
    }
    
    public void showMenu() {
        int choice;
        do {
            System.out.println("\n=== PATIENT MANAGEMENT ===");
            System.out.println("1. Register New Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Search Patient");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch(choice) {
                case 1: registerPatient(); break;
                case 2: viewAllPatients(); break;
                case 3: searchPatient(); break;
                case 4: System.out.println("Returning to main menu..."); break;
                default: System.out.println("Invalid choice! Please try again.");
            }
        } while(choice != 4);
    }
    
    private void registerPatient() {
        System.out.println("\n--- NEW PATIENT REGISTRATION ---");
        
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter patient ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter treatment type: ");
        String treatmentType = scanner.nextLine();
        
        System.out.print("Enter medical history: ");
        String medicalHistory = scanner.nextLine();
        
        System.out.print("Enter allergies: ");
        String allergies = scanner.nextLine();
        
        System.out.print("Enter contact number: ");
        String contactNumber = scanner.nextLine();
        
        PatientRecord newPatient = new PatientRecord(name, id, treatmentType, 
                                                   medicalHistory, allergies, contactNumber);
        control.registerPatient(newPatient);
        System.out.println("\nPatient registered successfully!");
    }
    
    private void viewAllPatients() {
        System.out.println("\n--- ALL REGISTERED PATIENTS ---");
        control.displayAllPatients();
    }
    
    private void searchPatient() {
        System.out.println("\n--- SEARCH PATIENT ---");
        System.out.print("Enter patient ID to search: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        control.searchPatient(id);
    }
}

