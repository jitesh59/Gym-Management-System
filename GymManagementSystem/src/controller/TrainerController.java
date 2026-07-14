package controller;

import model.Trainer;
import utils.FileManager;
import utils.IDGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TrainerController.java
 * Handles all business logic for Trainer Management.
 */
public class TrainerController {

    private static final String TRAINERS_FILE = "trainers.dat";

    private List<Trainer> trainers;
    private Map<String, Trainer> trainerIndex;

    public TrainerController() {
        trainers = FileManager.loadList(TRAINERS_FILE);
        rebuildIndex();
    }

    private void rebuildIndex() {
        trainerIndex = new HashMap<>();
        for (Trainer t : trainers) {
            trainerIndex.put(t.getTrainerId(), t);
        }
    }

    public String getNextTrainerId() {
        return IDGenerator.generateTrainerId(trainers.size());
    }

    public boolean addTrainer(Trainer trainer) {
        if (trainerIndex.containsKey(trainer.getTrainerId())) return false;
        trainers.add(trainer);
        trainerIndex.put(trainer.getTrainerId(), trainer);
        save();
        return true;
    }

    public boolean updateTrainer(Trainer updated) {
        Trainer existing = trainerIndex.get(updated.getTrainerId());
        if (existing == null) return false;
        int idx = trainers.indexOf(existing);
        trainers.set(idx, updated);
        trainerIndex.put(updated.getTrainerId(), updated);
        save();
        return true;
    }

    public boolean deleteTrainer(String trainerId) {
        Trainer existing = trainerIndex.remove(trainerId);
        if (existing == null) return false;
        trainers.remove(existing);
        save();
        return true;
    }

    public Trainer getTrainerById(String trainerId) {
        return trainerIndex.get(trainerId);
    }

    public List<Trainer> getAllTrainers() {
        return new ArrayList<>(trainers);
    }

    public List<Trainer> search(String query) {
        List<Trainer> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) return getAllTrainers();
        String q = query.trim().toLowerCase();
        for (Trainer t : trainers) {
            if (t.getTrainerId().toLowerCase().contains(q)
                    || t.getName().toLowerCase().contains(q)
                    || (t.getPhone() != null && t.getPhone().contains(q))) {
                results.add(t);
            }
        }
        return results;
    }

    public int getTrainerCount() {
        return trainers.size();
    }

    private void save() {
        FileManager.saveList(TRAINERS_FILE, trainers);
    }
}
