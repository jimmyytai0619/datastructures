package control;

import adt.ArrayList;
import adt.ListInterface;
import dao.MedicineDAO;
import entity.Medicine;
import utility.MessageUI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Complete Pharmacy Management Control Class
 * 
 * @author hugol
 */
public class PharmacyControl {
    private ListInterface<Medicine> medicineList;
    private MedicineDAO medicineDAO;
    private Scanner scanner;
    
    public PharmacyControl() {
        medicineDAO = new MedicineDAO();
        medicineList = medicineDAO.retrieveFromFile();
        scanner = new Scanner(System.in);
        
        if (medicineList.isEmpty()) {
            initializeSampleMedicines();
        }
    }
    
    private void initializeSampleMedicines() {
        medicineList.add(new Medicine("M001", "Paracetamol", "BATCH001", "500mg", 
            "PharmaCo", LocalDate.of(2025, 12, 31), "Non-prescription", 
            "Pain relief", 100, 5.50, LocalDate.of(2024, 1, 15)));
        
        medicineList.add(new Medicine("M002", "Amoxicillin", "BATCH002", "250mg", 
            "MediLab", LocalDate.of(2024, 6, 30), "Prescription", 
            "Antibiotic", 50, 12.80, LocalDate.of(2023, 12, 1)));
        
        medicineDAO.saveToFile(medicineList);
    }

    // 1. Medicine Management
    public void addNewMedicine() {
       
        displayCurrentMedicineIds();
        System.out.println("\n------ ADD NEW MEDICINE ------");

        String medicineId;
        while (true) {
            medicineId = inputMedicineId();
            if (isMedicineIdExists(medicineId)) {
                System.out.println("##Medicine ID already exists! Please choose a different ID.");
                displayCurrentMedicineIds(); // 
            } else {
                break;
            }
        }

        Medicine newMedicine = inputMedicineDetails(medicineId);

        // 检查批号是否已存在
        if (isBatchNumberExists(newMedicine.getBatchNumber())) {
            System.out.println("##Warning: Batch number already exists for another medicine!");
            System.out.print("Do you want to continue anyway? (yes/no): ");
            String response = scanner.nextLine();
            if (!response.equalsIgnoreCase("yes")) {
                MessageUI.displayInvalidChoiceMessage("Medicine addition cancelled.");
                return;
            }
        }

        medicineList.add(newMedicine);
        medicineDAO.saveToFile(medicineList);
        MessageUI.displaySuccessMessage("Medicine added successfully!");

        // 显示新添加的药品信息
        System.out.println("\nNew Medicine Details:");
        System.out.println(newMedicine.getDetailedInfo());
    }
    
    private Medicine inputMedicineDetails(String medicineId) {
        System.out.print("Medicine Name: ");
        String name = scanner.nextLine();

        String batchNumber;
        while (true) {
            System.out.print("Batch Number: ");
            batchNumber = scanner.nextLine();
            if (batchNumber.trim().isEmpty()) {
                System.out.println("##Batch number cannot be empty!");
            } else {
                break;
            }
        }

        System.out.print("Dosage: ");
        String dosage = scanner.nextLine();

        System.out.print("Manufacturer: ");
        String manufacturer = scanner.nextLine();

        LocalDate expiryDate = inputDate("Expiry Date (DD/MM/YYYY): ");
        LocalDate productionDate = inputDate("Production Date (DD/MM/YYYY): ");

        if (productionDate.isAfter(expiryDate)) {
            System.out.println("##Production date cannot be after expiry date!");
            productionDate = inputDate("Please enter valid Production Date (DD/MM/YYYY): ");
        }

        String category;
        while (true) {
            System.out.print("Category (Prescription/Non-prescription): ");
            category = scanner.nextLine();
            if (category.equalsIgnoreCase("prescription") || category.equalsIgnoreCase("non-prescription")) {
                break;
            }
            System.out.println("##Please enter either 'Prescription' or 'Non-prescription'");
        }

        System.out.print("Purpose: ");
        String purpose = scanner.nextLine();

        int stockQuantity;
        while (true) {
            System.out.print("Initial Stock Quantity: ");
            if (scanner.hasNextInt()) {
                stockQuantity = scanner.nextInt();
                if (stockQuantity >= 0) {
                    break;
                }
            }
            System.out.println("##Please enter a valid non-negative number!");
            scanner.nextLine();
        }

        double price;
        while (true) {
            System.out.print("Price: RM");
            if (scanner.hasNextDouble()) {
                price = scanner.nextDouble();
                if (price >= 0) {
                    break;
                }
            }
            System.out.println("##Please enter a valid non-negative price!");
            scanner.nextLine();
        }
        scanner.nextLine();

        return new Medicine(medicineId, name, batchNumber, dosage, manufacturer, 
                          expiryDate, category, purpose, stockQuantity, price, productionDate);
    }

