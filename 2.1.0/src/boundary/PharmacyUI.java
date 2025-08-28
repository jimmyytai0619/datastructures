package boundary;

import control.PharmacyControl;

/**
 * Boundary class for Pharmacy Management UI
 * 
 * @author hugol
 */
public class PharmacyUI {
    private PharmacyControl pharmacyControl;
    
    public PharmacyUI() {
        pharmacyControl = new PharmacyControl();
    }
    
    public void showMenu() {
        pharmacyControl.runPharmacyManagement();
    }
    
    // For integration with main system
    public void runPharmacyManagement() {
        System.out.println("\n=== PHARMACY MANAGEMENT MODULE ===");
        showMenu();
    }
}