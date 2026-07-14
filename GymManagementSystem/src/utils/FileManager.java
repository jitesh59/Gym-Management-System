package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileManager.java
 * Generic utility class that handles saving/loading of any Serializable
 * List of objects to/from .dat files using Object Streams.
 *
 * This class centralizes all file handling for the application so that
 * every module (Member, Trainer, Equipment, Attendance, Payment, Plans)
 * can reuse the same read/write logic (DRY principle).
 */
public class FileManager {

    // Root data directory - .dat files are stored here.
    public static final String DATA_DIR = "data" + File.separator;

    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Saves a list of serializable objects to the given file name.
     */
    @SuppressWarnings("unchecked")
    public static <T> void saveList(String fileName, List<T> list) {
        File file = new File(DATA_DIR + fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.err.println("Error saving file " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Loads a list of serializable objects from the given file name.
     * Returns an empty ArrayList if the file does not exist or cannot be read.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadList(String fileName) {
        File file = new File(DATA_DIR + fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<T>) obj;
            }
        } catch (EOFException e) {
            // empty file - treat as no data
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading file " + fileName + ": " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Appends a plain text line to a .txt file (used for exported reports and logs).
     */
    public static void appendTextLine(String fileName, String line) {
        try (FileWriter fw = new FileWriter(DATA_DIR + fileName, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing text file " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Writes (overwrites) a full text report to a file - used by the Report module
     * to export reports as .txt files.
     */
    public static void writeTextReport(String fileName, String content) {
        try (FileWriter fw = new FileWriter(DATA_DIR + fileName, false);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(content);
        } catch (IOException e) {
            System.err.println("Error writing report " + fileName + ": " + e.getMessage());
        }
    }
}
