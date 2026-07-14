package controller;

import model.Attendance;
import utils.FileManager;
import utils.IDGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * AttendanceController.java
 * Handles daily attendance marking, history, and monthly reports.
 */
public class AttendanceController {

    private static final String ATTENDANCE_FILE = "attendance.dat";

    private List<Attendance> records;

    public AttendanceController() {
        records = FileManager.loadList(ATTENDANCE_FILE);
    }

    public void markAttendance(String memberId, String memberName, LocalDate date, String status) {
        // Prevent duplicate marking for the same member on the same date - update instead
        for (Attendance a : records) {
            if (a.getMemberId().equals(memberId) && a.getDate().equals(date)) {
                a.setStatus(status);
                save();
                return;
            }
        }
        String id = IDGenerator.generateAttendanceId(records.size());
        records.add(new Attendance(id, memberId, memberName, date, status));
        save();
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        List<Attendance> result = new ArrayList<>();
        for (Attendance a : records) {
            if (a.getDate().equals(date)) result.add(a);
        }
        return result;
    }

    public List<Attendance> getAttendanceByMember(String memberId) {
        List<Attendance> result = new ArrayList<>();
        for (Attendance a : records) {
            if (a.getMemberId().equals(memberId)) result.add(a);
        }
        return result;
    }

    public List<Attendance> getMonthlyAttendance(int year, int month) {
        List<Attendance> result = new ArrayList<>();
        for (Attendance a : records) {
            if (a.getDate().getYear() == year && a.getDate().getMonthValue() == month) {
                result.add(a);
            }
        }
        return result;
    }

    public List<Attendance> search(String query) {
        List<Attendance> result = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) return getAllAttendance();
        String q = query.trim().toLowerCase();
        for (Attendance a : records) {
            if (a.getMemberId().toLowerCase().contains(q)
                    || a.getMemberName().toLowerCase().contains(q)) {
                result.add(a);
            }
        }
        return result;
    }

    public List<Attendance> getAllAttendance() {
        return new ArrayList<>(records);
    }

    public int getTodayAttendanceCount() {
        return getAttendanceByDate(LocalDate.now()).size();
    }

    private void save() {
        FileManager.saveList(ATTENDANCE_FILE, records);
    }
}
