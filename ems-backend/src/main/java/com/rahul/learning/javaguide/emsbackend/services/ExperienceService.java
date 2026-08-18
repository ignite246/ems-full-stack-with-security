package com.rahul.learning.javaguide.emsbackend.services;

import com.rahul.learning.javaguide.emsbackend.dtos.ExperienceDTO;

public interface ExperienceService {
    ExperienceDTO updateExperience(Long experienceId, ExperienceDTO experienceDTO);
}
