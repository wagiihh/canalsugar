package com.example.canalsugar.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int AdminID;

      @Email
    @NotEmpty(message = "email cannot be empty ")
    @Column(name = "email", nullable = false)
    private String email;
    
    @NotNull
    @Column(name = "pass", nullable = false)
    private String pass;
    
    @NotBlank(message = "First Name is required")
    @Column(name = "firstname")
    private String firstname;

    @NotBlank(message = "Last Name is required")
    @Column(name = "lastname")
    private String lastname;
    
    @NotBlank(message = "Number is required")
    @Column(name = "number")
    private String number;

    public Admin() {
    }

    public Admin(int AdminID, String email, String pass, String firstname, String lastname, String number) {
        this.AdminID = AdminID;
        this.email = email;
        this.pass = pass;
        this.firstname = firstname;
        this.lastname = lastname;
        this.number = number;
    }

    public int getAdminID() {
        return this.AdminID;
    }
    public Admin getAdmin()
    {
        return this;
    }

    public void setAdminID(int AdminID) {
        this.AdminID = AdminID;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Admin AdminID(int AdminID) {
        setAdminID(AdminID);
        return this;
    }

    public Admin email(String email) {
        setEmail(email);
        return this;
    }

    public Admin pass(String pass) {
        setPass(pass);
        return this;
    }

    public Admin firstname(String firstname) {
        setFirstname(firstname);
        return this;
    }

    public Admin lastname(String lastname) {
        setLastname(lastname);
        return this;
    }

    public Admin number(String number) {
        setNumber(number);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Admin)) {
            return false;
        }
        Admin admin = (Admin) o;
        return AdminID == admin.AdminID && Objects.equals(email, admin.email) && Objects.equals(pass, admin.pass) && Objects.equals(firstname, admin.firstname) && Objects.equals(lastname, admin.lastname) && Objects.equals(number, admin.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(AdminID, email, pass, firstname, lastname, number);
    }

    @Override
    public String toString() {
        return "{" +
            " AdminID='" + getAdminID() + "'" +
            ", email='" + getEmail() + "'" +
            ", pass='" + getPass() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", number='" + getNumber() + "'" +
            "}";
    }
    
}
