package entity;

import java.io.Serializable;

public class PatientRecord extends Patient implements Serializable {
    private String medicalHistory;
    private String allergies;

    public PatientRecord(String id, String name, int age, String medicalHistory, String allergies, String contactNumber) {
        super(id, name, age, contactNumber, "");  // ✅ contactNumber 传给父类
        this.medicalHistory = medicalHistory;
        this.allergies = allergies;
    }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    @Override
    public String toString() {
        return super.toString() +
                String.format("\nMedical History: %s\nAllergies: %s",
                        medicalHistory, allergies);
    }
}

