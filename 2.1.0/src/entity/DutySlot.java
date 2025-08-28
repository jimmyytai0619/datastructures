package entity;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class DutySlot implements Serializable {
    private final DayOfWeek day;
    private final LocalTime start;
    private final LocalTime end;

    public DutySlot(DayOfWeek day, LocalTime start, LocalTime end) {
        this.day = day;
        this.start = start;
        this.end = end;
    }

    public DayOfWeek getDay()   { return day; }
    public LocalTime  getStart(){ return start; }
    public LocalTime  getEnd()  { return end; }

    @Override public String toString() {
        return day + " " + start + "-" + end;
    }
}
