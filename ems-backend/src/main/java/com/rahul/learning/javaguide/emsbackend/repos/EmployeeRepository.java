package com.rahul.learning.javaguide.emsbackend.repos;

import com.rahul.learning.javaguide.emsbackend.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByDepartmentId(Long departmentId);

    boolean existsByEmail(String emailId);

    @Query("""
                SELECT DISTINCT e
                FROM Employee e
                JOIN FETCH e.department
                JOIN FETCH e.office o
                JOIN FETCH o.address
                JOIN FETCH e.experiences
            """)
    List<Employee> findAllWithDetails();
}