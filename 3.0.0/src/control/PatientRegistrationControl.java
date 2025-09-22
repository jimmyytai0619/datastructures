package control;

import adt.ListInterface;
import adt.ArrayList;
import dao.PatientDAO;
import entity.PatientRecord;

public class PatientRegistrationControl {
    private ListInterface<PatientRecord> patientRecords;
    private final PatientDAO patientDAO;

    public ListInterface<PatientRecord> getAllPatients() {
        return patientRecords;
    }

    public PatientRegistrationControl() {
        this.patientDAO = new PatientDAO();
        this.patientRecords = patientDAO.retrieveFromFile();

        if (this.patientRecords == null) {
            this.patientRecords = new ArrayList<>();
        }
    }

    // ✅ 新增病人
    public void registerPatient(PatientRecord patient) {
        patientRecords.add(patient);
        patientDAO.saveToFile(patientRecords);
    }

    // ✅ 显示所有病人
    public void displayAllPatients() {
        if (patientRecords.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }

        System.out.println("Total patients: " + patientRecords.getNumberOfEntries());
        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            System.out.println(patientRecords.getEntry(i));
        }
    }

    // ✅ 按ID搜索病人
    public PatientRecord searchPatientById(String id) {
        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            PatientRecord p = patientRecords.getEntry(i);
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    // ✅ 病人人数
    public int getPatientCount() {
        return patientRecords.getNumberOfEntries();
    }

    // 🔹 删除病人
    public boolean deletePatientById(String id) {
        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            PatientRecord p = patientRecords.getEntry(i);
            if (p.getId().equalsIgnoreCase(id)) {
                patientRecords.remove(i);
                patientDAO.saveToFile(patientRecords); // 保存修改
                return true; // 删除成功
            }
        }
        return false; // 没找到
    }

    // 🔹 更新病人信息
    public boolean updatePatient(PatientRecord updatedPatient) {
        for (int i = 1; i <= patientRecords.getNumberOfEntries(); i++) {
            PatientRecord p = patientRecords.getEntry(i);
            if (p.getId().equalsIgnoreCase(updatedPatient.getId())) {
                // 用新的覆盖旧的
                patientRecords.replace(i, updatedPatient);
                patientDAO.saveToFile(patientRecords); // 保存修改
                return true; // 更新成功
            }
        }
        return false; // 没找到
    }
}
