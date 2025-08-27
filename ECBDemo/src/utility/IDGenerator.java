package utility;

/**
 * Author: JUN
 * Simple ID generator for Doctor and Schedule.
 */

public class IDGenerator {
    private static int doctorCounter = 200;
    private static int scheduleCounter = 3000;

    public static String nextDoctorID() {
        doctorCounter++;
        return "D" + doctorCounter;
    }

    public static String nextScheduleID() {
        scheduleCounter++;
        return "S" + scheduleCounter;
    }
}
