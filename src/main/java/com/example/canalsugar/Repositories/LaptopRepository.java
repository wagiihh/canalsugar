package com.example.canalsugar.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.canalsugar.Models.Laptop;
import java.util.List;



public interface LaptopRepository extends JpaRepository<Laptop,Integer> {
    Laptop findBylaptopserial(String laptopserial);
    Laptop findBylaptopid(int laptopid); 
}
