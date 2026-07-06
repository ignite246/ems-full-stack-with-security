package com.rahul.learning.javaguide.emsbackend.repos;

import com.rahul.learning.javaguide.emsbackend.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
