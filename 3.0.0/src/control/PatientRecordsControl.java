package control;

import adt.ListInterface;
import adt.ArrayList;
import dao.PatientDAO;
import entity.PatientRecord;

public class PatientRecordsControl {
    private final ListInterface<PatientRecord> patientRecords;

    public PatientRecordsControl() {
        PatientDAO dao = new PatientDAO();
        ListInterface<PatientRecord> temp = dao.retrieveFromFile();
        this.patientRecords = (temp == null) ? new ArrayList<>() : temp; 
    }
    public int getTotalPatients() {
        return patientRecords.getNumberOfEntries();
    }


    public void generateTotalPatientsReport() {
        int total = patientRecords.getNumberOfEntries();
        System.out.println("\n=== Total Registered Patients Report ===");
        System.out.println("Total Registered Patients: " + total);
    }

    public void generateAgeGroupDistributionReport() {
        if (patientRecords.isEmpty()) {
            System.out.println("\n=== Patient Age Group Distribution Report ===");
            System.out.println("No patient records found.");
            return;
        }

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
        System.out.printf("Below 18 years old   : %d%n", below18);
        System.out.printf("18 - 30 years old    : %d%n", between18_30);
        System.out.printf("31 - 50 years old    : %d%n", between31_50);
        System.out.printf("Above 50 years old   : %d%n", above50);
        System.out.println("Total Patients       : " + patientRecords.getNumberOfEntries());
    }
}
