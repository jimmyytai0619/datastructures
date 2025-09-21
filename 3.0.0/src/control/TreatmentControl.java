package control;

import entity.Patient;
import entity.Treatment;
import adt.ListInterface;
import java.util.StringJoiner;

public class TreatmentControl {

    public void addTreatmentToPatient(Patient patient, Treatment treatment) {
        if (patient == null || treatment == null) {
            throw new IllegalArgumentException("Patient and treatment cannot be null");
        }
        patient.addTreatment(treatment);
    }

    public ListInterface<Treatment> getTreatmentHistory(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        return patient.getTreatments();
    }

    // Return report as String for flexibility
    public String generateTreatmentCountReport(ListInterface<? extends Patient> patientList) {
        if (patientList == null || patientList.isEmpty()) {
            return "No patients available for report.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Patient Treatment Count Report ===\n");
        sb.append(String.format("%-10s %-20s %-15s\n", "Patient ID", "Patient Name", "Total Treatments"));
        sb.append("------------------------------------------------------\n");

        for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
            Patient p = patientList.getEntry(i);
            int count = p.getTreatments().getNumberOfEntries();
            sb.append(String.format("%-10s %-20s %-15d\n", p.getId(), p.getName(), count));
        }

        sb.append("------------------------------------------------------\n");
        sb.append("Total Patients: ").append(patientList.getNumberOfEntries()).append("\n");
        return sb.toString();
    }

    public String generateTreatmentCoverageReport(ListInterface<? extends Patient> patientList) {
        if (patientList == null || patientList.isEmpty()) {
            return "No patients available for report.";
        }

        int treated = 0;
        for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
            Patient p = patientList.getEntry(i);
            if (p.getTreatments().getNumberOfEntries() > 0) {
                treated++;
            }
        }

        int totalPatients = patientList.getNumberOfEntries();
        int untreated = totalPatients - treated;

        StringJoiner report = new StringJoiner("\n");
        report.add("\n=== Treatment Coverage Report ===");
        report.add("Total Patients          : " + totalPatients);
        report.add("Patients with Treatments: " + treated);
        report.add("Patients without Treatments: " + untreated);

        double coverage = totalPatients == 0 ? 0 : ((double) treated / totalPatients) * 100;
        report.add(String.format("Treatment Coverage Rate : %.2f%%", coverage));

        return report.toString();
    }
}
