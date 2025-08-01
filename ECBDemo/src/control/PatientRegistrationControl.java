/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import adt.ListInterface;
import adt.ArrayList;
import dao.PatientDAO;
import entity.PatientRecord;

public class PatientRegistrationControl {
    private ListInterface<PatientRecord> patientRecords;
    private final PatientDAO patientDAO;
    
    public PatientRegistrationControl() {
        this.patientDAO = new PatientDAO();
        this.patientRecords = patientDAO.retrieveFromFile();
        
        if (this.patientRecords == null) {
            this.patientRecords = new ArrayList<>();
        }
    }
    
    public void registerPatient(PatientRecord patient) {
        patientRecords.add(patient);
        patientDAO.saveToFile(patientRecords);
    }
    
    public void displayAllPatients() {
        if (patientRecords.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }
        
        System.out.println("Total patients: " + patientRecords.getNumberOfEntries());
        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            System.out.println("\nPatient #" + i);
            System.out.println(patientRecords.getEntry(i));
        }
    }
    
    public void searchPatient(int id) {
        boolean found = false;
        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            PatientRecord patient = patientRecords.getEntry(i);
            if (patient.getId() == id) {
                System.out.println("\nPatient found:");
                System.out.println(patient);
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("Patient with ID " + id + " not found.");
        }
    }
}

