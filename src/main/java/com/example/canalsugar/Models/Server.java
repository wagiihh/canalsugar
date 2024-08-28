package com.example.canalsugar.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Servers")
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ServerID;

    @NotBlank(message = "Server name is required")
    @Column(name = "Servername")
    private String Servername;

    @NotBlank(message = "Server IP address is required")
    @Column(name = "ServerIP")
    private String ServerIP;
    
}
