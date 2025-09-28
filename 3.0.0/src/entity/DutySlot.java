package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class DutySlot implements Serializable {
    private final LocalDate date;   
    private final LocalTime start;
    private final LocalTime end;

    public DutySlot(LocalDate date, LocalTime start, LocalTime end) {
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public LocalDate getDate() { return date; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }

    @Override
    public String toString() {
        return date + " " + start + "-" + end;
    }
}
