package com.example.canalsugar.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="Laptops")
public class Laptop {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int LaptopID;

    @NotBlank(message = "Laptop Model is required")
    @Column(name = "LaptopModel")
    private String LaptopModel;

    @NotBlank(message = "Laptop serial number is required")
    @Column(name = "LaptopSerial")
    private String LaptopSerial;

    @NotBlank(message = "Laptop name is required")
    @Column(name = "Laptopname")
    private String Laptopname;
}
