package com.rahul.learning.javaguide.emsbackend.mappers;

import com.rahul.learning.javaguide.emsbackend.dtos.EmployeeDTO;
import com.rahul.learning.javaguide.emsbackend.dtos.ExperienceDTO;
import com.rahul.learning.javaguide.emsbackend.entities.Employee;
import com.rahul.learning.javaguide.emsbackend.entities.Experience;

public class EmployeeMapper {

    private EmployeeMapper() {
        /* This utility class should not be instantiated */
    }

    public static EmployeeDTO mapToEmployeeDTO(Employee employee) {

        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),

                employee.getDepartment() != null
                        ? employee.getDepartment().getId()
                        : null,

                employee.getOffice() != null
                        ? employee.getOffice().getOfficeId()
                        : null,

                employee.getExperiences()
                        .stream()
                        .map(EmployeeMapper::mapToExperienceDTO)
                        .toList(),

                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }

    private static ExperienceDTO mapToExperienceDTO(Experience experience) {
        return new ExperienceDTO(
                experience.getExpId(),
                experience.getOrgName(),
                experience.getDateOfJoining(),
                experience.getDateOfLeaving(),
                experience.getCreatedAt(),
                experience.getUpdatedAt()
        );
    }

    public static Employee mapToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        employee.setId(employeeDTO.getId());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());

        /*
         * Department and Office are deliberately NOT mapped here.
         *
         * EmployeeDTO contains IDs, while Employee contains
         * Department and Office entities.
         *
         * The service resolves those IDs using repositories.
         */

        if (employeeDTO.getExperiences() != null) {
            employeeDTO.getExperiences()
                    .forEach(experienceDTO -> {

                        Experience experience = new Experience();

                        experience.setExpId(experienceDTO.getExpId());
                        experience.setOrgName(experienceDTO.getOrgName());
                        experience.setDateOfJoining(experienceDTO.getDateOfJoining());
                        experience.setDateOfLeaving(experienceDTO.getDateOfLeaving());

                        /*
                         * This is extremely important.
                         *
                         * Employee is the parent and Experience is
                         * the owning side of the relationship.
                         *
                         * addExperience() sets:
                         *
                         * employee.experiences
                         * AND
                         * experience.employee
                         */
                        employee.addExperience(experience);
                    });
        }

        /*
         * Don't manually set createdAt/updatedAt here.
         *
         * Employee manages these through @PrePersist/@PreUpdate.
         */

        return employee;
    }
}