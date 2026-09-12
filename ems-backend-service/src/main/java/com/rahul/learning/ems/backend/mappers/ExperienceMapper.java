package com.rahul.learning.ems.backend.mappers;

import com.rahul.learning.ems.backend.dtos.ExperienceDTO;
import com.rahul.learning.ems.backend.entities.Experience;

public class ExperienceMapper {
    private ExperienceMapper() {
        /* This utility class should not be instantiated */
    }


    public static ExperienceDTO mapToExperienceDTO(Experience experience) {
        return new ExperienceDTO(
                experience.getExpId(),
                experience.getOrgName(),
                experience.getDateOfJoining(),
                experience.getDateOfLeaving(),
                experience.getCreatedAt(),
                experience.getUpdatedAt()
        );
    }


    public static Experience mapToExperience(ExperienceDTO experienceDTO) {
        return new Experience(
                experienceDTO.getExpId(),
                experienceDTO.getOrgName(),
                experienceDTO.getDateOfJoining(),
                experienceDTO.getDateOfLeaving(),
                null,
                experienceDTO.getCreatedAt(),
                experienceDTO.getUpdatedAt()
        );
    }
}
