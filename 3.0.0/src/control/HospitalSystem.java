package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.Patient;

public class HospitalSystem {
    private final ListInterface<Patient> waitingQueue;

    public HospitalSystem() {
        waitingQueue = new ArrayList<>();
    }

    public void registerPatient(Patient p) {
        waitingQueue.add(p); // 入队
    }

    public Patient callNextPatient() {
        return waitingQueue.getNumberOfEntries() == 0 ? null : waitingQueue.remove(1);
    }

    public Patient peekNextPatient() {
        return waitingQueue.getNumberOfEntries() == 0 ? null : waitingQueue.getEntry(1);
    }

    public boolean isQueueEmpty() {
        return waitingQueue.getNumberOfEntries() == 0;
    }

    public int size() {
        return waitingQueue.getNumberOfEntries();
    }
}
