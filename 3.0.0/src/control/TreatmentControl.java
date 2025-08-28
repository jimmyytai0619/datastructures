package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.Patient;

public class TreatmentControl {

    private final ListInterface<Patient> queue = new ArrayList<>();

    public void registerPatient(Patient p) {
        queue.add(p);
        System.out.println(p.getName() + " registered.");
    }

    public void treatNextPatient() {
        if (queue.getNumberOfEntries() == 0) {
            System.out.println("No patients waiting.");
        } else {
            Patient next = queue.remove(1); 
            System.out.println("Treating: " + next);
        }
    }

    public void displayQueueStatus() {
        System.out.println("Patients waiting: " + queue.getNumberOfEntries());
    }
}
