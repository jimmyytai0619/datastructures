package entity;

import java.util.ArrayList;
import java.util.List;

public class Patient. {
    private String name;
    private int id;
    private String treatmentType;
    private String diagnosis;
    private List<String> treatmentHistory;

    public Patient(String name, int id, String treatmentType) {
        this.name = name;
        this.id = id;
        this.treatmentType = treatmentType;
        this.treatmentHistory = new ArrayList<>();
    }

    // getters & setters
    public String getName() { return name; }
    public int getId() { return id; }
    public String getTreatmentType() { return treatmentType; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public void addTreatmentHistory(String record) {
        treatmentHistory.add(record);
    }

    public List<String> getTreatmentHistory() {
        return treatmentHistory;
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ", Type: " + treatmentType + ")";
    }
}
