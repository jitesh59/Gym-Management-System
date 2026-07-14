package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Attendance.java
 * Represents a single attendance record for a member on a given date.
 */
public class Attendance implements Serializable {
    private static final long serialVersionUID = 1L;

    private String attendanceId;
    private String memberId;
    private String memberName;
    private LocalDate date;
    private String status; // Present, Absent

    public Attendance() {
    }

    public Attendance(String attendanceId, String memberId, String memberName, LocalDate date, String status) {
        this.attendanceId = attendanceId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.date = date;
        this.status = status;
    }

    public String getAttendanceId() { return attendanceId; }
    public void setAttendanceId(String attendanceId) { this.attendanceId = attendanceId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return date + " | " + memberName + " | " + status;
    }
}
