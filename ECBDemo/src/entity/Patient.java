/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

public class Patient {
    private String name;
    private int id;
    private String treatmentType;

    public Patient(String name, int id, String treatmentType) {
        this.name = name;
        this.id = id;
        this.treatmentType = treatmentType;
    }

    public String getName() { return name; }
    public int getId() { return id; }
    public String getTreatmentType() { return treatmentType; }

    @Override
    public String toString() {
        return String.format("Patient[ID:%d, Name:%s, Treatment:%s]", id, name, treatmentType);
    }
}

