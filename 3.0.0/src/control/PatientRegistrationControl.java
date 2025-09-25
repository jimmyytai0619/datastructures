package control;

import adt.ListInterface;
import adt.ArrayList;
import dao.PatientDAO;
import entity.PatientRecord;

public class PatientRegistrationControl {
    private ListInterface<PatientRecord> patientRecords;
    private final PatientDAO patientDAO;

    public ListInterface<PatientRecord> getAllPatients() {
        return patientRecords;
    }

    public PatientRegistrationControl() {
        this.patientDAO = new PatientDAO();
        this.patientRecords = patientDAO.retrieveFromFile();

        if (this.patientRecords == null) {
            this.patientRecords = new ArrayList<>();
        }
    }

    public boolean registerPatient(PatientRecord patient) {
        if (containsPatientId(patient.getId())) {
            System.out.println("Patient with ID " + patient.getId() + " already exists!");
            return false; 
        }
        patientRecords.add(patient);
        patientDAO.saveToFile(patientRecords);
        return true;
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

    public int getPatientCount() {
        return patientRecords.getNumberOfEntries();
    }

    public boolean deletePatientById(String id) {
        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            PatientRecord p = patientRecords.getEntry(i);
            if (p.getId().equalsIgnoreCase(id)) {
                patientRecords.remove(i);
                patientDAO.saveToFile(patientRecords);
                return true;
            }
        }
        return false;
    }

    public boolean updatePatient(PatientRecord updatedPatient) {
        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            PatientRecord p = patientRecords.getEntry(i);
            if (p.getId().equalsIgnoreCase(updatedPatient.getId())) {
                for (int j = 1; j <= patientRecords.getNumberOfEntries(); j++) {
                    if (j != i && patientRecords.getEntry(j).getId().equalsIgnoreCase(updatedPatient.getId())) {
                        System.out.println("Update failed: Another patient already has ID " + updatedPatient.getId());
                        return false;
                    }
                }
                patientRecords.replace(i, updatedPatient);
                patientDAO.saveToFile(patientRecords);
                return true;
            }
        }
        return false;
    }

    public void clearAllPatients() {
        patientRecords.clear();
        patientDAO.saveToFile(patientRecords);
        System.out.println("All patient records have been cleared!");
    }

    private boolean containsPatientId(String id) {
        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            if (patientRecords.getEntry(i).getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public void sortPatientsById() {
        int n = patientRecords.getNumberOfEntries();
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                PatientRecord p1 = patientRecords.getEntry(i);
                PatientRecord p2 = patientRecords.getEntry(j);
                if (p1.getId().compareToIgnoreCase(p2.getId()) > 0) {
                    patientRecords.replace(i, p2);
                    patientRecords.replace(j, p1);
                }
            }
        }
        patientDAO.saveToFile(patientRecords);
    }

    public PatientRecord binarySearchPatientById(String id) {
        sortPatientsById();

        int left = 1;
        int right = patientRecords.getNumberOfEntries();

        while (left <= right) {
            int mid = (left + right) / 2;
            PatientRecord midPatient = patientRecords.getEntry(mid);

            int cmp = midPatient.getId().compareToIgnoreCase(id);
            if (cmp == 0) {
                return midPatient;
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
}