    private String inputMedicineId() {
        System.out.print("Medicine ID: ");
        return scanner.nextLine();
    }

    private LocalDate inputDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = scanner.nextLine();
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use DD/MM/YYYY.");
            }
        }
    }

    public void updateMedicine() {
        System.out.println("\n------ UPDATE MEDICINE ------");
        System.out.print("Enter Medicine ID: ");
        String medicineId = scanner.nextLine();
        
        Medicine medicine = findMedicineById(medicineId);
        if (medicine == null) {
            MessageUI.displayInvalidChoiceMessage("Medicine not found!");
            return;
        }
        
        System.out.println("Current details:");
        System.out.println(medicine.getDetailedInfo());
        
        // Update stock
        System.out.print("Enter new stock quantity (current: " + medicine.getStockQuantity() + "): ");
        int newStock = scanner.nextInt();
        scanner.nextLine();
        medicine.setStockQuantity(newStock);
        
        medicineDAO.saveToFile(medicineList);
        MessageUI.displaySuccessMessage("Medicine updated successfully!");
    }

    // 2. Inventory Management
    public void updateStock() {
        System.out.println("\n------ UPDATE STOCK ------");
        System.out.print("Enter Medicine ID: ");
        String medicineId = scanner.nextLine();
        
        Medicine medicine = findMedicineById(medicineId);
        if (medicine == null) {
            MessageUI.displayInvalidChoiceMessage("Medicine not found!");
            return;
        }
        
        System.out.println("Current stock: " + medicine.getStockQuantity());
        System.out.print("Enter quantity to add (use negative to reduce): ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        
        if (quantity < 0 && Math.abs(quantity) > medicine.getStockQuantity()) {
            MessageUI.displayInvalidChoiceMessage("Cannot reduce more than current stock!");
            return;
        }
        
        medicine.addStock(quantity);
        medicineDAO.saveToFile(medicineList);
        MessageUI.displaySuccessMessage("Stock updated successfully! New stock: " + medicine.getStockQuantity());
    }

    public void dispenseMedicine() {
        System.out.println("\n------ DISPENSE MEDICINE ------");
        System.out.print("Enter Medicine ID: ");
        String medicineId = scanner.nextLine();
        
        Medicine medicine = findMedicineById(medicineId);
        if (medicine == null) {
            MessageUI.displayInvalidChoiceMessage("Medicine not found!");
            return;
        }
        
        if (medicine.isExpired()) {
            MessageUI.displayInvalidChoiceMessage("Cannot dispense expired medicine!");
            return;
        }
        
        System.out.println("Available stock: " + medicine.getStockQuantity());
        System.out.print("Enter quantity to dispense: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        
        if (quantity <= 0) {
            MessageUI.displayInvalidChoiceMessage("Invalid quantity!");
            return;
        }
        
        if (quantity > medicine.getStockQuantity()) {
            MessageUI.displayInvalidChoiceMessage("Insufficient stock! Available: " + medicine.getStockQuantity());
            return;
        }
        
        medicine.reduceStock(quantity);
        medicineDAO.saveToFile(medicineList);
        MessageUI.displaySuccessMessage("Medicine dispensed successfully! Remaining stock: " + medicine.getStockQuantity());
    }

    // 3. Search and View Functions
    public Medicine findMedicineById(String medicineId) {
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            if (med.getMedicineId().equalsIgnoreCase(medicineId)) {
                return med;
            }
        }
        return null;
    }

    public void searchMedicine() {
        System.out.println("\n------ SEARCH MEDICINE ------");
        System.out.print("Enter Medicine ID: ");
        String medicineId = scanner.nextLine();
        
        Medicine medicine = findMedicineById(medicineId);
        if (medicine != null) {
            System.out.println("\nMedicine found:");
            System.out.println(medicine.getDetailedInfo());
        } else {
            MessageUI.displayInvalidChoiceMessage("Medicine not found!");
        }
    }

    public String getAllMedicines() {
        if (medicineList.isEmpty()) {
            return "No medicines available in inventory.";
        }
        
        String outputStr = "\n------------------ ALL MEDICINES ------------------\n";
        outputStr += String.format("%-8s %-20s %-12s %-15s %-8s %-10s\n", 
                                 "ID", "Name", "Batch", "Category", "Stock", "Price");
        outputStr += "----------------------------------------------------------------------------\n";
        
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            outputStr += String.format("%-8s %-20s %-12s %-15s %-8d RM%-9.2f\n", 
                                     med.getMedicineId(), med.getName(), med.getBatchNumber(),
                                     med.getCategory(), med.getStockQuantity(), med.getPrice());
        }
        return outputStr;
    }

    // 4. Alert Systems
    public String getLowStockMedicines() {
        StringBuilder outputStr = new StringBuilder();
        outputStr.append("\n--- LOW STOCK ALERT ---\n");
        outputStr.append(String.format("%-8s %-20s %-8s %-15s\n", 
                                    "ID", "Name", "Stock", "Category"));
        outputStr.append("----------------------------------------\n");
        
        boolean found = false;
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            if (med.isLowStock()) {
                outputStr.append(String.format("%-8s %-20s %-8d %-15s\n", 
                                            med.getMedicineId(), med.getName(),
                                            med.getStockQuantity(), med.getCategory()));
                found = true;
            }
        }
        
        if (!found) {
            return "No medicines with low stock.";
        }
        return outputStr.toString();
    }

    public String getExpiringMedicines() {
        StringBuilder outputStr = new StringBuilder();
        outputStr.append("\n------------ EXPIRING SOON ALERT ------------\n");
        outputStr.append(String.format("%-8s %-20s %-12s %-15s %-12s\n", 
                                    "ID", "Name", "Batch", "Expiry", "Days Left"));
        outputStr.append("------------------------------------------------------------\n");
        
        boolean found = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            if (med.willExpireSoon()) {
                outputStr.append(String.format("%-8s %-20s %-12s %-15s %-12d\n", 
                                            med.getMedicineId(), med.getName(), med.getBatchNumber(),
                                            med.getExpiryDate().format(formatter), med.getDaysUntilExpiry()));
                found = true;
            }
        }
        
        if (!found) {
            return "No medicines expiring soon.";
        }
        return outputStr.toString();
    }

    public String getExpiredMedicines() {
        StringBuilder outputStr = new StringBuilder();
        outputStr.append("\n------------ EXPIRED MEDICINES ------------\n");
        outputStr.append(String.format("%-8s %-20s %-12s %-15s\n", 
                                    "ID", "Name", "Batch", "Expiry"));
        outputStr.append("-----------------------------------------------\n");
        
        boolean found = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            if (med.isExpired()) {
                outputStr.append(String.format("%-8s %-20s %-12s %-15s\n", 
                                            med.getMedicineId(), med.getName(), med.getBatchNumber(),
                                            med.getExpiryDate().format(formatter)));
                found = true;
            }
        }
        
        if (!found) {
            return "No expired medicines.";
        }
        return outputStr.toString();
    }

    // 5. Reporting Functions
    public void generateInventoryReport() {
        System.out.println("\n========= PHARMACY INVENTORY REPORT =========");
        System.out.println("Total Medicines: " + medicineList.getNumberOfEntries());
        System.out.printf("Total Inventory Value: RM%.2f\n", calculateTotalInventoryValue());
        System.out.println(getLowStockMedicines());
        System.out.println(getExpiringMedicines());
        System.out.println(getExpiredMedicines());
    }

    public void generateSalesReport() {
        System.out.println("\n========= SALES REPORT =========");
        // This would typically connect to sales data
        System.out.println("Sales reporting feature - would show sales by medicine, time period, etc.");
        System.out.println("Total medicines dispensed today: " + getTodayDispensedCount());
    }

    public void generateCategoryReport() {
        System.out.println("\n========= CATEGORY REPORT =========");
        System.out.println("Medicines by category:");
        
        int prescriptionCount = 0;
        int nonPrescriptionCount = 0;
        
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            if ("Prescription".equalsIgnoreCase(med.getCategory())) {
                prescriptionCount++;
            } else {
                nonPrescriptionCount++;
            }
        }
        
        System.out.println("Prescription medicines: " + prescriptionCount);
        System.out.println("Non-prescription medicines: " + nonPrescriptionCount);
        System.out.println("Total: " + (prescriptionCount + nonPrescriptionCount));
    }

    private double calculateTotalInventoryValue() {
        double totalValue = 0;
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            totalValue += med.getPrice() * med.getStockQuantity();
        }
        return totalValue;
    }

    private int getTodayDispensedCount() {
        // Simulate today's dispensed count
        return (int) (Math.random() * 20) + 5;
    }
    
    private void displayCurrentMedicineIds() {
    if (medicineList.isEmpty()) {
        System.out.println("No medicines in inventory yet.");
        return;
    }
    
    System.out.println("\n======== CURRENT MEDICINE IDs AND BATCH NUMBERS ========");
    System.out.println(String.format("%-8s %-20s %-12s", "ID", "Name", "Batch No."));
    System.out.println("---------------------------------------------------------");
    
    for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
        Medicine med = medicineList.getEntry(i);
        System.out.println(String.format("%-8s %-20s %-12s", 
            med.getMedicineId(), med.getName(), med.getBatchNumber()));
    }
}

    private boolean isMedicineIdExists(String medicineId) {
        return findMedicineById(medicineId) != null;
    }

    private boolean isBatchNumberExists(String batchNumber) {
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            if (med.getBatchNumber().equalsIgnoreCase(batchNumber)) {
                return true;
            }
        }
        return false;
    }

}

