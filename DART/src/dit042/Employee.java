package dit042;

import java.util.Calendar;

/**
 * Employee model object
 *
 * <p>
 * Employee object attributes, getters and setters
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
class Employee {
    private String employeeID;
    private String employeeName;
    private String employeePassword;
    private int employeeBirthYear;
    private String employeeAddress;
    private double employeeSalary;

    // Employee constructor
    public Employee(String employeeID, String name, String employeePassword, int birthYear, String address, double salary){
        this.employeeID = employeeID;
        this.employeeName = name;
        this.employeePassword = employeePassword;
        this.employeeBirthYear = birthYear;
        this.employeeAddress = address;
        this.employeeSalary = salary;
    }

    public Employee() {}

    // Getters
    public String getId() { return this.employeeID; }
    public String getName() {
        return this.employeeName;
    }
    public String getEmployeePassword() { return this.employeePassword; }
    public int getBirthYear() {
        return this.employeeBirthYear;
    }
    public int getAge() {
        int employeeAge = (Calendar.getInstance().get(Calendar.YEAR) - this.employeeBirthYear);
        return employeeAge;
    }
    public String getAddress() {
        return this.employeeAddress;
    }
    public double getSalary() {
        return this.employeeSalary;
    }

    // Setters
    public void setEmployeeDetails(String name, int birthYear, String address, double salary) {
        this.employeeName = name;
        this.employeeBirthYear = birthYear;
        this.employeeAddress = address;
        this.employeeSalary = salary;
    }
    public void setName(String name) { employeeName = name; }
    public void setBirthYear(int birthYear) { employeeBirthYear = birthYear; }
    public void setAddress(String address) { employeeAddress = address; }
    public void setSalary(double salary) { employeeSalary = salary; }

    // Employee toString override
    @Override
    public String toString() {
        return getId() + " : " + getName() + " - " + getBirthYear() + " ( " + getAge() + " ): " + getSalary() + " SEK.";
    }
}