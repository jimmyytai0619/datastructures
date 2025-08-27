/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import adt.ArrayQueue;
import adt.QueueInterface;
import entity.Patient;

public class TreatmentControl {
    private QueueInterface<Patient> queue = new ArrayQueue<>(100);

    public void registerPatient(Patient p) {
        queue.enqueue(p);
        System.out.println(p.getName() + " registered.");
    }

    public void treatNextPatient() {
        if (queue.isEmpty()) {
            System.out.println("No patients waiting.");
        } else {
            Patient next = queue.dequeue();
            System.out.println("Treating: " + next);
        }
    }

    public void displayQueueStatus() {
        System.out.println("Patients waiting: " + queue.size());
        // To list all, you could implement iteration if desired.
    }
}

