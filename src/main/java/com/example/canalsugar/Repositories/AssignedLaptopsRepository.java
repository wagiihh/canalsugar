package com.example.canalsugar.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.canalsugar.Models.AssignedLaptops;
import com.example.canalsugar.Models.Laptop;

public interface AssignedLaptopsRepository extends JpaRepository<AssignedLaptops,Integer> {
        AssignedLaptops findByLaptop(Laptop laptop);
}
