package boundary;

import control.PharmacyControl;
import java.util.Scanner;

/**
 * Boundary class for Pharmacy Management UI
 * 
 * @author hugol
 */
public class PharmacyUI {
    private PharmacyControl pharmacyControl;
    private Scanner scanner;
    
    public PharmacyUI() {
        pharmacyControl = new PharmacyControl();
        scanner = new Scanner(System.in);
    }
    
    public void showMenu() {
        int choice = 0;
        do {
            displayMainMenu();  // ← 菜单显示在boundary层
            choice = getChoice();
            
            switch (choice) {
                case 1 -> addNewMedicine();
                case 2 -> updateMedicine();
                case 3 -> updateStock();
                case 4 -> dispenseMedicine();
                case 5 -> searchMedicine();
                case 6 -> viewAllMedicines();
                case 7 -> showLowStockAlert();
                case 8 -> showExpiringSoonAlert();
                case 9 -> showExpiredMedicines();
                case 10 -> generateInventoryReport();
                case 11 -> generateSalesReport();
                case 12 -> generateCategoryReport();
                case 0 -> exitSystem();
                default -> displayInvalidChoice();
            }
        } while (choice != 0);
    }
    
    // 菜单显示方法 - 现在在boundary层
    private void displayMainMenu() {
        System.out.println("\n======================================");
        System.out.println("        PHARMACY MANAGEMENT SYSTEM    ");
        System.out.println("======================================");
        
        System.out.println("[1]  Add New Medicine");
        System.out.println("[2]  Update Medicine Details");
        System.out.println("[3]  Update Stock");
        System.out.println("[4]  Dispense Medicine");
        System.out.println("[5]  Search Medicine");
        System.out.println("[6]  View All Medicines");
        
        System.out.println("--------------------------------------");
        System.out.println("[7]  Low Stock Alert");
        System.out.println("[8]  Expiring Soon Alert");
        System.out.println("[9]  Expired Medicines");
        
        System.out.println("--------------------------------------");
        System.out.println("[10] Inventory Report");
        System.out.println("[11] Sales Report");
        System.out.println("[12] Category Report");
        
        System.out.println("======================================");
        System.out.println("[0]  Exit");
        System.out.println("======================================");
        
        System.out.print("Enter your choice: ");
    }
    
    private int getChoice() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // clear invalid input
            return -1;
        } finally {
            scanner.nextLine(); // consume newline
        }
    }
    
    // 各个功能的方法（调用control层）
    private void addNewMedicine() {
        displayInputFormatGuide();
        pharmacyControl.addNewMedicine();
    }
    
    private void updateMedicine() {
        pharmacyControl.updateMedicine();
    }
    
    private void updateStock() {
        pharmacyControl.updateStock();
    }
    
    private void dispenseMedicine() {
        pharmacyControl.dispenseMedicine();
    }
    
    private void searchMedicine() {
        pharmacyControl.searchMedicine();
    }
    
    private void viewAllMedicines() {
        String medicines = pharmacyControl.getAllMedicines();
        System.out.println(medicines);
    }
    
    private void showLowStockAlert() {
        String alert = pharmacyControl.getLowStockMedicines();
        System.out.println(alert);
    }
    
    private void showExpiringSoonAlert() {
        String alert = pharmacyControl.getExpiringMedicines();
        System.out.println(alert);
    }
    
    private void showExpiredMedicines() {
        String expired = pharmacyControl.getExpiredMedicines();
        System.out.println(expired);
    }
    
    private void generateInventoryReport() {
        pharmacyControl.generateInventoryReport();
    }
    
    private void generateSalesReport() {
        pharmacyControl.generateSalesReport();
    }
    
    private void generateCategoryReport() {
        pharmacyControl.generateCategoryReport();
    }
    
    private void exitSystem() {
        System.out.println("Exiting Pharmacy Management System...");
    }
    
    private void displayInvalidChoice() {
        System.out.println("Invalid choice! Please try again.");
    }
    
    // 为外部系统提供入口
    public void runPharmacyManagement() {
        System.out.println("\n=== PHARMACY MANAGEMENT MODULE ===");
        showMenu();
    }
    
    private void displayInputFormatGuide() {
        System.out.println("\n================== INPUT FORMAT GUIDE =================");
        System.out.println("Medicine ID    : M001, M002, etc. (Unique identifier)");
        System.out.println("Batch Number   : BATCH001, LOT2024A, etc. (Manufacturer batch)");
        System.out.println("Dosage         : 500mg, 250mg/5ml, etc.");
        System.out.println("Expiry Date    : DD/MM/YYYY format (e.g., 31/12/2024)");
        System.out.println("Production Date: DD/MM/YYYY format");
        System.out.println("Category       : Prescription or Non-prescription");
        System.out.println("Purpose        : Pain relief, Antibiotic, etc.");
        System.out.println("Stock Quantity : Whole number (e.g., 100)");
        System.out.println("Price          : Decimal number (e.g., 12.50)");
        System.out.println("========================================================");
    }
    
    
}
