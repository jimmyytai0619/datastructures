package dao;

import adt.ArrayList;
import adt.ListInterface;
import entity.Medicine;
import java.io.*;

/**
 * Data Access Object for Medicine operations
 * 
 * @author hugol
 */
public class MedicineDAO {
    private String fileName = "medicines.dat";
    
    public void saveToFile(ListInterface<Medicine> medicineList) {
        File file = new File(fileName);
        try {
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(medicineList);
            ooStream.close();
        } catch (FileNotFoundException ex) {
            System.out.println("\nFile not found: " + fileName);
        } catch (IOException ex) {
            System.out.println("\nCannot save to file: " + fileName);
        }
    }
    
    public ListInterface<Medicine> retrieveFromFile() {
        File file = new File(fileName);
        ListInterface<Medicine> medicineList = new ArrayList<>();
        try {
            if (file.exists()) {
                ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
                medicineList = (ArrayList<Medicine>) oiStream.readObject();
                oiStream.close();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("\nNo such file: " + fileName);
        } catch (IOException ex) {
            System.out.println("\nCannot read from file: " + fileName);
        } catch (ClassNotFoundException ex) {
            System.out.println("\nClass not found.");
        }
        return medicineList;
    }
}