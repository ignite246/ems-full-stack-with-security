package com.rahul.learning.javaguide.emsbackend.repos;

import com.rahul.learning.javaguide.emsbackend.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
