package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Entity class for Medicine with complete attributes
 * 
 * @author hugol
 */
public class Medicine implements Serializable {
    private String medicineId;
    private String name;
    private String batchNumber;
    private String dosage;
    private String manufacturer;
    private LocalDate expiryDate;
    private String category; // Prescription/Non-prescription
    private String purpose;
    private int stockQuantity;
    private double price;
    private LocalDate productionDate;

    public Medicine() {
    }

    public Medicine(String medicineId, String name, String batchNumber, String dosage, 
                   String manufacturer, LocalDate expiryDate, String category, 
                   String purpose, int stockQuantity, double price, LocalDate productionDate) {
        this.medicineId = medicineId;
        this.name = name;
        this.batchNumber = batchNumber;
        this.dosage = dosage;
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
        this.category = category;
        this.purpose = purpose;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.productionDate = productionDate;
    }

    // Getters and Setters
    public String getMedicineId() { return medicineId; }
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDate getProductionDate() { return productionDate; }
    public void setProductionDate(LocalDate productionDate) { this.productionDate = productionDate; }

    // Business methods
    public void reduceStock(int quantity) {
        if (quantity <= stockQuantity) {
            stockQuantity -= quantity;
        }
    }

    public void addStock(int quantity) {
        stockQuantity += quantity;
    }

    public boolean isLowStock() {
        return stockQuantity < 10; // Low stock threshold
    }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    public boolean willExpireSoon() {
        return expiryDate.isBefore(LocalDate.now().plusMonths(3)) && !isExpired();
    }

    public int getDaysUntilExpiry() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("ID: %s | Name: %s | Batch: %s | Category: %s | Stock: %d | Price: RM%.2f | Expiry: %s",
                medicineId, name, batchNumber, category, stockQuantity, price, expiryDate.format(formatter));
    }

    public String getDetailedInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format(
            "Medicine ID: %s\nName: %s\nBatch Number: %s\nDosage: %s\nManufacturer: %s\n" +
            "Expiry Date: %s\nCategory: %s\nPurpose: %s\nStock Quantity: %d\nPrice: RM%.2f\nProduction Date: %s",
            medicineId, name, batchNumber, dosage, manufacturer, expiryDate.format(formatter),
            category, purpose, stockQuantity, price, productionDate.format(formatter)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Medicine medicine = (Medicine) obj;
        return medicineId.equals(medicine.medicineId);
    }

    @Override
    public int hashCode() {
        return medicineId.hashCode();
    }
}