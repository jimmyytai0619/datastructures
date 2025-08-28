package entity;

import adt.ArrayList;
import adt.ListInterface;

public class PatientQueue {
    private final ListInterface<Patient> queue;

    public PatientQueue() {
        this(100); // 容量参数对 ArrayList 没意义，仅保留构造签名
    }

    public PatientQueue(int capacity) {
        this.queue = new ArrayList<>();
    }

    public void addPatient(Patient patient) {
        queue.add(patient);
    }

    public Patient nextPatient() {
        return queue.getNumberOfEntries() == 0 ? null : queue.remove(1);
    }

    public Patient peekNext() {
        return queue.getNumberOfEntries() == 0 ? null : queue.getEntry(1);
    }

    public boolean isEmpty() {
        return queue.getNumberOfEntries() == 0;
    }

    public int size() {
        return queue.getNumberOfEntries();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Queue[");
        for (int i = 1; i <= queue.getNumberOfEntries(); i++) {
            if (i > 1) sb.append(", ");
            sb.append(queue.getEntry(i));
        }
        return sb.append(']').toString();
    }
}
