package Model;

import java.time.LocalDate;

public class Employee {
    private int empId;
    private String fName;
    private String lName;
    private LocalDate dob;
    private double salary;
    private int deptId;

    public Employee() {
    }

    public Employee(int empId, String fName, String lName, LocalDate dob, double salary, int deptId) {
        this.empId = empId;
        this.fName = fName;
        this.lName = lName;
        this.dob = dob;
        this.salary = salary;
        this.deptId = deptId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }
}
