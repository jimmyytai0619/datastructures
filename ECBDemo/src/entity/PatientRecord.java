/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import entity.Patient;
import java.io.Serializable;

public class PatientRecord extends Patient implements Serializable {
    private String medicalHistory;
    private String allergies;
    private String contactNumber;
    
    public PatientRecord(String name, int id, String treatmentType, 
                        String medicalHistory, String allergies, String contactNumber) {
        super(name, id, treatmentType);
        this.medicalHistory = medicalHistory;
        this.allergies = allergies;
        this.contactNumber = contactNumber;
    }

    // Getters and setters
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    @Override
    public String toString() {
        return super.toString() + 
               String.format("\nMedical History: %s\nAllergies: %s\nContact: %s", 
               medicalHistory, allergies, contactNumber);
    }
}
