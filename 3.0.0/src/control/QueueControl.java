package control;

import adt.ArrayList;
import adt.ListInterface;
import entity.Patient;



public class QueueControl {
    private final ListInterface<Patient> appointmentQ = new ArrayList<>();
    private final ListInterface<Patient> walkInQ      = new ArrayList<>();

    public void enqueueAppointment(Patient p) { appointmentQ.add(p); }
    public void enqueueWalkIn(Patient p)      { walkInQ.add(p); }

    public Patient callNext() {
        if (appointmentQ.getNumberOfEntries() > 0) return appointmentQ.remove(1);
        if (walkInQ.getNumberOfEntries() > 0)      return walkInQ.remove(1);
        return null;
    }

    public String snapshot() {
        return "AppointmentQ=" + appointmentQ.getNumberOfEntries()
             + " | WalkInQ=" + walkInQ.getNumberOfEntries();
    }
}
