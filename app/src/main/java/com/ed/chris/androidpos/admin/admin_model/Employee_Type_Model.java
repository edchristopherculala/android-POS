package com.ed.chris.androidpos.admin.admin_model;

public class Employee_Type_Model {

    int id;
    String employeetype;
    String description;

    public Employee_Type_Model(String employeetype, String description) {
        this.employeetype = employeetype;
        this.description = description;
    }

    public Employee_Type_Model(int id, String employeetype, String description) {
        this.id = id;
        this.employeetype = employeetype;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeetype() {
        return employeetype;
    }

    public void setEmployeetype(String employeetype) {
        this.employeetype = employeetype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
