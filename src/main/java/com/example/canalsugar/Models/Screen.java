package com.example.canalsugar.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
@Entity
@Table(name="Screens")
public class Screen {
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int LaptopID;

    @NotBlank(message = "Screen Model is required")
    @Column(name = "ScreenModel")
    private String LaptopModel;

    @NotBlank(message = "Screen serial number is required")
    @Column(name = "ScreenSerial")
    private String ScreenSerial;

    @NotBlank(message = "Screen name is required")
    @Column(name = "Screenname")
    private String Screenname;
}
