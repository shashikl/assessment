package com.example.assessment.repositories;

import com.example.assessment.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDao extends JpaRepository<Customer, Integer> {

}
