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
    
    public void generateTotalPatientsReport() {
        int total = patientRecords.getNumberOfEntries();
        System.out.println("\n=== Total Registered Patients Report ===");
        System.out.println("Total Registered Patients: " + total);
    }

    public void generateAgeGroupDistributionReport() {
        int below18 = 0, between18_30 = 0, between31_50 = 0, above50 = 0;

        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            PatientRecord p = patientRecords.getEntry(i);
            int age = p.getAge();

            if (age < 18) {
                below18++;
            } else if (age <= 30) {
                between18_30++;
            } else if (age <= 50) {
                between31_50++;
            } else {
                above50++;
            }
        }

        System.out.println("\n=== Patient Age Group Distribution Report ===");
        System.out.printf("Below 18 years old   : %d\n", below18);
        System.out.printf("18 - 30 years old    : %d\n", between18_30);
        System.out.printf("31 - 50 years old    : %d\n", between31_50);
        System.out.printf("Above 50 years old   : %d\n", above50);
        System.out.println("Total Patients       : " + patientRecords.getNumberOfEntries());
    }

}
