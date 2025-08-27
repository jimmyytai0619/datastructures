package entity;

public class Treatment {
    private String diagnosis;
    private String description;

    public Treatment(String diagnosis, String description) {
        this.diagnosis = diagnosis;
        this.description = description;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Diagnosis: " + diagnosis + ", Treatment: " + description;
    }
}
