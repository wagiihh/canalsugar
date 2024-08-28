package com.example.canalsugar.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.canalsugar.Models.Department;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    
    
}
