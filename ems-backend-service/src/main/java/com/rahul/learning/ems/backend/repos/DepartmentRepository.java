package com.rahul.learning.ems.backend.repos;

import com.rahul.learning.ems.backend.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
