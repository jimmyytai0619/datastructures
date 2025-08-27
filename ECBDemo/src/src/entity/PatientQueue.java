package entity;

import adt.ArrayQueue;

public class PatientQueue {

    private final ArrayQueue<Patient> queue;

    public PatientQueue() {
        this(100); 
    }

    public PatientQueue(int capacity) {
        this.queue = new ArrayQueue<>(capacity);
    }

    public void addPatient(Patient patient) {
        queue.enqueue(patient);
    }

    public Patient nextPatient() {
        return queue.dequeue();
    }

    public Patient peekNext() {
        return queue.getFront();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size(); 
    }

    public void clear() {
        queue.clear();
    }

    public void displayQueue() {
        if (queue.isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        int n = queue.size();
        System.out.println("=== Patient Waiting Queue (" + n + ") ===");
        for (int i = 0; i < n; i++) {
            Patient p = queue.dequeue();
            System.out.println((i + 1) + ". " + p);
            queue.enqueue(p); 
        }
    }
}
