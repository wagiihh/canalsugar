package com.example.canalsugar.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.canalsugar.Models.Admin;
import java.util.List;


public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Admin findByEmail(String email);
    
}
