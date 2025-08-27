package dao;

import java.io.*;
import adt.ListInterface;
import adt.ArrayList;
import entity.Doctor;

public class DoctorDAO {
    private static final String DATA_FILE = "doctors.dat";

    // Save the entire doctor ListInterface (e.g. ArrayList<Doctor>)
    public static boolean saveDoctors(ListInterface<Doctor> doctors) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(doctors);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Load; if file missing returns empty ArrayList.
    public static ListInterface<Doctor> loadDoctors() {
        File f = new File(DATA_FILE);
        if (!f.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof ListInterface) {
                return (ListInterface<Doctor>) obj;
            } else {
                return new ArrayList<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
