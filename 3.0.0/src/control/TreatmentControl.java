package control;

import entity.Patient;
import entity.Treatment;
import adt.ListInterface;

public class TreatmentControl {

    public void addTreatmentToPatient(Patient patient, Treatment treatment) {
        patient.addTreatment(treatment);
    }

    public ListInterface<Treatment> getTreatmentHistory(Patient patient) {
        return patient.getTreatments();
    }

public void generateTreatmentCountReport(ListInterface<? extends Patient> patientList){
    System.out.println("\n=== Patient Treatment Count Report ===");
    System.out.printf("%-10s %-20s %-15s\n", "Patient ID", "Patient Name", "Total Treatments");
    System.out.println("------------------------------------------------------");

    for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
        Patient p = patientList.getEntry(i);
        int count = p.getTreatments().getNumberOfEntries();
        System.out.printf("%-10s %-20s %-15d\n", p.getId(), p.getName(), count);
    }

    System.out.println("------------------------------------------------------");
    System.out.println("Total Patients: " + patientList.getNumberOfEntries());
}

public void generateTreatmentCoverageReport(ListInterface<? extends Patient> patientList){
    System.out.println("\n=== Treatment Coverage Report ===");
    int treated = 0;

    for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
        Patient p = patientList.getEntry(i);
        if (p.getTreatments().getNumberOfEntries() > 0) {
            treated++;
        }
    }

    int totalPatients = patientList.getNumberOfEntries();
    int untreated = totalPatients - treated;

    System.out.println("Total Patients          : " + totalPatients);
    System.out.println("Patients with Treatments: " + treated);
    System.out.println("Patients without Treatments: " + untreated);

    double coverage = totalPatients == 0 ? 0 : ((double) treated / totalPatients) * 100;
    System.out.printf("Treatment Coverage Rate : %.2f%%\n", coverage);
}


}
