package control;

import adt.ListInterface;
import dao.PatientDAO;
import entity.PatientRecord;
import entity.Treatment;

public class PatientRecordsControl {
    private final ListInterface<PatientRecord> patientRecords;

    public PatientRecordsControl() {
        PatientDAO dao = new PatientDAO();
        this.patientRecords = dao.retrieveFromFile();
    }

    public int getTotalPatients() {
        return patientRecords == null ? 0 : patientRecords.getNumberOfEntries();
    }

    public void displayTreatmentCountPerPatient() {
        if (patientRecords == null || patientRecords.isEmpty()) {
            System.out.println("No patient records found.");
            return;
        }

        System.out.println("\n--- Treatment Count Per Patient ---");
        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            PatientRecord p = patientRecords.getEntry(i);
            int treatmentCount = p.getTreatments().getNumberOfEntries();
            System.out.println(p.getId() + " | " + p.getName() + " | Treatments: " + treatmentCount);
        }
    }
}
