package boundary;

import control.PatientRecordsControl;

public class PatientRecordsUI {
    private final PatientRecordsControl control;

    public PatientRecordsUI() {
        this.control = new PatientRecordsControl();
    }

    public void showReports() {
        System.out.println("\n=== PATIENT RECORD REPORTS ===");
        System.out.println("Total Registered Patients: " + control.getTotalPatients());
        control.displayTreatmentCountPerPatient();
    }
}
