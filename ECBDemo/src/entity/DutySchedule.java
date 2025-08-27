package entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class DutySchedule implements Serializable {
    private static final long serialVersionUID = 1L;

    private String scheduleID;
    private String doctorID;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String shiftType; // e.g., Morning/Afternoon/On-call

    public DutySchedule(String scheduleID, String doctorID,
                        LocalDate date, LocalTime startTime, LocalTime endTime,
                        String location, String shiftType) {
        this.scheduleID = scheduleID;
        this.doctorID = doctorID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.shiftType = shiftType;
    }

    public String getScheduleID() { return scheduleID; }
    public String getDoctorID() { return doctorID; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public String getLocation() { return location; }
    public String getShiftType() { return shiftType; }

    public double getHours() {
        Duration d = Duration.between(startTime, endTime);
        return d.toMinutes() / 60.0;
    }

    @Override
    public String toString() {
        return scheduleID + " | " + date.toString() + " " + startTime + "-" + endTime +
               " | " + shiftType + " | " + location;
    }
}
