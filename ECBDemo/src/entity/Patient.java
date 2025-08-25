package entity;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String name;
    private int id;
    private String treatmentType;
    private List<String> treatmentHistory;  // keep track of all treatments

    public Patient(String name, int id, String treatmentType) {
        this.name = name;
        this.id = id;
        this.treatmentType = treatmentType;
        this.treatmentHistory = new ArrayList<>();
        this.treatmentHistory.add(treatmentType); // first treatment
    }

    public String getName() { return name; }
    public int getId() { return id; }
    public String getTreatmentType() { return treatmentType; }

    public void addTreatment(String treatment) {
        treatmentHistory.add(treatment);
        this.treatmentType = treatment; // latest treatment
    }

    public List<String> getTreatmentHistory() {
        return treatmentHistory;
    }

    @Override
    public String toString() {
        return String.format("Patient[ID:%d, Name:%s, Current Treatment:%s]", 
                              id, name, treatmentType);
    }
}
