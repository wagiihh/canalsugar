package com.example.canalsugar.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "Departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int departmentID;

    @NotBlank(message = "Department name is required")
    @Column(name = "Departmentname")
    private String Departmentname;


    public Department() {
    }

    public Department(int departmentID, String Departmentname) {
        this.departmentID = departmentID;
        this.Departmentname = Departmentname;
    }

    public int getDepartmentID() {
        return this.departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentname() {
        return this.Departmentname;
    }

    public void setDepartmentname(String Departmentname) {
        this.Departmentname = Departmentname;
    }

    public Department departmentID(int departmentID) {
        setDepartmentID(departmentID);
        return this;
    }

    public Department Departmentname(String Departmentname) {
        setDepartmentname(Departmentname);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Department)) {
            return false;
        }
        Department department = (Department) o;
        return departmentID == department.departmentID && Objects.equals(Departmentname, department.Departmentname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentID, Departmentname);
    }

    @Override
    public String toString() {
        return "{" +
            " departmentID='" + getDepartmentID() + "'" +
            ", Departmentname='" + getDepartmentname() + "'" +
            "}";
    }
    
    
}
