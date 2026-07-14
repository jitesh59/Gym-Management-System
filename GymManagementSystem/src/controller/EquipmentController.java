package controller;

import model.Equipment;
import utils.FileManager;
import utils.IDGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EquipmentController.java
 * Handles all business logic for Equipment Management, including
 * low-stock alerts.
 */
public class EquipmentController {

    private static final String EQUIPMENT_FILE = "equipment.dat";

    private List<Equipment> equipmentList;
    private Map<String, Equipment> equipmentIndex;

    public EquipmentController() {
        equipmentList = FileManager.loadList(EQUIPMENT_FILE);
        rebuildIndex();
    }

    private void rebuildIndex() {
        equipmentIndex = new HashMap<>();
        for (Equipment e : equipmentList) {
            equipmentIndex.put(e.getEquipmentId(), e);
        }
    }

    public String getNextEquipmentId() {
        return IDGenerator.generateEquipmentId(equipmentList.size());
    }

    public boolean addEquipment(Equipment equipment) {
        if (equipmentIndex.containsKey(equipment.getEquipmentId())) return false;
        equipmentList.add(equipment);
        equipmentIndex.put(equipment.getEquipmentId(), equipment);
        save();
        return true;
    }

    public boolean updateEquipment(Equipment updated) {
        Equipment existing = equipmentIndex.get(updated.getEquipmentId());
        if (existing == null) return false;
        int idx = equipmentList.indexOf(existing);
        equipmentList.set(idx, updated);
        equipmentIndex.put(updated.getEquipmentId(), updated);
        save();
        return true;
    }

    public boolean deleteEquipment(String equipmentId) {
        Equipment existing = equipmentIndex.remove(equipmentId);
        if (existing == null) return false;
        equipmentList.remove(existing);
        save();
        return true;
    }

    public Equipment getEquipmentById(String equipmentId) {
        return equipmentIndex.get(equipmentId);
    }

    public List<Equipment> getAllEquipment() {
        return new ArrayList<>(equipmentList);
    }

    public List<Equipment> search(String query) {
        List<Equipment> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) return getAllEquipment();
        String q = query.trim().toLowerCase();
        for (Equipment e : equipmentList) {
            if (e.getEquipmentId().toLowerCase().contains(q)
                    || e.getEquipmentName().toLowerCase().contains(q)) {
                results.add(e);
            }
        }
        return results;
    }

    public List<Equipment> getLowStockEquipment() {
        List<Equipment> results = new ArrayList<>();
        for (Equipment e : equipmentList) {
            if (e.isLowStock()) results.add(e);
        }
        return results;
    }

    public int getEquipmentCount() {
        return equipmentList.size();
    }

    private void save() {
        FileManager.saveList(EQUIPMENT_FILE, equipmentList);
    }
}
