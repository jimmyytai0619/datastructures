package control;

import adt.ArrayQueue;
import adt.QueueInterface;
import entity.Patient;

public class HospitalSystem {
    private QueueInterface<Patient> waitingQueue;

    public HospitalSystem() {
        waitingQueue = new ArrayQueue<>(100);
    }

    public void registerPatient(Patient p) {
        waitingQueue.enqueue(p);
    }

    public Patient callNextPatient() {
        return waitingQueue.dequeue();
    }

    public Patient peekNextPatient() {
        return waitingQueue.getFront();
    }

    public boolean isQueueEmpty() {
        return waitingQueue.isEmpty();
    }
}
