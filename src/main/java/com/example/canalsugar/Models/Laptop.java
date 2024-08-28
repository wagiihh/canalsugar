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
@Table(name="Laptops")
public class Laptop {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int laptopid;

    @NotBlank(message = "Laptop Model is required")
    @Column(name = "LaptopModel")
    private String LaptopModel;

    @NotBlank(message = "Laptop serial number is required")
    @Column(name = "LaptopSerial")
    private String laptopserial; 

    @NotBlank(message = "Laptop name is required")
    @Column(name = "Laptopname")
    private String Laptopname;

    public Laptop() {
    }
    public Laptop getLaptop()
    {
        return this;
    }
    public Laptop(int laptopid, String LaptopModel, String laptopserial, String Laptopname) {
        this.laptopid = laptopid;
        this.LaptopModel = LaptopModel;
        this.laptopserial = laptopserial;
        this.Laptopname = Laptopname;
    }

    public int getLaptopid() { // Corrected getter name
        return this.laptopid;
    }

    public void setLaptopid(int laptopid) { // Corrected setter name
        this.laptopid = laptopid;
    }

    public String getLaptopModel() {
        return this.LaptopModel;
    }

    public void setLaptopModel(String LaptopModel) {
        this.LaptopModel = LaptopModel;
    }

    public String getLaptopserial() {
        return this.laptopserial;
    }

    public void setlaptopserial(String laptopserial) {
        this.laptopserial = laptopserial;
    }

    public String getLaptopname() {
        return this.Laptopname;
    }

    public void setLaptopname(String Laptopname) {
        this.Laptopname = Laptopname;
    }



    public Laptop LaptopModel(String LaptopModel) {
        setLaptopModel(LaptopModel);
        return this;
    }

    public Laptop laptopserial(String laptopserial) {
        setlaptopserial(laptopserial);
        return this;
    }

    public Laptop Laptopname(String Laptopname) {
        setLaptopname(Laptopname);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Laptop)) {
            return false;
        }
        Laptop laptop = (Laptop) o;
        return laptopid == laptop.laptopid && Objects.equals(LaptopModel, laptop.LaptopModel) && Objects.equals(laptopserial, laptop.laptopserial) && Objects.equals(Laptopname, laptop.Laptopname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(laptopid, LaptopModel, laptopserial, Laptopname);
    }

    @Override
    public String toString() {
        return "{" +
            ", LaptopModel='" + getLaptopModel() + "'" +
            ", laptopserial='" + getLaptopserial() + "'" +
            ", Laptopname='" + getLaptopname() + "'" +
            "}";
    }
    
}
