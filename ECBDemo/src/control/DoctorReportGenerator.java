package control;

import adt.ListInterface;
import adt.ArrayList;
import entity.Doctor;
import entity.DutySchedule;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.DayOfWeek;

public class DoctorReportGenerator {
    private DoctorControl control;

    public DoctorReportGenerator(DoctorControl control) {
        this.control = control;
    }

    // 1) Monthly Duty Roster
    public String monthlyDutyRoster(int month, int year) {
        StringBuilder sb = new StringBuilder();
        sb.append("Monthly Duty Roster for ").append(year).append("-").append(String.format("%02d", month)).append("\n");
        sb.append("ID | Name | Spec | #Shifts | TotalHours\n");
        sb.append("------------------------------------------------\n");
        ListInterface<Doctor> doctors = control.listAllDoctors();
        int n = doctors.getNumberOfEntries();
        for (int i = 1; i <= n; i++) {
            Doctor d = doctors.getEntry(i);
            int shifts = 0;
            double hours = 0.0;
            int sN = d.getSchedules().getNumberOfEntries();
            for (int j = 1; j <= sN; j++) {
                DutySchedule ds = d.getSchedules().getEntry(j);
                if (ds.getDate().getYear() == year && ds.getDate().getMonthValue() == month) {
                    shifts++;
                    hours += ds.getHours();
                }
            }
            sb.append(d.getDoctorID()).append(" | ").append(d.getName()).append(" | ").append(d.getSpecialization())
              .append(" | ").append(shifts).append(" | ").append(String.format("%.2f", hours)).append("\n");
        }
        return sb.toString();
    }

    // 2) Availability Summary (next N days)
    public String availabilitySummary(int nextNDays) {
        StringBuilder sb = new StringBuilder();
        sb.append("Availability Summary (next ").append(nextNDays).append(" days)\n");
        sb.append("--------------------------------------------\n");
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(nextNDays);
        ListInterface<Doctor> doctors = control.listAllDoctors();
        int total = doctors.getNumberOfEntries();
        sb.append("Total doctors: ").append(total).append("\n");

        // specialization coverage: two ADT lists (no Map)
        ListInterface<String> specs = new ArrayList<>();
        ListInterface<Integer> counts = new ArrayList<>();

        ListInterface<String> noShifts = new ArrayList<>();

        for (int i = 1; i <= total; i++) {
            Doctor d = doctors.getEntry(i);
            // specialization tally
            String sp = d.getSpecialization();
            int sIdx = -1;
            for (int k = 1; k <= specs.getNumberOfEntries(); k++) {
                if (specs.getEntry(k).equals(sp)) { sIdx = k; break; }
            }
            if (sIdx == -1) { specs.add(sp); counts.add(1); } else { counts.replace(sIdx, counts.getEntry(sIdx) + 1); }

            // check schedule in nextN
            boolean has = false;
            for (int j = 1; j <= d.getSchedules().getNumberOfEntries(); j++) {
                LocalDate dt = d.getSchedules().getEntry(j).getDate();
                if (!dt.isBefore(start) && !dt.isAfter(end)) { has = true; break; }
            }
            if (!has) noShifts.add(d.getDoctorID() + " - " + d.getName());
        }

        sb.append("Specialization coverage:\n");
        for (int k = 1; k <= specs.getNumberOfEntries(); k++) {
            sb.append("  ").append(specs.getEntry(k)).append(" : ").append(counts.getEntry(k)).append("\n");
        }

        sb.append("\nDoctors without scheduled shifts in next ").append(nextNDays).append(" days:\n");
        if (noShifts.getNumberOfEntries() == 0) sb.append("  (none)\n");
        else {
            for (int i = 1; i <= noShifts.getNumberOfEntries(); i++) sb.append("  ").append(noShifts.getEntry(i)).append("\n");
        }

        return sb.toString();
    }

    // 3) Utilization: average weekly hours (approx) & busiest weekday
    public String utilizationReport(int lookbackDays) {
        StringBuilder sb = new StringBuilder();
        sb.append("Utilization Report (last ").append(lookbackDays).append(" days)\n");
        sb.append("-----------------------------------------------\n");
        LocalDate start = LocalDate.now().minusDays(lookbackDays);
        int[] weekdayCount = new int[7]; // 0=MON ... 6=SUN
        double totalHours = 0.0;
        int totalShifts = 0;

        ListInterface<Doctor> doctors = control.listAllDoctors();
        for (int i = 1; i <= doctors.getNumberOfEntries(); i++) {
            Doctor d = doctors.getEntry(i);
            for (int j = 1; j <= d.getSchedules().getNumberOfEntries(); j++) {
                DutySchedule ds = d.getSchedules().getEntry(j);
                if (!ds.getDate().isBefore(start) && !ds.getDate().isAfter(LocalDate.now())) {
                    totalHours += ds.getHours();
                    totalShifts++;
                    DayOfWeek dow = ds.getDate().getDayOfWeek();
                    weekdayCount[dow.getValue() - 1]++; // Monday=1
                }
            }
        }

        sb.append("Total shifts: ").append(totalShifts).append("\n");
        sb.append("Total hours: ").append(String.format("%.2f", totalHours)).append("\n");
        double avg = (double) totalHours / Math.max(1, doctors.getNumberOfEntries());
        sb.append("Avg hours per doctor (approx): ").append(String.format("%.2f", avg)).append("\n");

        // busiest weekday
        int maxIdx = 0;
        for (int k = 1; k < 7; k++) if (weekdayCount[k] > weekdayCount[maxIdx]) maxIdx = k;
        sb.append("Busiest weekday: ").append(java.time.DayOfWeek.of(maxIdx + 1)).append(" (")
          .append(weekdayCount[maxIdx]).append(" shifts)\n");

        return sb.toString();
    }
}
