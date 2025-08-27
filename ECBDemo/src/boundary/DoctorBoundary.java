package boundary;

import control.DoctorControl;
import control.DoctorReportGenerator;
import entity.Doctor;
import entity.DutySchedule;
import entity.AvailabilityStatus;
import utility.DateHelper;
import utility.IDGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import adt.ListInterface;

public class DoctorBoundary {
    private DoctorControl control;
    private DoctorReportGenerator reports;
    private Scanner sc;

    public DoctorBoundary() {
        control = new DoctorControl();
        reports = new DoctorReportGenerator(control);
        sc = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n--- Doctor Management Menu ---");
            System.out.println("1. List doctors");
            System.out.println("2. Add doctor");
            System.out.println("3. Update doctor");
            System.out.println("4. Remove doctor");
            System.out.println("5. Assign duty schedule");
            System.out.println("6. Show schedules for doctor");
            System.out.println("7. Reports");
            System.out.println("0. Exit");
            System.out.print("Select: ");
            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1": listDoctors(); break;
                case "2": addDoctor(); break;
                case "3": updateDoctor(); break;
                case "4": removeDoctor(); break;
                case "5": assignSchedule(); break;
                case "6": showSchedules(); break;
                case "7": reportsMenu(); break;
                case "0": System.out.println("Goodbye"); return;
                default: System.out.println("Invalid option");
            }
        }
    }

    private void listDoctors() {
        ListInterface<Doctor> list = control.listAllDoctors();
        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            System.out.println(list.getEntry(i));
        }
    }

    private void addDoctor() {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Specialization: ");
        String spec = sc.nextLine();
        System.out.print("Phone: ");
        String phone = sc.nextLine();
        System.out.print("Fee: ");
        double fee = Double.parseDouble(sc.nextLine());
        Doctor d = control.addDoctor(name, spec, phone, fee);
        System.out.println("Added: " + d.getDoctorID());
    }

    private void updateDoctor() {
        System.out.print("DoctorID: ");
        String id = sc.nextLine();
        Doctor d = control.findDoctorById(id);
        if (d == null) { System.out.println("Not found"); return; }
        System.out.print("Name ("+d.getName()+"): ");
        String name = sc.nextLine();
        System.out.print("Spec ("+d.getSpecialization()+"): ");
        String spec = sc.nextLine();
        System.out.print("Phone ("+d.getPhone()+"): ");
        String phone = sc.nextLine();
        System.out.print("Fee ("+d.getConsultationFee()+"): ");
        String feeStr = sc.nextLine();
        Double fee = feeStr.isBlank() ? null : Double.parseDouble(feeStr);
        System.out.println("Status options: 1=ACTIVE 2=ON_LEAVE 3=RETIRED");
        System.out.print("Status: ");
        String st = sc.nextLine();
        AvailabilityStatus status = null;
        if (st.equals("1")) status = AvailabilityStatus.ACTIVE;
        else if (st.equals("2")) status = AvailabilityStatus.ON_LEAVE;
        else if (st.equals("3")) status = AvailabilityStatus.RETIRED;
        boolean ok = control.updateDoctor(id, name.isBlank() ? null : name, spec.isBlank() ? null : spec,
                phone.isBlank() ? null : phone, fee, status);
        System.out.println(ok ? "Updated" : "Failed");
    }

    private void removeDoctor() {
        System.out.print("DoctorID: ");
        String id = sc.nextLine();
        boolean ok = control.removeDoctor(id);
        System.out.println(ok ? "Removed" : "Not found");
    }

    private void assignSchedule() {
        System.out.print("DoctorID: ");
        String id = sc.nextLine();
        Doctor d = control.findDoctorById(id);
        if (d == null) { System.out.println("Doctor not found"); return; }
        System.out.print("Date (YYYY-MM-DD): ");
        LocalDate date = DateHelper.parseDate(sc.nextLine());
        System.out.print("Start time (HH:MM): ");
        LocalTime st = DateHelper.parseTime(sc.nextLine());
        System.out.print("End time (HH:MM): ");
        LocalTime et = DateHelper.parseTime(sc.nextLine());
        System.out.print("Location: ");
        String loc = sc.nextLine();
        System.out.print("Shift type: ");
        String shift = sc.nextLine();

        DutySchedule ds = new DutySchedule(IDGenerator.nextScheduleID(), id, date, st, et, loc, shift);
        boolean ok = control.assignSchedule(ds);
        System.out.println(ok ? "Assigned" : "Conflict or failed (overlap?)");
    }

    private void showSchedules() {
        System.out.print("DoctorID: ");
        String id = sc.nextLine();
        Doctor d = control.findDoctorById(id);
        if (d == null) { System.out.println("Not found"); return; }
        for (int i = 1; i <= d.getSchedules().getNumberOfEntries(); i++) {
            System.out.println(d.getSchedules().getEntry(i));
        }
    }

    private void reportsMenu() {
        System.out.println("1) Monthly Roster  2) Availability (next 7)  3) Utilization (last 30 days)");
        System.out.print("Select: ");
        String r = sc.nextLine();
        switch (r) {
            case "1":
                System.out.print("Month (1-12): "); int m = Integer.parseInt(sc.nextLine());
                System.out.print("Year: "); int y = Integer.parseInt(sc.nextLine());
                System.out.println(reports.monthlyDutyRoster(m, y));
                break;
            case "2":
                System.out.println(reports.availabilitySummary(7));
                break;
            case "3":
                System.out.println(reports.utilizationReport(30));
                break;
            default: System.out.println("Invalid");
        }
    }

    // Quick main for demo
    public static void main(String[] args) {
        new DoctorBoundary().start();
    }
}

