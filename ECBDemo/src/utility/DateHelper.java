package utility;

/**
 * Author: JUN
 * Lightweight parsing helpers. Simple fallback defaults for prototype.
 */

import java.time.LocalDate;
import java.time.LocalTime;

public class DateHelper {
    public static LocalDate parseDate(String s) {
        try { return LocalDate.parse(s.trim()); }
        catch (Exception e) { return LocalDate.now(); }
    }
    public static LocalTime parseTime(String s) {
        try { return LocalTime.parse(s.trim()); }
        catch (Exception e) { return LocalTime.of(9,0); }
    }
}
