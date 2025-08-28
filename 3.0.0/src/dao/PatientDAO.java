/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import adt.ListInterface;
import adt.ArrayList;
import entity.PatientRecord;
import java.io.*;

public class PatientDAO {
    private final String fileName = "patients.dat";
    
    public void saveToFile(ListInterface<PatientRecord> patientList) {
        File file = new File(fileName);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(patientList);
            oos.close();
        } catch (IOException e) {
            System.err.println("Error saving patient data: " + e.getMessage());
        }
    }
    
    public ListInterface<PatientRecord> retrieveFromFile() {
        File file = new File(fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            ListInterface<PatientRecord> patientList = (ArrayList<PatientRecord>) ois.readObject();
            ois.close();
            return patientList;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading patient data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

