/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import control.TreatmentControl;
import entity.Patient;

import java.util.Scanner;

public class TreatmentBoundary {
    public static void main(String[] args) {
        TreatmentBoundary ui = new TreatmentBoundary();
        ui.run();
    }

    private TreatmentControl control = new TreatmentControl();
    private Scanner sc = new Scanner(System.in);

    private void run() {
        int choice;
        do {
            System.out.println("\n1.Register 2.Treat Next 3.Status 4.Exit");
            choice = sc.nextInt(); sc.nextLine();
            switch (choice) {
                case 1 -> register();
                case 2 -> control.treatNextPatient();
                case 3 -> control.displayQueueStatus();
                case 4 -> System.out.println("Exiting");
                default -> System.out.println("Invalid");
            }
        } while (choice != 4);
    }

    private void register() {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Treatment type: ");
        String tt = sc.nextLine();

        control.registerPatient(new Patient(name, id, tt));
    }
}
