package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Equipment.java
 * Represents gym equipment inventory item.
 */
public class Equipment implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int LOW_STOCK_THRESHOLD = 2;

    private String equipmentId;
    private String equipmentName;
    private int quantity;
    private LocalDate purchaseDate;
    private LocalDate maintenanceDate;
    private String status; // Working, Under Maintenance, Damaged

    public Equipment() {
    }

    public Equipment(String equipmentId, String equipmentName, int quantity,
                      LocalDate purchaseDate, LocalDate maintenanceDate, String status) {
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
        this.maintenanceDate = maintenanceDate;
        this.status = status;
    }

    public boolean isLowStock() {
        return quantity <= LOW_STOCK_THRESHOLD;
    }

    public String getEquipmentId() { return equipmentId; }
    public void setEquipmentId(String equipmentId) { this.equipmentId = equipmentId; }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public LocalDate getMaintenanceDate() { return maintenanceDate; }
    public void setMaintenanceDate(LocalDate maintenanceDate) { this.maintenanceDate = maintenanceDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return equipmentId + " - " + equipmentName;
    }
}
