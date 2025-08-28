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
            System.out.println(patientRecords.getEntry(i));
        }
    }

    public PatientRecord searchPatientById(String id) {
        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            PatientRecord p = patientRecords.getEntry(i);
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    public int getPatientCount() {
        return patientRecords.getNumberOfEntries();
    }
}
